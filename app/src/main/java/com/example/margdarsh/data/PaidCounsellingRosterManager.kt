package com.example.margdarsh.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets

data class PaidCounsellingStudentEntry(
    val studentName: String = "",
    val parentName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val studentClass: String = "Class 10",
    val schoolName: String = "",
    val city: String = "",
    val amountPaid: String = "₹999",
    val couponUsed: String = "None (Full Fee)",
    val paymentStatus: String = "Paid ₹999",
    val paymentUtr: String = "",
    val bookingTimestamp: String = "",
    val bookingStatus: String = "Confirmed Paid Booking",
    val recommendedStream: String = "General Stream Guidance",
    val topCareers: String = "",
    val studentRefId: String = "",
    val notes: String = "1-on-1 Personal Counselling Call"
) {
    fun toJson(): JSONObject {
        return JSONObject().apply {
            put("studentName", studentName)
            put("parentName", parentName)
            put("phoneNumber", phoneNumber)
            put("email", email)
            put("studentClass", studentClass)
            put("schoolName", schoolName)
            put("city", city)
            put("amountPaid", amountPaid)
            put("couponUsed", couponUsed)
            put("paymentStatus", paymentStatus)
            put("paymentUtr", paymentUtr)
            put("bookingTimestamp", bookingTimestamp)
            put("bookingStatus", bookingStatus)
            put("recommendedStream", recommendedStream)
            put("topCareers", topCareers)
            put("studentRefId", studentRefId)
            put("notes", notes)
        }
    }

    companion object {
        fun fromJson(json: JSONObject): PaidCounsellingStudentEntry {
            return PaidCounsellingStudentEntry(
                studentName = json.optString("studentName", ""),
                parentName = json.optString("parentName", ""),
                phoneNumber = json.optString("phoneNumber", ""),
                email = json.optString("email", ""),
                studentClass = json.optString("studentClass", "Class 10"),
                schoolName = json.optString("schoolName", ""),
                city = json.optString("city", ""),
                amountPaid = json.optString("amountPaid", "₹999"),
                couponUsed = json.optString("couponUsed", "None"),
                paymentStatus = json.optString("paymentStatus", "Paid ₹999"),
                paymentUtr = json.optString("paymentUtr", ""),
                bookingTimestamp = json.optString("bookingTimestamp", ""),
                bookingStatus = json.optString("bookingStatus", "Confirmed Paid Booking"),
                recommendedStream = json.optString("recommendedStream", ""),
                topCareers = json.optString("topCareers", ""),
                studentRefId = json.optString("studentRefId", ""),
                notes = json.optString("notes", "")
            )
        }
    }
}

object PaidCounsellingRosterManager {
    private const val TAG = "PaidCounsellingRoster"
    private const val PREFS_NAME = "margdarsh_paid_counselling_roster_prefs"
    private const val KEY_ROSTER_JSON = "counselling_roster_json"
    private const val CSV_FILE_NAME = "Paid_Counselling_Students.csv"

    @Synchronized
    fun getAllBookings(context: Context): List<PaidCounsellingStudentEntry> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonStr = prefs.getString(KEY_ROSTER_JSON, "[]") ?: "[]"
        val list = mutableListOf<PaidCounsellingStudentEntry>()
        try {
            val arr = JSONArray(jsonStr)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(PaidCounsellingStudentEntry.fromJson(obj))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed parsing roster json", e)
        }
        return list
    }

    @Synchronized
    fun recordBooking(context: Context, entry: PaidCounsellingStudentEntry) {
        val existing = getAllBookings(context).toMutableList()
        val cleanPhone = entry.phoneNumber.filter { it.isDigit() }.takeLast(10)

        // Deduplicate or update existing entry
        val existingIndex = existing.indexOfFirst {
            val itemPhone = it.phoneNumber.filter { ch -> ch.isDigit() }.takeLast(10)
            (entry.studentRefId.isNotBlank() && it.studentRefId.equals(entry.studentRefId, ignoreCase = true)) ||
                    (cleanPhone.isNotBlank() && itemPhone == cleanPhone) ||
                    (entry.studentName.isNotBlank() && it.studentName.equals(entry.studentName, ignoreCase = true) && cleanPhone == itemPhone)
        }

        if (existingIndex >= 0) {
            existing[existingIndex] = entry
        } else {
            existing.add(0, entry) // Prepend newest to top of roster
        }

        // Persist to SharedPreferences
        val arr = JSONArray()
        existing.forEach { arr.put(it.toJson()) }
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_ROSTER_JSON, arr.toString())
            .apply()

        // Also write directly to CSV file
        saveCsvToFile(context, existing)
    }

    private fun escapeCsv(value: String): String {
        val safe = value.replace("\"", "\"\"")
        return "\"$safe\""
    }

    fun generateCsv(context: Context): String {
        val list = getAllBookings(context)
        val sb = StringBuilder()
        // UTF-8 BOM for Microsoft Excel compatibility
        sb.append("\uFEFF")

        // Headers matching Excel Sheet specification
        val headers = listOf(
            "Student Name",
            "Parent Name",
            "Phone Number",
            "Email ID",
            "Class / Grade",
            "School / Institute",
            "City",
            "Payment Status & Amount",
            "Coupon Applied",
            "Payment UTR / Transaction ID",
            "Booking Timestamp (IST)",
            "Booking Status",
            "Recommended Stream",
            "Top Career Options",
            "Student Reference ID",
            "Notes / Consultation Goal"
        )
        sb.append(headers.joinToString(",") { escapeCsv(it) }).append("\r\n")

        for (item in list) {
            val row = listOf(
                item.studentName.ifBlank { "Student" },
                item.parentName.ifBlank { "N/A" },
                item.phoneNumber,
                item.email.ifBlank { "N/A" },
                item.studentClass,
                item.schoolName.ifBlank { "N/A" },
                item.city.ifBlank { "N/A" },
                item.paymentStatus,
                item.couponUsed,
                item.paymentUtr,
                item.bookingTimestamp,
                item.bookingStatus,
                item.recommendedStream,
                item.topCareers,
                item.studentRefId,
                item.notes
            )
            sb.append(row.joinToString(",") { escapeCsv(it) }).append("\r\n")
        }

        return sb.toString()
    }

    fun generateTsvForExcelCopy(context: Context): String {
        val list = getAllBookings(context)
        val sb = StringBuilder()

        val headers = listOf(
            "Student Name",
            "Parent Name",
            "Phone Number",
            "Email ID",
            "Class",
            "School",
            "City",
            "Payment Status & Amount",
            "Coupon Applied",
            "UTR / Transaction ID",
            "Booking Timestamp",
            "Stream",
            "Top Careers",
            "Student Ref ID"
        )
        sb.append(headers.joinToString("\t")).append("\n")

        for (item in list) {
            val row = listOf(
                item.studentName.ifBlank { "Student" },
                item.parentName.ifBlank { "N/A" },
                item.phoneNumber,
                item.email.ifBlank { "N/A" },
                item.studentClass,
                item.schoolName.ifBlank { "N/A" },
                item.city.ifBlank { "N/A" },
                item.paymentStatus,
                item.couponUsed,
                item.paymentUtr,
                item.bookingTimestamp,
                item.recommendedStream,
                item.topCareers,
                item.studentRefId
            )
            sb.append(row.joinToString("\t")).append("\n")
        }
        return sb.toString()
    }

    private fun saveCsvToFile(context: Context, list: List<PaidCounsellingStudentEntry>): File? {
        return try {
            val file = File(context.filesDir, CSV_FILE_NAME)
            val csvContent = generateCsv(context)
            OutputStreamWriter(FileOutputStream(file), StandardCharsets.UTF_8).use { writer ->
                writer.write(csvContent)
                writer.flush()
            }
            file
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save CSV to file", e)
            null
        }
    }

    fun exportOrShareCsv(context: Context) {
        val list = getAllBookings(context)
        if (list.isEmpty()) {
            Toast.makeText(context, "No paid counselling bookings yet to export.", Toast.LENGTH_SHORT).show()
            return
        }

        val file = saveCsvToFile(context, list)
        if (file != null && file.exists()) {
            try {
                val authority = "${context.packageName}.fileprovider"
                val uri: Uri = FileProvider.getUriForFile(context, authority, file)

                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/comma-separated-values"
                    putExtra(Intent.EXTRA_SUBJECT, "Paid Counselling Students - Excel Sheet Export")
                    putExtra(Intent.EXTRA_TEXT, "Attached is the latest Excel Sheet roster of students who booked paid 1-on-1 counselling sessions (${list.size} students).")
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(sendIntent, "Export Paid Counselling Students Excel Sheet"))
            } catch (e: Exception) {
                Log.e(TAG, "Failed to share via FileProvider, sharing text directly", e)
                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "Paid Counselling Students - Excel Roster")
                    putExtra(Intent.EXTRA_TEXT, generateCsv(context))
                }
                context.startActivity(Intent.createChooser(sendIntent, "Share Paid Counselling Sheet"))
            }
        } else {
            Toast.makeText(context, "Failed to create Excel CSV file", Toast.LENGTH_SHORT).show()
        }
    }
}
