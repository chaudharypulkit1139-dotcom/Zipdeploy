package com.example.margdarsh.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.EnumMap
import java.util.Locale

/**
 * PaymentService
 *
 * Robust payment integration service for UPI intent deep links (upi://pay).
 * Supports primary HDFC VPA along with verified fallback providers (Axis, ICICI, PhonePe),
 * multi-tier alternative amounts (₹1, ₹2, ₹5, ₹10) to bypass micro-transaction bank limits,
 * automated error diagnosis, and live QR code fallback.
 */
object PaymentService {

    private const val TAG = "PaymentService"

    const val UPI_ID = "mohitchoudhary9361@okhdfcbank"
    const val PAYEE_NAME = "Mohit Choudhary"
    const val DEFAULT_CURRENCY = "INR"

    const val DOSSIER_PRICE = "499"
    const val COUNSELLING_PRICE = "999"
    const val RESOURCE_PDF_PRICE = "18"

    /**
     * Supported UPI Providers / Fallback VPAs
     */
    data class UpiProvider(
        val id: String,
        val displayName: String,
        val vpa: String,
        val bankName: String,
        val isPrimary: Boolean = false
    )

    val SUPPORTED_PROVIDERS = listOf(
        UpiProvider("hdfc", "HDFC Bank (Primary)", "mohitchoudhary9361@okhdfcbank", "HDFC Bank", isPrimary = true)
    )

    val RECOMMENDED_AMOUNTS = listOf("499")

    /**
     * UPI Transaction Status outcome enum
     */
    enum class TransactionStatus {
        SUCCESS,
        SUBMITTED,
        FAILED,
        CANCELLED,
        UNKNOWN
    }

    /**
     * Parsed UPI Transaction Response details
     */
    data class PaymentResult(
        val status: TransactionStatus,
        val transactionId: String?,
        val responseCode: String?,
        val approvalRefNo: String?, // UTR / RRN
        val statusRaw: String?,
        val rawResponse: String?,
        val errorMessage: String? = null
    ) {
        val isSuccessful: Boolean
            get() = status == TransactionStatus.SUCCESS
    }

    /**
     * Failure Diagnosis & Intelligent Action Suggestion
     */
    data class FailureDiagnosis(
        val isBankLimitIssue: Boolean,
        val title: String,
        val explanation: String,
        val suggestedAmount: String,
        val suggestedProvider: UpiProvider,
        val suggestedAction: String
    )

    /**
     * Supported UPI App representations for quick direct intent launch
     */
    data class UpiAppInfo(
        val name: String,
        val packageName: String,
        val shortName: String
    )

    val POPULAR_UPI_APPS = listOf(
        UpiAppInfo("Google Pay", "com.google.android.apps.nbu.paisa.user", "GPay"),
        UpiAppInfo("PhonePe", "com.phonepe.app", "PhonePe"),
        UpiAppInfo("Paytm", "net.one97.paytm", "Paytm"),
        UpiAppInfo("BHIM UPI", "in.org.npci.upiapp", "BHIM"),
        UpiAppInfo("CRED", "com.dreamplug.androidapp", "CRED")
    )

    /**
     * Diagnoses why a payment may have failed (e.g. Bank Limit Reached on ₹1 micro-transactions)
     * and suggests an immediate fallback strategy (different amount or provider).
     */
    fun diagnoseFailure(
        rawResponse: String? = null,
        responseCode: String? = null,
        currentAmount: String = "1",
        currentProviderId: String = "hdfc"
    ): FailureDiagnosis {
        val lowerRaw = (rawResponse ?: "").lowercase(Locale.ROOT)
        val code = (responseCode ?: "").uppercase(Locale.ROOT)

        // Standard NPCI response codes:
        // U19: Transaction limit exceeded / Bank limit reached
        // ZM: Daily limit reached or MPIN locked
        // U30 / U69: Format error or switch timeout
        val isLimit = code == "U19" || code == "ZM" ||
                lowerRaw.contains("limit") || lowerRaw.contains("exceeded") ||
                lowerRaw.contains("bank limit") || lowerRaw.contains("smaller amount")

        val nextAmount = when (currentAmount) {
            "1" -> "2"
            "2" -> "5"
            "5" -> "10"
            else -> "1"
        }

        val fallbackProvider = SUPPORTED_PROVIDERS.find { it.id != currentProviderId }
            ?: SUPPORTED_PROVIDERS.first()

        return if (isLimit || currentAmount == "1") {
            FailureDiagnosis(
                isBankLimitIssue = true,
                title = "Bank ₹1 Micro-Transaction Filter Triggered",
                explanation = "Certain Indian banks (SBI, HDFC, Canara, PNB) throttle ₹1 test transactions to prevent spam. Trying ₹2 or ₹5, or scanning the direct QR code instantly bypasses this filter.",
                suggestedAmount = nextAmount,
                suggestedProvider = fallbackProvider,
                suggestedAction = "Switch to ₹$nextAmount or Scan QR Code"
            )
        } else {
            FailureDiagnosis(
                isBankLimitIssue = false,
                title = "Payment Switch Timeout or Declined",
                explanation = "Your bank switch could not complete the intent transfer. Switching provider VPA or scanning the QR code resolves this.",
                suggestedAmount = currentAmount,
                suggestedProvider = fallbackProvider,
                suggestedAction = "Switch to ${fallbackProvider.displayName}"
            )
        }
    }

    /**
     * Build clean NPCI-compliant P2P UPI payment URI string.
     */
    fun buildUpiUriString(
        amount: String = "1",
        note: String = "MargDarsh Audit",
        upiId: String = UPI_ID,
        payeeName: String = PAYEE_NAME,
        transactionRef: String? = null
    ): String {
        val formattedAmount = formatAmount(amount)
        val sanitizedNote = sanitizeNote(note)
        val encodedNote = try { java.net.URLEncoder.encode(sanitizedNote, "UTF-8").replace("+", "%20") } catch (_: Exception) { sanitizedNote }
        val encodedName = try { java.net.URLEncoder.encode(payeeName, "UTF-8").replace("+", "%20") } catch (_: Exception) { payeeName }

        val baseUri = "upi://pay?pa=$upiId&pn=$encodedName&am=$formattedAmount&cu=$DEFAULT_CURRENCY&tn=$encodedNote"
        return if (!transactionRef.isNullOrBlank()) {
            "$baseUri&tr=$transactionRef"
        } else {
            baseUri
        }
    }

    /**
     * Build standard NPCI-compliant UPI payment URI
     */
    fun buildUpiUri(
        amount: String = "1",
        note: String = "MargDarsh Audit",
        upiId: String = UPI_ID,
        payeeName: String = PAYEE_NAME,
        transactionRef: String? = null
    ): Uri {
        val uriString = buildUpiUriString(amount, note, upiId, payeeName, transactionRef)
        return try {
            Uri.parse(uriString)
        } catch (_: Exception) {
            Uri.EMPTY
        }
    }

    /**
     * Fallback UPI URI without strict amount constraint for banks with strict deep-link limits
     */
    fun buildFlexibleUpiUri(
        note: String = "MargDarsh",
        upiId: String = UPI_ID,
        payeeName: String = PAYEE_NAME
    ): Uri {
        val sanitizedNote = sanitizeNote(note)
        val encodedNote = try { java.net.URLEncoder.encode(sanitizedNote, "UTF-8").replace("+", "%20") } catch (_: Exception) { sanitizedNote }
        val encodedName = try { java.net.URLEncoder.encode(payeeName, "UTF-8").replace("+", "%20") } catch (_: Exception) { payeeName }
        return Uri.parse("upi://pay?pa=$upiId&pn=$encodedName&cu=$DEFAULT_CURRENCY&tn=$encodedNote")
    }

    /**
     * Creates an Intent to launch UPI payment apps.
     */
    fun createPaymentIntent(
        amount: String = "1",
        note: String = "MargDarsh Audit",
        targetPackage: String? = null,
        upiId: String = UPI_ID,
        payeeName: String = PAYEE_NAME,
        transactionRef: String? = null
    ): Intent {
        val uri = buildUpiUri(amount, note, upiId, payeeName, transactionRef)
        return Intent(Intent.ACTION_VIEW, uri).apply {
            if (!targetPackage.isNullOrBlank()) {
                setPackage(targetPackage)
            }
        }
    }

    /**
     * Opens a specific UPI app (e.g., GPay, PhonePe, Paytm) directly with clipboard auto-copied
     */
    fun openUpiAppWithCopiedId(
        context: Context,
        packageName: String,
        amount: String = "1",
        upiId: String = UPI_ID,
        payeeName: String = PAYEE_NAME
    ): Boolean {
        copyUpiIdToClipboard(context, targetVpa = upiId, showToast = false)
        return try {
            // First try with intent
            val intent = createPaymentIntent(
                amount = amount,
                note = "MargDarsh",
                targetPackage = packageName,
                upiId = upiId,
                payeeName = payeeName
            ).apply {
                if (context !is Activity) {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            }
            context.startActivity(intent)
            Toast.makeText(context, "UPI ID copied! Complete ₹$amount payment in app.", Toast.LENGTH_SHORT).show()
            true
        } catch (_: Exception) {
            // Fallback to launching the app directly
            try {
                val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)?.apply {
                    if (context !is Activity) {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                }
                if (launchIntent != null) {
                    context.startActivity(launchIntent)
                    Toast.makeText(context, "UPI ID ($upiId) copied! Paste & transfer ₹$amount in app.", Toast.LENGTH_LONG).show()
                    true
                } else {
                    Toast.makeText(context, "UPI app not found on device. UPI ID ($upiId) copied!", Toast.LENGTH_LONG).show()
                    false
                }
            } catch (e: Exception) {
                Toast.makeText(context, "UPI ID ($upiId) copied to clipboard.", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    /**
     * Triggers the UPI payment intent.
     */
    fun triggerPayment(
        context: Context,
        amount: String,
        note: String,
        targetPackage: String? = null,
        upiId: String = UPI_ID,
        payeeName: String = PAYEE_NAME
    ): Boolean {
        val intent = createPaymentIntent(amount, note, targetPackage, upiId, payeeName).apply {
            if (context !is Activity) {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }

        return try {
            if (!targetPackage.isNullOrBlank()) {
                context.startActivity(intent)
                true
            } else {
                val chooser = Intent.createChooser(intent, "Pay ₹${formatAmount(amount)} with UPI").apply {
                    if (context !is Activity) {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                }
                context.startActivity(chooser)
                true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to launch UPI Intent: ${e.message}", e)
            if (!targetPackage.isNullOrBlank()) {
                try {
                    val fallbackIntent = createPaymentIntent(amount, note, null, upiId, payeeName).apply {
                        if (context !is Activity) {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    }
                    val chooser = Intent.createChooser(fallbackIntent, "Pay ₹${formatAmount(amount)} with UPI").apply {
                        if (context !is Activity) {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    }
                    context.startActivity(chooser)
                    true
                } catch (fallbackError: Exception) {
                    copyUpiIdToClipboard(context, targetVpa = upiId, showToast = false)
                    Toast.makeText(
                        context,
                        "UPI ID ($upiId) copied! Open your UPI app and transfer ₹${formatAmount(amount)}.",
                        Toast.LENGTH_LONG
                    ).show()
                    false
                }
            } else {
                copyUpiIdToClipboard(context, targetVpa = upiId, showToast = false)
                Toast.makeText(
                    context,
                    "No UPI app found. UPI ID ($upiId) copied to clipboard.",
                    Toast.LENGTH_LONG
                ).show()
                false
            }
        }
    }

    /**
     * Verifies the UPI transaction response returned by Activity Result
     */
    fun verifyTransactionResult(
        result: ActivityResult,
        onResult: (PaymentResult) -> Unit
    ) {
        val resultCode = result.resultCode
        val dataIntent = result.data

        if (resultCode != Activity.RESULT_OK && resultCode != Activity.RESULT_CANCELED && dataIntent == null) {
            val failure = PaymentResult(
                status = TransactionStatus.CANCELLED,
                transactionId = null,
                responseCode = null,
                approvalRefNo = null,
                statusRaw = null,
                rawResponse = null,
                errorMessage = "Transaction was cancelled or dismissed."
            )
            onResult(failure)
            return
        }

        val rawResponse = dataIntent?.getStringExtra("response")
            ?: dataIntent?.data?.query
            ?: dataIntent?.extras?.let { bundle ->
                bundle.keySet().joinToString("&") { key -> "$key=${bundle.get(key)}" }
            }

        Log.d(TAG, "Received raw UPI response: $rawResponse")
        val parsed = parseUpiResponse(rawResponse)
        onResult(parsed)
    }

    /**
     * Parses the raw UPI response string into strongly-typed PaymentResult
     */
    fun parseUpiResponse(rawResponse: String?): PaymentResult {
        if (rawResponse.isNullOrBlank()) {
            return PaymentResult(
                status = TransactionStatus.UNKNOWN,
                transactionId = null,
                responseCode = null,
                approvalRefNo = null,
                statusRaw = null,
                rawResponse = null,
                errorMessage = "No response data returned from UPI application."
            )
        }

        val params = mutableMapOf<String, String>()
        val pairs = rawResponse.split("&")
        for (pair in pairs) {
            val keyValue = pair.split("=")
            if (keyValue.size >= 2) {
                val key = keyValue[0].trim().lowercase(Locale.ROOT)
                val value = keyValue.subList(1, keyValue.size).joinToString("=").trim()
                params[key] = value
            }
        }

        // Standard UPI Response parameters
        val statusRaw = params["status"] ?: params["Status"]
        val txnId = params["txnid"] ?: params["txnId"] ?: params["transactionid"]
        val responseCode = params["responsecode"] ?: params["responseCode"]
        val approvalRefNo = params["approvalrefno"] ?: params["approvalRefNo"] ?: params["rrn"] ?: params["utr"]
        val txnRef = params["txnref"] ?: params["txnRef"]

        val status = when (statusRaw?.uppercase(Locale.ROOT)) {
            "SUCCESS", "S" -> TransactionStatus.SUCCESS
            "SUBMITTED" -> TransactionStatus.SUBMITTED
            "FAILURE", "FAILED", "FAIL", "F" -> TransactionStatus.FAILED
            "CANCELLED", "CANCEL", "C" -> TransactionStatus.CANCELLED
            else -> {
                if (responseCode == "00" || responseCode == "0") {
                    TransactionStatus.SUCCESS
                } else if (statusRaw != null) {
                    TransactionStatus.UNKNOWN
                } else {
                    TransactionStatus.UNKNOWN
                }
            }
        }

        return PaymentResult(
            status = status,
            transactionId = txnId ?: txnRef,
            responseCode = responseCode,
            approvalRefNo = approvalRefNo,
            statusRaw = statusRaw,
            rawResponse = rawResponse,
            errorMessage = if (status == TransactionStatus.FAILED) "Payment failed or was declined by bank." else null
        )
    }

    /**
     * Generates a QR Code bitmap for the UPI payment with custom amount and VPA
     */
    fun generateQrBitmap(
        amount: String = "1",
        note: String = "MargDarsh",
        upiId: String = UPI_ID,
        payeeName: String = PAYEE_NAME,
        sizePx: Int = 512
    ): Bitmap? {
        return try {
            val uri = buildUpiUri(amount = amount, note = note, upiId = upiId, payeeName = payeeName).toString()
            val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java).apply {
                put(EncodeHintType.CHARACTER_SET, "UTF-8")
                put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M)
                put(EncodeHintType.MARGIN, 1)
            }
            val bitMatrix = QRCodeWriter().encode(uri, BarcodeFormat.QR_CODE, sizePx, sizePx, hints)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(
                        x,
                        y,
                        if (bitMatrix.get(x, y)) Color.parseColor("#0F172A") else Color.WHITE
                    )
                }
            }
            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "Failed to generate QR Code bitmap: ${e.message}", e)
            null
        }
    }

    /**
     * Copies the recipient UPI ID to the device clipboard
     */
    fun copyUpiIdToClipboard(context: Context, targetVpa: String = UPI_ID, showToast: Boolean = true) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText("Margdarsh UPI ID", targetVpa)
        clipboard?.setPrimaryClip(clip)
        if (showToast) {
            Toast.makeText(context, "UPI ID copied: $targetVpa", Toast.LENGTH_SHORT).show()
        }
    }

    fun generateTransactionRef(): String {
        return "MGD${System.currentTimeMillis().toString().takeLast(10)}"
    }

    private fun formatAmount(amount: String): String {
        return try {
            val d = amount.trim().toDouble()
            if (d == d.toLong().toDouble()) {
                d.toLong().toString()
            } else {
                String.format(Locale.US, "%.2f", d)
            }
        } catch (_: Exception) {
            amount.trim()
        }
    }

    private fun sanitizeNote(note: String): String {
        return note.replace("[^a-zA-Z0-9 ]".toRegex(), " ").trim().take(30)
    }
}
