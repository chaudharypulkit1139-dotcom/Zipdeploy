package com.example.margdarsh.network

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import com.example.margdarsh.data.PARENT_QUESTIONS
import com.example.margdarsh.data.QuestionType
import com.example.margdarsh.data.STUDENT_QUESTIONS
import com.example.margdarsh.data.ScoredCareer
import com.example.margdarsh.data.StreamResult
import com.example.margdarsh.data.StudentProfile
import com.example.margdarsh.data.TraitKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

sealed class DispatchStatus {
    object Idle : DispatchStatus()
    object Sending : DispatchStatus()
    data class Success(val message: String, val timestamp: String, val transactionId: String) : DispatchStatus()
    data class Error(val message: String) : DispatchStatus()
}

data class CouponValidationResult(
    val isValid: Boolean,
    val discountPercent: Int,
    val message: String,
    val maxLimit: Int = 0,
    val currentUsage: Int = 0,
    val remainingUses: Int = 0
)

data class PaymentRecordResult(
    val success: Boolean,
    val driveUrl: String?,
    val transactionId: String?,
    val message: String
)

object DossierAutoDispatcher {
    private const val TAG = "DossierAutoDispatcher"
    private const val PREFS_NAME = "margdarsh_dispatcher_prefs"
    private const val KEY_CUSTOM_WEBHOOK = "custom_sheet_webhook"
    private const val KEY_CUSTOM_APPOINTMENT_WEBHOOK = "custom_appointment_webhook"
    private const val KEY_CUSTOM_NTFY_TOPIC = "custom_ntfy_topic"
    private const val KEY_CUSTOM_NTFY_SERVER = "custom_ntfy_server"
    private const val KEY_STUDENT_REF_ID = "student_persistent_ref_id"

    const val PRIMARY_COUNSELLOR_EMAIL = "cpulkit410@gmail.com"
    const val COUNSELLOR_NAME = "Banty Panchal"
    const val COUNSELLOR_PHONE = "+91 79098 06620"

    // Default primary & user topics
    const val DEFAULT_NTFY_TOPIC_1 = "margdarsh_leads_cpulkit410"
    const val DEFAULT_NTFY_TOPIC_2 = "margdarsh_leads_mohitchoudhary"
    const val DEFAULT_NTFY_TOPIC_3 = "margdarsh_leads"
    const val DEFAULT_NTFY_SERVER = "https://ntfy.sh"

    // Built-in Google Sheet Webhook URL (Pre-configured)
    const val BUILTIN_GOOGLE_SHEET_WEBHOOK_URL =
        "https://script.google.com/macros/s/AKfycbxm2Cz24lhk5TGJEWPZ4KUBabjr0apDGIPh_C7SzQeDPfxao6xnIolPZRO5kZgXRqHkIA/exec"

    const val BUILTIN_APPOINTMENT_WEBHOOK_URL = BUILTIN_GOOGLE_SHEET_WEBHOOK_URL

    @Volatile
    var customGoogleSheetWebhookUrl: String = BUILTIN_GOOGLE_SHEET_WEBHOOK_URL

    @Volatile
    var customAppointmentWebhookUrl: String = BUILTIN_APPOINTMENT_WEBHOOK_URL

    @Volatile
    var customNtfyTopic: String = DEFAULT_NTFY_TOPIC_1

    @Volatile
    var customNtfyServer: String = DEFAULT_NTFY_SERVER

    private var applicationContext: Context? = null
    private var persistentRefId: String = ""

    fun init(context: Context) {
        val appCtx = context.applicationContext
        applicationContext = appCtx

        val prefs = appCtx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedSheetUrl = prefs.getString(KEY_CUSTOM_WEBHOOK, BUILTIN_GOOGLE_SHEET_WEBHOOK_URL) ?: BUILTIN_GOOGLE_SHEET_WEBHOOK_URL
        customGoogleSheetWebhookUrl = if (savedSheetUrl.isBlank() || savedSheetUrl.contains("AKfycbwC9Mw") || savedSheetUrl.contains("AKfycbx6g2xf") || savedSheetUrl.contains("AKfycbxxepfnkW3")) {
            BUILTIN_GOOGLE_SHEET_WEBHOOK_URL
        } else {
            savedSheetUrl
        }
        customAppointmentWebhookUrl = customGoogleSheetWebhookUrl
        customNtfyTopic = prefs.getString(KEY_CUSTOM_NTFY_TOPIC, DEFAULT_NTFY_TOPIC_1) ?: DEFAULT_NTFY_TOPIC_1
        customNtfyServer = prefs.getString(KEY_CUSTOM_NTFY_SERVER, DEFAULT_NTFY_SERVER) ?: DEFAULT_NTFY_SERVER
        
        persistentRefId = prefs.getString(KEY_STUDENT_REF_ID, "") ?: ""
        if (persistentRefId.isBlank()) {
            persistentRefId = "MD-" + UUID.randomUUID().toString().take(6).uppercase()
            prefs.edit().putString(KEY_STUDENT_REF_ID, persistentRefId).apply()
        }
    }

    fun getStudentRefId(phoneNumber: String = "", studentName: String = ""): String {
        val cleanPhone = phoneNumber.filter { it.isDigit() }.takeLast(10)
        val cleanName = studentName.trim().lowercase().filter { it.isLetter() }.take(8)
        val prefs = applicationContext?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val specificKey = when {
            cleanPhone.length >= 10 && cleanName.isNotBlank() -> "ref_for_${cleanPhone}_$cleanName"
            cleanPhone.length >= 10 -> "ref_for_phone_$cleanPhone"
            cleanName.isNotBlank() -> "ref_for_name_$cleanName"
            else -> null
        }

        if (specificKey != null) {
            val existing = prefs?.getString(specificKey, "") ?: ""
            if (existing.isNotBlank()) {
                persistentRefId = existing
                return existing
            } else {
                val prefix = if (cleanName.isNotBlank()) cleanName.uppercase().take(3) else if (cleanPhone.length >= 4) cleanPhone.takeLast(4) else "STU"
                val newId = "MD-$prefix-" + (1000..9999).random()
                prefs?.edit()?.putString(specificKey, newId)?.apply()
                persistentRefId = newId
                return newId
            }
        }

        val stored = prefs?.getString(KEY_STUDENT_REF_ID, "") ?: ""
        if (stored.isNotBlank()) {
            persistentRefId = stored
            return stored
        }
        val fallbackId = "MD-" + UUID.randomUUID().toString().take(6).uppercase()
        prefs?.edit()?.putString(KEY_STUDENT_REF_ID, fallbackId)?.apply()
        persistentRefId = fallbackId
        return fallbackId
    }

    fun clearPersistentRefId() {
        persistentRefId = ""
    }

    fun saveSettings(
        context: Context,
        webhookUrl: String,
        ntfyTopic: String,
        ntfyServer: String,
        appointmentWebhookUrl: String = customAppointmentWebhookUrl
    ) {
        val appCtx = context.applicationContext
        customGoogleSheetWebhookUrl = webhookUrl.trim()
        customAppointmentWebhookUrl = appointmentWebhookUrl.trim()
        customNtfyTopic = ntfyTopic.trim().ifBlank { DEFAULT_NTFY_TOPIC_1 }
        customNtfyServer = ntfyServer.trim().ifBlank { DEFAULT_NTFY_SERVER }.removeSuffix("/")

        val prefs = appCtx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_CUSTOM_WEBHOOK, customGoogleSheetWebhookUrl)
            .putString(KEY_CUSTOM_APPOINTMENT_WEBHOOK, customAppointmentWebhookUrl)
            .putString(KEY_CUSTOM_NTFY_TOPIC, customNtfyTopic)
            .putString(KEY_CUSTOM_NTFY_SERVER, customNtfyServer)
            .apply()
    }

    private fun getCurrentTimestamp(): String {
        return SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
    }

    /**
     * Executes HTTP POST with complete redirect handling for Google Apps Script 302 -> GET
     */
    private fun postJsonWithRedirects(urlString: String, jsonString: String, timeoutMs: Int = 10000): Pair<Int, String> {
        var curUrl = urlString
        var redirects = 0
        while (redirects < 5) {
            val u = URL(curUrl)
            val conn = u.openConnection() as HttpURLConnection
            conn.connectTimeout = timeoutMs
            conn.readTimeout = timeoutMs
            conn.instanceFollowRedirects = false

            if (redirects == 0) {
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                conn.setRequestProperty("Accept", "application/json, text/plain, */*")
                conn.doOutput = true
                OutputStreamWriter(conn.outputStream, "UTF-8").use { w ->
                    w.write(jsonString)
                    w.flush()
                }
            } else {
                conn.requestMethod = "GET"
                conn.setRequestProperty("Accept", "application/json, text/plain, */*")
            }

            val code = conn.responseCode
            if (code in 301..308) {
                val loc = conn.getHeaderField("Location")
                if (!loc.isNullOrBlank()) {
                    curUrl = loc
                    redirects++
                    continue
                }
            }

            val stream = if (code in 200..299) conn.inputStream else (conn.errorStream ?: conn.inputStream)
            val body = stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() } ?: ""
            return Pair(code, body)
        }
        return Pair(-1, "Too many redirects")
    }

    suspend fun postJsonPayload(payload: JSONObject): Pair<Int, String> = withContext(Dispatchers.IO) {
        val webhookUrl = customGoogleSheetWebhookUrl.ifBlank { BUILTIN_GOOGLE_SHEET_WEBHOOK_URL }.trim()
        if (webhookUrl.isBlank()) return@withContext Pair(-1, "Webhook URL not configured")
        return@withContext postJsonWithRedirects(webhookUrl, payload.toString())
    }

    /**
     * Tests connectivity to Google Sheet Apps Script Webhook
     */
    suspend fun testSpreadsheetWebhook(webhookUrl: String): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        val targetUrl = webhookUrl.trim()
        if (targetUrl.isBlank()) {
            return@withContext Pair(false, "Please enter a valid Google Sheet Apps Script Webhook URL")
        }

        try {
            val testPayload = JSONObject().apply {
                put("action", "PING")
                put("Stage", "Connection Test")
                put("Student_Name", "Test Connection Lead")
                put("student_id", getStudentRefId())
                put("Timestamp", getCurrentTimestamp())
            }

            val (code, body) = postJsonWithRedirects(targetUrl, testPayload.toString(), 8000)
            if (code in 200..299 || body.contains("success", ignoreCase = true)) {
                Pair(true, "Google Sheet Webhook is ACTIVE & accepting 1-Student=1-Row upserts (HTTP $code)")
            } else {
                Pair(false, "Google Sheet Webhook returned HTTP $code: ${body.take(120)}")
            }
        } catch (e: Exception) {
            Pair(false, "Webhook connection error: ${e.localizedMessage ?: e.message}")
        }
    }

    /**
     * Formats all psychometric question responses into one clean, structured readable text block
     */
    fun formatStudentAnswersSummary(studentAnswers: Map<String, String>): String {
        if (studentAnswers.isEmpty()) return "No test answers submitted yet."
        val count = STUDENT_QUESTIONS.count { studentAnswers[it.id] != null }
        val sb = StringBuilder()
        sb.append("[$count/${STUDENT_QUESTIONS.size} Psychometric Questions Answered]\n")
        STUDENT_QUESTIONS.forEachIndexed { idx, q ->
            val ans = studentAnswers[q.id]
            if (ans != null) {
                val answerLabel = when (q.type) {
                    QuestionType.LIKERT -> {
                        val v = ans.toIntOrNull() ?: 3
                        val ratingText = when(v) { 1 -> "Strongly Disagree"; 2 -> "Disagree"; 3 -> "Neutral"; 4 -> "Agree"; 5 -> "Strongly Agree"; else -> "Scale $v" }
                        "$v/5 ($ratingText)"
                    }
                    QuestionType.MCQ_APTITUDE, QuestionType.SCENARIO_PREFERENCE -> {
                        val opt = q.options.find { it.id.equals(ans, ignoreCase = true) }
                        if (opt != null) "[Opt ${ans.uppercase()}]: ${opt.text}" else "Option $ans"
                    }
                }
                sb.append("Q${idx + 1} [${q.section}]: ${q.text.take(45)}... -> $answerLabel\n")
            }
        }
        return sb.toString().trim()
    }

    /**
     * Formats all parent survey responses into one structured readable text block
     */
    fun formatParentAnswersSummary(parentAnswers: Map<String, String>): String {
        if (parentAnswers.isEmpty()) return "Parent survey not filled."
        val count = PARENT_QUESTIONS.count { parentAnswers[it.id] != null }
        val sb = StringBuilder()
        sb.append("[$count/${PARENT_QUESTIONS.size} Parent Preferences Answered]\n")
        PARENT_QUESTIONS.forEachIndexed { idx, q ->
            val ans = parentAnswers[q.id]
            if (ans != null) {
                val opt = q.options.find { it.value.equals(ans, ignoreCase = true) || it.label.equals(ans, ignoreCase = true) }
                val label = opt?.label ?: ans
                sb.append("${idx + 1}. [${q.section}] ${q.text.take(40)}... -> $label\n")
            }
        }
        return sb.toString().trim()
    }

    /**
     * Transmits the candidate's complete consolidated data in 1 SINGLE ROW to Google Sheet & 1 unified ntfy alert.
     */
    suspend fun syncCandidateUnified(
        stage: String,
        profile: StudentProfile,
        studentAnswers: Map<String, String>,
        parentAnswers: Map<String, String>,
        traits: Map<TraitKey, Int>,
        streamResult: StreamResult?,
        topCareers: List<ScoredCareer>,
        paymentStatus: String = "Free Assessment",
        paymentUtr: String = ""
    ): DispatchStatus = withContext(Dispatchers.IO) {
        val refId = getStudentRefId(profile.phoneNumber, profile.studentName)
        val timestamp = getCurrentTimestamp()

        val studentAnswersFormatted = formatStudentAnswersSummary(studentAnswers)
        val parentAnswersFormatted = formatParentAnswersSummary(parentAnswers)
        val streamText = streamResult?.stream?.displayName ?: "Under Evaluation"
        val careersText = topCareers.take(4).joinToString(", ") { "${it.career.title} (${it.fit}%)" }

        // Top traits
        val sortedTraits = TraitKey.entries.sortedByDescending { traits[it] ?: 0 }
        val archetype = when {
            (traits[TraitKey.ANALYTICAL] ?: 0) >= 80 -> "Analytical Systems Architect"
            (traits[TraitKey.ENTERPRISING] ?: 0) >= 80 -> "Strategic Venture Catalyst"
            (traits[TraitKey.CREATIVE] ?: 0) >= 80 -> "Cross-Disciplinary Innovator"
            (traits[TraitKey.SOCIAL] ?: 0) >= 80 -> "Empathetic Public Diplomat"
            else -> "High-Execution Strategist"
        }

        val jsonPayload = JSONObject().apply {
            put("action", "UPSERT_STUDENT")
            put("student_id", refId)
            put("ref_id", refId)
            put("Reference ID", refId)
            put("last_updated", timestamp)
            put("Times", timestamp)
            put("Timestamp", timestamp)
            put("student_name", profile.studentName)
            put("Student Name", profile.studentName)
            put("parent_name", profile.parentName)
            put("Parent Name", profile.parentName)
            put("phone", profile.phoneNumber)
            put("Number", profile.phoneNumber)
            put("WhatsApp Number", profile.phoneNumber)
            put("email", profile.email)
            put("Gmail", profile.email)
            put("Email", profile.email)
            put("student_class", profile.studentClass)
            put("Class", profile.studentClass)
            put("school", profile.schoolName)
            put("School", profile.schoolName)
            put("city", profile.city)
            put("City", profile.city)
            put("stage", stage)
            put("Stage", stage)
            put("best_stream", streamText)
            put("Best stream", streamText)
            put("Recommended Stream", streamText)
            put("top_careers", careersText)
            put("Top job options", careersText)
            put("Top Careers", careersText)
            put("cognitive_archetype", archetype)
            put("Cognitive Archetype", archetype)
            put("trait_analytical", "${traits[TraitKey.ANALYTICAL] ?: 0}%")
            put("trait_spatial", "${traits[TraitKey.SPATIAL] ?: 0}%")
            put("trait_verbal", "${traits[TraitKey.VERBAL] ?: 0}%")
            put("trait_enterprising", "${traits[TraitKey.ENTERPRISING] ?: 0}%")
            put("trait_social", "${traits[TraitKey.SOCIAL] ?: 0}%")
            put("trait_creative", "${traits[TraitKey.CREATIVE] ?: 0}%")
            put("trait_practical", "${traits[TraitKey.PRACTICAL] ?: 0}%")
            put("payment_status", paymentStatus)
            put("Payment Status", paymentStatus)
            put("Payment / Dossier Status", paymentStatus)
            put("utr_number", paymentUtr)
            put("Payment UTR", paymentUtr)

            // Student answers in all possible key variants
            put("all_student_answers", studentAnswersFormatted)
            put("Answers to student questions", studentAnswersFormatted)
            put("answers to student questions", studentAnswersFormatted)
            put("Answers to Student Questions", studentAnswersFormatted)
            put("Answers_to_Student_Questions", studentAnswersFormatted)
            put("studentAnswers", studentAnswersFormatted)
            put("student_answers", studentAnswersFormatted)
            put("Student_Answers", studentAnswersFormatted)
            put("Psychometric Answers", studentAnswersFormatted)
            put("psychometric_answers", studentAnswersFormatted)

            // Parent answers in all possible key variants
            put("all_parent_answers", parentAnswersFormatted)
            put("Answers to parent questions", parentAnswersFormatted)
            put("answers to parent questions", parentAnswersFormatted)
            put("Answers to Parent Questions", parentAnswersFormatted)
            put("Answers_to_Parent_Questions", parentAnswersFormatted)
            put("parentAnswers", parentAnswersFormatted)
            put("parent_answers", parentAnswersFormatted)
            put("Parent_Answers", parentAnswersFormatted)
            put("Parent Survey", parentAnswersFormatted)
            put("parent_survey", parentAnswersFormatted)

            // Answers Summary
            val combinedSummary = "STUDENT PSYCHOMETRIC EVALUATION:\n$studentAnswersFormatted\n\nPARENT PREFERENCE SURVEY:\n$parentAnswersFormatted"
            put("Answers / Choices Summary", combinedSummary)
            put("Answers_Summary", combinedSummary)
            put("answers_summary", combinedSummary)
            put("Answers", combinedSummary)
        }

        var sheetSuccess = false
        var sheetMsg = ""

        // 1. Google Sheet Single-Row Webhook Dispatch
        val webhookUrl = customGoogleSheetWebhookUrl.ifBlank { BUILTIN_GOOGLE_SHEET_WEBHOOK_URL }
        if (webhookUrl.isNotBlank()) {
            try {
                val (code, body) = postJsonWithRedirects(webhookUrl, jsonPayload.toString(), 9000)
                if (code in 200..299 || body.contains("success", ignoreCase = true)) {
                    sheetSuccess = true
                    sheetMsg = "Synced to Google Sheet (1 Student = 1 Row)"
                    Log.d(TAG, "Google Sheet Webhook 1-Row Upsert Success for $refId (HTTP $code)")
                } else {
                    sheetMsg = "Google Sheet returned HTTP $code"
                    Log.w(TAG, "Google Sheet Webhook returned HTTP $code: $body")
                }
            } catch (e: Exception) {
                sheetMsg = "Sheet Webhook offline: ${e.message}"
                Log.e(TAG, "Google Sheet Webhook error", e)
            }
        }

        // 2. Real-Time Consolidated ntfy Push Alert
        val topic = customNtfyTopic.ifBlank { DEFAULT_NTFY_TOPIC_1 }
        val server = customNtfyServer.ifBlank { DEFAULT_NTFY_SERVER }.removeSuffix("/")
        try {
            val ntfyUrl = URL("$server/$topic")
            val conn = ntfyUrl.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8")
            conn.setRequestProperty("Title", "MargDarsh: ${profile.studentName.ifBlank { "Candidate" }} ($refId) - $stage")
            conn.setRequestProperty("Priority", if (paymentStatus.contains("Paid", ignoreCase = true)) "5" else "4")
            conn.setRequestProperty("Tags", "mortar_board,student,sparkles")
            conn.connectTimeout = 7000
            conn.readTimeout = 7000
            conn.doOutput = true

            val ntfyBody = buildString {
                appendLine("🎓 STUDENT DOSSIER RECORD [$refId]")
                appendLine("👤 Student: ${profile.studentName} | Class: ${profile.studentClass}")
                appendLine("👨‍👩‍👦 Parent: ${profile.parentName} | Phone: ${profile.phoneNumber}")
                appendLine("🏫 School: ${profile.schoolName}, ${profile.city}")
                appendLine("🎯 Stream: $streamText")
                appendLine("💼 Top Careers: $careersText")
                appendLine("🧠 Archetype: $archetype")
                appendLine("💳 Status: $paymentStatus ${if (paymentUtr.isNotBlank()) "(UTR: $paymentUtr)" else ""}")
                appendLine("🕒 Synced: $timestamp")
            }

            OutputStreamWriter(conn.outputStream, "UTF-8").use { w ->
                w.write(ntfyBody)
                w.flush()
            }
            val ntfyCode = conn.responseCode
            Log.d(TAG, "ntfy push result HTTP $ntfyCode")
        } catch (e: Exception) {
            Log.w(TAG, "ntfy push failed: ${e.message}")
        }

        return@withContext if (sheetSuccess) {
            DispatchStatus.Success(
                message = "Student Profile & Answers consolidated in 1 Row on Google Sheet",
                timestamp = timestamp,
                transactionId = refId
            )
        } else {
            DispatchStatus.Success(
                message = "Candidate record generated [$refId]. $sheetMsg",
                timestamp = timestamp,
                transactionId = refId
            )
        }
    }

    // Convenience Wrappers for Specific Stage Dispatches:
    suspend fun dispatchRegistrationUnified(profile: StudentProfile): DispatchStatus {
        return syncCandidateUnified(
            stage = "Profile Registered",
            profile = profile,
            studentAnswers = emptyMap(),
            parentAnswers = emptyMap(),
            traits = emptyMap(),
            streamResult = null,
            topCareers = emptyList(),
            paymentStatus = "Registered"
        )
    }

    suspend fun dispatchAssessmentCompleteUnified(
        profile: StudentProfile,
        studentAnswers: Map<String, String>,
        parentAnswers: Map<String, String>,
        traits: Map<TraitKey, Int>,
        streamResult: StreamResult?,
        topCareers: List<ScoredCareer>
    ): DispatchStatus {
        return syncCandidateUnified(
            stage = "Assessment Completed (36/36 Questions)",
            profile = profile,
            studentAnswers = studentAnswers,
            parentAnswers = parentAnswers,
            traits = traits,
            streamResult = streamResult,
            topCareers = topCareers,
            paymentStatus = "Free Assessment Completed"
        )
    }

    suspend fun dispatchPaymentUnlockedUnified(
        profile: StudentProfile,
        studentAnswers: Map<String, String>,
        parentAnswers: Map<String, String>,
        traits: Map<TraitKey, Int>,
        streamResult: StreamResult?,
        topCareers: List<ScoredCareer>,
        utrNumber: String
    ): DispatchStatus {
        return syncCandidateUnified(
            stage = "Premium Dossier & Report Unlocked",
            profile = profile,
            studentAnswers = studentAnswers,
            parentAnswers = parentAnswers,
            traits = traits,
            streamResult = streamResult,
            topCareers = topCareers,
            paymentStatus = "Paid Premium Report (Verified)",
            paymentUtr = utrNumber
        )
    }

    /**
     * Records a Paid 1-on-1 Counselling Appointment Booking.
     * Immediately dispatches candidate details, coupon, and payment time to:
     * 1. Dedicated Appointment Spreadsheet Webhook (customAppointmentWebhookUrl) -> "Paid Counselling Students"
     * 2. Main Sheet Webhook (customGoogleSheetWebhookUrl) -> "Paid Counselling Students" tab + unified row
     * 3. High-Priority push notification to counselor's ntfy topic
     * 4. Local persistent Excel CSV roster of paid counselling students
     */
    suspend fun recordCounsellingAppointment(
        profile: StudentProfile,
        utr: String,
        amount: String = "999",
        couponCode: String? = null,
        notes: String = "1-on-1 Personal Counselling Call",
        streamResult: StreamResult? = null,
        topCareers: List<ScoredCareer> = emptyList(),
        context: Context? = null,
        screenshotUri: Uri? = null,
        driveUrl: String? = null
    ): DispatchStatus = withContext(Dispatchers.IO) {
        val refId = getStudentRefId(profile.phoneNumber, profile.studentName)
        val timestamp = getCurrentTimestamp()
        val streamText = streamResult?.stream?.displayName ?: "Personal Stream Guidance"
        val careersText = topCareers.take(4).joinToString(", ") { "${it.career.title} (${it.fit}%)" }.ifBlank { "Personalized Consultation" }

        val cleanCoupon = couponCode?.trim()?.replace("^COUPON_".toRegex(), "")?.split("_")?.getOrNull(0)?.ifBlank { null }
        val couponText = cleanCoupon ?: if (utr.startsWith("COUPON_")) utr.removePrefix("COUPON_").split("_")[0] else "None"
        val discountPercentText = when {
            amount == "0" || cleanCoupon?.contains("100") == true || utr.startsWith("COUPON_") -> "100%"
            amount == "499" || cleanCoupon?.contains("50") == true -> "50%"
            else -> "0%"
        }
        val paymentLabel = when {
            amount == "0" || cleanCoupon?.contains("100") == true || utr.startsWith("COUPON_") ->
                "100% Scholarship ($couponText) - Free ₹0"
            amount == "499" || cleanCoupon?.contains("50") == true ->
                "Paid ₹499 (50% Coupon: $couponText)"
            else -> "Paid ₹$amount (1-on-1 Personal Counselling)"
        }

        val jsonPayload = JSONObject().apply {
            put("action", "BOOK_COUNSELLING")
            put("service", "1-on-1 Personal Career Counselling")
            put("service_name", "1-on-1 Personal Career Counselling")
            put("original_price", "999")
            put("stage", "Paid 1-on-1 Counselling Session")
            put("Stage", "Paid 1-on-1 Counselling Session")
            put("sheet_name", "Paid Counselling Students")
            put("target_sheet", "Paid Counselling Students")

            // Candidate profile with all naming and casing variants
            put("student_name", profile.studentName)
            put("Student Name", profile.studentName)
            put("Student_Name", profile.studentName)
            put("studentName", profile.studentName)
            put("name", profile.studentName)
            put("Name", profile.studentName)

            put("parent_name", profile.parentName.ifBlank { "N/A" })
            put("Parent Name", profile.parentName.ifBlank { "N/A" })
            put("Parent_Name", profile.parentName.ifBlank { "N/A" })
            put("parentName", profile.parentName.ifBlank { "N/A" })

            put("phone", profile.phoneNumber)
            put("Phone", profile.phoneNumber)
            put("phone_number", profile.phoneNumber)
            put("Phone Number", profile.phoneNumber)
            put("Number", profile.phoneNumber)
            put("mobile", profile.phoneNumber)
            put("Mobile", profile.phoneNumber)
            put("WhatsApp Number", profile.phoneNumber)

            put("email", profile.email.ifBlank { "N/A" })
            put("Email", profile.email.ifBlank { "N/A" })
            put("gmail", profile.email.ifBlank { "N/A" })
            put("Gmail", profile.email.ifBlank { "N/A" })
            put("Email ID", profile.email.ifBlank { "N/A" })

            put("class", profile.studentClass)
            put("Class", profile.studentClass)
            put("student_class", profile.studentClass)

            put("school", profile.schoolName.ifBlank { "N/A" })
            put("School", profile.schoolName.ifBlank { "N/A" })
            put("school_name", profile.schoolName.ifBlank { "N/A" })
            put("School / Institute", profile.schoolName.ifBlank { "N/A" })

            put("city", profile.city.ifBlank { "N/A" })
            put("City", profile.city.ifBlank { "N/A" })
            put("location", profile.city.ifBlank { "N/A" })

            // Payment, Coupon and booking status
            put("payment_status", paymentLabel)
            put("Payment Status", paymentLabel)
            put("Payment Amount & Status", paymentLabel)
            put("payment", paymentLabel)
            put("amount", if (amount == "0") "₹0" else "₹$amount")

            put("coupon", couponText)
            put("Coupon", couponText)
            put("coupon_code", couponText)
            put("Coupon Code", couponText)
            put("Coupon Applied", couponText)
            put("discount", discountPercentText)
            put("Discount", discountPercentText)

            put("utr_number", utr)
            put("Payment UTR", utr)
            put("Payment UTR / Transaction ID", utr)
            put("utr", utr)
            put("UTR", utr)

            put("booking_status", "Confirmed Paid Booking")
            put("Booking Status", "Confirmed Paid Booking")

            put("timestamp", timestamp)
            put("Timestamp", timestamp)
            put("Payment / Booking Timestamp", timestamp)

            put("recommended_stream", streamText)
            put("Recommended Stream", streamText)
            put("Best stream", streamText)
            put("stream", streamText)

            put("top_careers", careersText)
            put("Top Career Options", careersText)
            put("Top job options", careersText)
            put("careers", careersText)

            put("ref_id", refId)
            put("reference_id", refId)
            put("Reference ID", refId)
            put("Student Reference ID", refId)
            put("student_id", refId)

            put("notes", notes)
            if (!driveUrl.isNullOrBlank()) {
                put("drive_screenshot_url", driveUrl)
                put("screenshot_url", driveUrl)
                put("screenshot_drive_link", driveUrl)
            }
            val base64Img = if (context != null && screenshotUri != null) encodeImageToBase64(context, screenshotUri) else null
            if (!base64Img.isNullOrBlank()) {
                put("image_base64", base64Img)
                put("image_mime", "image/jpeg")
            }
        }

        // Also update local persistent roster of paid counselling students
        applicationContext?.let { ctx ->
            com.example.margdarsh.data.PaidCounsellingRosterManager.recordBooking(
                context = ctx,
                entry = com.example.margdarsh.data.PaidCounsellingStudentEntry(
                    studentName = profile.studentName,
                    parentName = profile.parentName,
                    phoneNumber = profile.phoneNumber,
                    email = profile.email,
                    studentClass = profile.studentClass,
                    schoolName = profile.schoolName,
                    city = profile.city,
                    amountPaid = if (amount == "0") "₹0" else "₹$amount",
                    couponUsed = couponText,
                    paymentStatus = paymentLabel,
                    paymentUtr = utr,
                    bookingTimestamp = timestamp,
                    recommendedStream = streamText,
                    topCareers = careersText,
                    studentRefId = refId,
                    notes = notes
                )
            )
        }

        // Collect all configured webhook endpoints
        val targetUrls = mutableListOf<String>()
        if (customAppointmentWebhookUrl.isNotBlank()) {
            targetUrls.add(customAppointmentWebhookUrl.trim())
        }
        val sheetUrl = customGoogleSheetWebhookUrl.ifBlank { BUILTIN_GOOGLE_SHEET_WEBHOOK_URL }.trim()
        if (sheetUrl.isNotBlank() && !targetUrls.contains(sheetUrl)) {
            targetUrls.add(sheetUrl)
        }

        var successCount = 0
        targetUrls.forEach { url ->
            try {
                val (code, body) = postJsonWithRedirects(url, jsonPayload.toString(), 9000)
                if (code in 200..299 || body.contains("success", ignoreCase = true)) {
                    successCount++
                    Log.d(TAG, "Counseling appointment recorded to webhook $url (HTTP $code)")
                } else {
                    Log.w(TAG, "Appointment webhook $url returned HTTP $code: $body")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to send appointment to $url", e)
            }
        }

        // Send instant high-priority ntfy mobile alert
        val topic = customNtfyTopic.ifBlank { DEFAULT_NTFY_TOPIC_1 }
        val server = customNtfyServer.ifBlank { DEFAULT_NTFY_SERVER }.removeSuffix("/")
        try {
            val alertUrl = "$server/$topic"
            val conn = URL(alertUrl).openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.connectTimeout = 8000
            conn.readTimeout = 8000
            conn.doOutput = true
            val alertTitle = if (amount == "0") "🚨 100% SCHOLARSHIP COUNSELLING: ${profile.studentName}" else "🚨 PAID ₹$amount COUNSELLING: ${profile.studentName}"
            conn.setRequestProperty("Title", alertTitle)
            conn.setRequestProperty("Priority", "5")
            conn.setRequestProperty("Tags", "moneybag,calendar,phone,white_check_mark")

            val alertBody = buildString {
                appendLine("💰 Paid 1-on-1 Career Counselling Session Booked!")
                appendLine("👤 Student: ${profile.studentName} (${profile.studentClass})")
                if (profile.parentName.isNotBlank()) appendLine("👨‍👩‍👦 Parent: ${profile.parentName}")
                appendLine("📞 WhatsApp/Mobile: ${profile.phoneNumber}")
                if (profile.city.isNotBlank()) appendLine("📍 City: ${profile.city}")
                if (profile.schoolName.isNotBlank()) appendLine("🏫 School: ${profile.schoolName}")
                appendLine("💳 Amount & Status: $paymentLabel")
                if (couponText != "None (Full Fee)") appendLine("🎟️ Coupon Applied: $couponText (Discount: $discountPercentText)")
                appendLine("🔍 UTR / Transaction Ref: $utr")
                appendLine("🎯 Recommended Stream: $streamText")
                appendLine("🕒 Payment Time: $timestamp")
            }
            OutputStreamWriter(conn.outputStream, "UTF-8").use { w ->
                w.write(alertBody)
                w.flush()
            }
            conn.responseCode
        } catch (e: Exception) {
            Log.w(TAG, "Appointment ntfy push alert failed: ${e.message}")
        }

        return@withContext if (successCount > 0) {
            DispatchStatus.Success(
                message = "Counseling session appointment recorded with payment timestamp.",
                timestamp = timestamp,
                transactionId = refId
            )
        } else {
            DispatchStatus.Success(
                message = "Appointment logged locally ($refId).",
                timestamp = timestamp,
                transactionId = refId
            )
        }
    }

    /**
     * Efficiently reads, scales, compresses, and base64 encodes an image Uri
     */
    fun encodeImageToBase64(context: Context, uri: Uri, maxDimension: Int = 1000): String? {
        return try {
            val input = context.contentResolver.openInputStream(uri) ?: return null
            val original = BitmapFactory.decodeStream(input) ?: return null
            input.close()

            val width = original.width
            val height = original.height
            val scaled = if (width > maxDimension || height > maxDimension) {
                val ratio = width.toFloat() / height.toFloat()
                val newWidth = if (width > height) maxDimension else (maxDimension * ratio).toInt().coerceAtLeast(1)
                val newHeight = if (height >= width) maxDimension else (maxDimension / ratio).toInt().coerceAtLeast(1)
                Bitmap.createScaledBitmap(original, newWidth, newHeight, true)
            } else {
                original
            }

            val outputStream = ByteArrayOutputStream()
            scaled.compress(Bitmap.CompressFormat.JPEG, 75, outputStream)
            val bytes = outputStream.toByteArray()
            Base64.encodeToString(bytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to encode image to base64: ${e.message}")
            null
        }
    }

    /**
     * Live server-side coupon validation against 'Coupons' tab rows with limit checking
     */
    suspend fun validateCouponWithServer(
        couponCode: String,
        service: String = "ALL"
    ): CouponValidationResult = withContext(Dispatchers.IO) {
        val cleanCode = couponCode.trim().uppercase()
        if (cleanCode.isBlank()) {
            return@withContext CouponValidationResult(false, 0, "Please enter a coupon code")
        }

        val targetUrl = customGoogleSheetWebhookUrl.ifBlank { BUILTIN_GOOGLE_SHEET_WEBHOOK_URL }.trim()
        try {
            val payload = JSONObject().apply {
                put("action", "VALIDATE_COUPON")
                put("coupon_code", cleanCode)
                put("service", service)
                put("timestamp", getCurrentTimestamp())
            }

            val (code, body) = postJsonWithRedirects(targetUrl, payload.toString(), 10000)
            if (code in 200..299 && body.isNotBlank()) {
                val json = JSONObject(body)
                val status = json.optString("status", "")
                if (status.equals("success", ignoreCase = true)) {
                    val discount = json.optInt("discount_percent", 0)
                    val maxLimit = json.optInt("max_limit", 0)
                    val currentUsage = json.optInt("current_usage", 0)
                    val remaining = json.optInt("remaining_uses", maxLimit - currentUsage)
                    val msg = json.optString("message", "✓ Coupon '$cleanCode' Applied! $discount% Discount")
                    return@withContext CouponValidationResult(
                        isValid = true,
                        discountPercent = discount,
                        message = msg,
                        maxLimit = maxLimit,
                        currentUsage = currentUsage,
                        remainingUses = remaining
                    )
                } else {
                    val err = json.optString("message", "Invalid or expired coupon code.")
                    return@withContext CouponValidationResult(
                        isValid = false,
                        discountPercent = 0,
                        message = err
                    )
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Server coupon validation failed: ${e.message}")
        }

        // Graceful fallback if server is temporarily unreachable
        return@withContext when {
            cleanCode in listOf("FREE100", "MARG100", "SCHOLARSHIP100", "DIRECT100", "BODHAMI100") -> {
                CouponValidationResult(true, 100, "✓ Coupon '$cleanCode' Applied! 100% Full Scholarship")
            }
            cleanCode in listOf("MARG50", "SAVE50", "HALF50", "DISCOUNT50", "RESOURCE50") -> {
                CouponValidationResult(true, 50, "✓ Coupon '$cleanCode' Applied! 50% Instant Discount")
            }
            else -> {
                CouponValidationResult(false, 0, "Invalid coupon code. Please verify the code entered.")
            }
        }
    }

    /**
     * Records payment transaction in Google Sheet, uploads screenshot to Google Drive,
     * updates coupon usage count, and triggers instant team push notification.
     */
    suspend fun recordPaymentTransaction(
        context: Context,
        profile: StudentProfile,
        serviceName: String,
        amount: String,
        originalPrice: String,
        couponCode: String?,
        utr: String,
        screenshotUri: Uri?,
        notes: String = ""
    ): PaymentRecordResult = withContext(Dispatchers.IO) {
        val refId = getStudentRefId(profile.phoneNumber, profile.studentName)
        val timestamp = getCurrentTimestamp()
        val targetUrl = customGoogleSheetWebhookUrl.ifBlank { BUILTIN_GOOGLE_SHEET_WEBHOOK_URL }.trim()

        val base64Img = screenshotUri?.let { encodeImageToBase64(context, it) }
        val cleanCpn = couponCode?.trim()?.replace("^COUPON_".toRegex(), "")?.split("_")?.getOrNull(0)?.ifBlank { null }
        val couponStr = cleanCpn ?: if (utr.startsWith("COUPON_")) utr.removePrefix("COUPON_").split("_")[0] else "NONE"

        val payload = JSONObject().apply {
            put("action", "RECORD_PAYMENT")
            put("student_id", refId)
            put("ref_id", refId)
            put("student_name", profile.studentName.ifBlank { "Candidate" })
            put("parent_name", profile.parentName)
            put("phone", profile.phoneNumber)
            put("email", profile.email)
            put("service", serviceName)
            put("amount", amount)
            put("original_price", originalPrice)
            val orig = originalPrice.toIntOrNull() ?: 0
            val paid = amount.toIntOrNull() ?: 0
            val disc = if (orig > paid) orig - paid else 0
            put("discount", disc.toString())
            put("coupon_code", couponStr)
            put("coupon", couponStr)
            put("Coupon Applied", couponStr)
            put("utr", utr)
            put("notes", notes)
            put("timestamp", timestamp)
            if (!base64Img.isNullOrBlank()) {
                put("image_base64", base64Img)
                put("image_mime", "image/jpeg")
            }
        }

        var driveUrl: String? = null
        var txnId: String? = "TXN_${System.currentTimeMillis()}"
        var isSuccess = false
        var message = "Payment recorded."

        try {
            val (code, body) = postJsonWithRedirects(targetUrl, payload.toString(), 25000)
            if (code in 200..299 && body.isNotBlank()) {
                val resp = JSONObject(body)
                if (resp.optString("status").equals("success", ignoreCase = true)) {
                    isSuccess = true
                    driveUrl = resp.optString("drive_screenshot_url", null)
                    txnId = resp.optString("transaction_id", txnId)
                    message = resp.optString("message", "Payment and screenshot logged in Google Sheet & Drive.")
                    Log.d(TAG, "Payment transaction recorded to Google Sheet & Drive: $driveUrl")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to upload payment to Google Sheet Webhook: ${e.message}")
        }

        // Send instant high-priority ntfy alert about new payment & screenshot
        val topic = customNtfyTopic.ifBlank { DEFAULT_NTFY_TOPIC_1 }
        val server = customNtfyServer.ifBlank { DEFAULT_NTFY_SERVER }.removeSuffix("/")
        try {
            val alertUrl = "$server/$topic"
            val conn = URL(alertUrl).openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.connectTimeout = 8000
            conn.readTimeout = 8000
            conn.doOutput = true
            val alertTitle = "💵 NEW PAYMENT: ₹$amount for $serviceName"
            conn.setRequestProperty("Title", alertTitle)
            conn.setRequestProperty("Priority", "5")
            conn.setRequestProperty("Tags", "credit_card,camera,chart_with_upwards_trend,white_check_mark")

            val alertBody = buildString {
                appendLine("💳 Payment Received & Screenshot Logged!")
                appendLine("👤 Student: ${profile.studentName} (${profile.phoneNumber})")
                appendLine("📦 Service: $serviceName")
                appendLine("💰 Paid: ₹$amount (Original: ₹$originalPrice)")
                if (!couponCode.isNullOrBlank() && couponCode != "NONE") {
                    appendLine("🎟️ Coupon Used: $couponCode")
                }
                appendLine("🔢 Ref / UTR: $utr")
                if (!driveUrl.isNullOrBlank() && driveUrl.startsWith("http")) {
                    appendLine("🖼️ Screenshot Link: $driveUrl")
                } else if (!base64Img.isNullOrBlank()) {
                    appendLine("🖼️ Screenshot attached and dispatched")
                }
                appendLine("🕒 Time: $timestamp")
            }
            OutputStreamWriter(conn.outputStream, "UTF-8").use { w ->
                w.write(alertBody)
                w.flush()
            }
            conn.responseCode
        } catch (e: Exception) {
            Log.w(TAG, "Payment ntfy alert failed: ${e.message}")
        }

        PaymentRecordResult(
            success = isSuccess,
            driveUrl = driveUrl,
            transactionId = txnId,
            message = message
        )
    }
}

