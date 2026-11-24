package com.example.margdarsh.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object WhatsAppLauncher {

    const val DEFAULT_COUNSELLOR_PHONE = "+91 79098 06620"
    const val DEFAULT_COUNSELLOR_CLEAN = "917909806620"

    /**
     * Normalizes an Indian or international phone number so it works reliably in WhatsApp URLs.
     * Guarantees NO duplicate country codes (e.g. 9191...).
     */
    fun normalizePhoneNumber(rawPhone: String): String {
        val digits = rawPhone.filter { it.isDigit() }
        return when {
            digits.length == 10 -> "91$digits"
            digits.length == 11 && digits.startsWith("0") -> "91${digits.substring(1)}"
            digits.length == 12 && digits.startsWith("91") -> digits
            digits.startsWith("0091") -> digits.substring(2)
            digits.length > 10 && digits.startsWith("91") -> digits
            digits.length > 10 -> digits
            digits.isNotBlank() -> "91$digits"
            else -> DEFAULT_COUNSELLOR_CLEAN
        }
    }

    /**
     * Launches WhatsApp to message the specified phone number with a prefilled message.
     * Fallbacks:
     * 1. Direct WhatsApp package intent or standard wa.me intent
     * 2. Browser view intent to wa.me URL
     * 3. Clipboard copy with informative toast and opening the phone dialer
     */
    fun launch(context: Context, rawPhone: String, message: String) {
        val cleanPhone = normalizePhoneNumber(rawPhone)
        val encodedMsg = Uri.encode(message)
        val waUrl = "https://wa.me/$cleanPhone?text=$encodedMsg"

        // Strategy 1: Direct WhatsApp application intent
        val directIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(waUrl)
            setPackage("com.whatsapp")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(directIntent)
            return
        } catch (_: Exception) {
            // WhatsApp app not installed or cannot handle direct intent; try Strategy 2
        }

        // Strategy 2: General Intent.ACTION_VIEW (can open browser or WhatsApp Business)
        val genericIntent = Intent(Intent.ACTION_VIEW, Uri.parse(waUrl)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(genericIntent)
            return
        } catch (_: Exception) {
            // Device has no browser or handler for wa.me links
        }

        // Strategy 3: Graceful fallback - copy number & message, and offer dialer
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            val displayPhone = if (cleanPhone.startsWith("91") && cleanPhone.length == 12) {
                "+91 ${cleanPhone.substring(2, 7)} ${cleanPhone.substring(7)}"
            } else {
                "+$cleanPhone"
            }
            val clip = ClipData.newPlainText("Counsellor Contact", "$displayPhone\n$message")
            clipboard?.setPrimaryClip(clip)

            Toast.makeText(
                context,
                "WhatsApp not installed. Counsellor phone ($displayPhone) copied to clipboard!",
                Toast.LENGTH_LONG
            ).show()

            // Open phone dialer as convenient fallback
            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$displayPhone")).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(dialIntent)
        } catch (_: Exception) {
            Toast.makeText(
                context,
                "Counsellor Contact: +91 79098 06620",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
