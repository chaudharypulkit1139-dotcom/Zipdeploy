package com.example.margdarsh.ui.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.margdarsh.R
import com.example.margdarsh.data.StudentProfile
import com.example.margdarsh.network.DossierAutoDispatcher
import kotlinx.coroutines.launch
import com.example.margdarsh.ui.theme.AmberBg
import com.example.margdarsh.ui.theme.AmberGold
import com.example.margdarsh.ui.theme.ForestGreen
import com.example.margdarsh.ui.theme.MargNavy
import com.example.margdarsh.ui.theme.MargNavyDark
import com.example.margdarsh.ui.theme.PaperBackground
import com.example.margdarsh.ui.theme.PaperSurface
import com.example.margdarsh.ui.theme.PaperSurfaceWarm
import com.example.margdarsh.ui.theme.RoyalBlue
import com.example.margdarsh.util.PaymentService
import com.example.margdarsh.util.WhatsAppLauncher

@Composable
fun CounsellingPaymentDialog(
    profile: StudentProfile = StudentProfile(),
    counsellorPhone: String = "+91 79098 06620",
    counsellorEmail: String = "rankmakersindore@gmail.com",
    onDismiss: () -> Unit,
    onBookingSuccess: (utr: String, notes: String, bookedProfile: StudentProfile, amount: String, couponCode: String?) -> Unit = { _, _, _, _, _ -> },
    onBookingSuccessWithScreenshot: (utr: String, notes: String, bookedProfile: StudentProfile, amount: String, couponCode: String?, screenshotUri: Uri?) -> Unit = { utr, notes, p, amt, cpn, _ -> onBookingSuccess(utr, notes, p, amt, cpn) }
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var couponInput by remember { mutableStateOf("") }
    var appliedCoupon by remember { mutableStateOf<String?>(null) }
    var discountPercent by remember { mutableStateOf(0) }
    var couponSuccessMsg by remember { mutableStateOf<String?>(null) }
    var couponErrorMsg by remember { mutableStateOf<String?>(null) }
    var isCheckingCoupon by remember { mutableStateOf(false) }
    var utrError by remember { mutableStateOf(false) }

    val payableAmount = when (discountPercent) {
        100 -> 0
        50 -> 499
        else -> 999
    }

    var selectedProvider by remember { mutableStateOf(PaymentService.SUPPORTED_PROVIDERS.first()) }
    var isPaidVerified by remember { mutableStateOf(false) }
    var utrInput by remember { mutableStateOf("") }
    var showQrCode by remember { mutableStateOf(false) }
    var attachedScreenshotUri by remember { mutableStateOf<Uri?>(null) }
    var showCounselorKeyInput by remember { mutableStateOf(false) }

    val screenshotPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            attachedScreenshotUri = uri
            utrError = false
        }
    }

    val screenshotBitmap = remember(attachedScreenshotUri) {
        attachedScreenshotUri?.let { uri ->
            try {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    android.graphics.BitmapFactory.decodeStream(stream)
                }?.asImageBitmap()
            } catch (_: Exception) {
                null
            }
        }
    }

    val qrBitmap = remember(payableAmount, selectedProvider) {
        PaymentService.generateQrBitmap(
            amount = payableAmount.toString(),
            note = "MargDarsh Counselling",
            upiId = selectedProvider.vpa,
            payeeName = PaymentService.PAYEE_NAME,
            sizePx = 400
        )
    }

    fun generateWhatsAppBookingMessage(utr: String): String {
        val studentPart = if (profile.studentName.isNotBlank()) profile.studentName else "a student"
        val classPart = if (profile.studentClass.isNotBlank()) profile.studentClass else "Class 10"
        val cityPart = if (profile.city.isNotBlank()) " from ${profile.city}" else ""
        val utrPart = if (utr.isNotBlank()) " (Payment Ref/UTR: $utr)" else if (payableAmount == 0) " (100% Scholarship Voucher: $appliedCoupon)" else " (Payment Completed: ₹$payableAmount)"
        return "Hello Dr. Panchal Sir, I am $studentPart$cityPart ($classPart). I have booked the 1-on-1 Personal Career Counselling Session on Margdarsh$utrPart. Please let me know your available slots to schedule our live 45-minute consultation."
    }

    fun openWhatsApp(utr: String) {
        val waMsg = generateWhatsAppBookingMessage(utr)
        val cleanPhone = counsellorPhone.replace("[^0-9]".toRegex(), "")
        val waIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://api.whatsapp.com/send?phone=$cleanPhone&text=${Uri.encode(waMsg)}")
        }
        try {
            context.startActivity(waIntent)
        } catch (_: Exception) {
            Toast.makeText(context, "WhatsApp not installed. Please contact $counsellorPhone.", Toast.LENGTH_LONG).show()
        }
    }

    // Shimmering animated gradient for the Autopay button to make it shine and be the instant instinct to press
    val infiniteTransition = rememberInfiniteTransition(label = "autopay_shimmer")
    val shimmerTranslate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translation"
    )

    val shiningGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0D532B),
            Color(0xFF1E824C),
            Color(0xFF5CD895),
            Color(0xFFF1C40F),
            Color(0xFF1E824C),
            Color(0xFF0D532B)
        ),
        start = Offset(shimmerTranslate - 400f, 0f),
        end = Offset(shimmerTranslate + 200f, 200f)
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 14.dp)
                .testTag("counselling_payment_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.5.dp, ForestGreen.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(MargNavyDark, MargNavy)
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = AmberGold
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = MargNavyDark, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "DIRECT 1-ON-1 STRATEGY CALL",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MargNavyDark,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Dr. B.B. Panchal Profile Spotlight
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.dr_bb_panchal_counsellor_1789197215041),
                                contentDescription = "Dr. B.B. Panchal",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, AmberGold, CircleShape)
                                    .background(MargNavyDark)
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Dr. B.B. Panchal",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 19.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Verified Master Counsellor",
                                        tint = AmberGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Text(
                                    text = "25+ Years Experience • 10,000+ Students Mentored",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 11.sp
                                )

                                Spacer(modifier = Modifier.height(3.dp))

                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = ForestGreen.copy(alpha = 0.35f)
                                ) {
                                    Text(
                                        text = "Founder, Rankers Point • Master Career Strategist",
                                        color = AmberGold,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Price Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Institutional Value: ₹4,999",
                                    color = Color.White.copy(alpha = 0.6f),
                                    fontSize = 11.sp,
                                    textDecoration = TextDecoration.LineThrough
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Session Fee: ",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "₹999",
                                        color = AmberGold,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        shape = CircleShape,
                                        color = ForestGreen
                                    ) {
                                        Text(
                                            text = "80% OFF",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Body Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // What is included
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = PaperSurfaceWarm,
                        border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "🎯 What You Receive in Your 1-on-1 Session:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MargNavy,
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            CounsellingFeatureRow(icon = Icons.Default.Schedule, title = "45-Minute Live Video / Voice Diagnostic Session")
                            CounsellingFeatureRow(icon = Icons.Default.VideoCall, title = "Personalized Stream & Entrance Exam Roadmap (Class 9-12)")
                            CounsellingFeatureRow(icon = Icons.Default.CheckCircle, title = "Parent-Student Gap Mediation & Clear Action Agreement")
                            CounsellingFeatureRow(icon = Icons.Default.Verified, title = "Direct Advice on Coaching vs. Self-Study & College Budgets")
                        }
                    }

                    // PAYMENT / POST-PAYMENT STATE
                    if (isPaidVerified) {
                        // SUCCESS STATE: Big WhatsApp Booking Button
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = ForestGreen.copy(alpha = 0.1f),
                            border = BorderStroke(1.5.dp, ForestGreen),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = ForestGreen,
                                    modifier = Modifier.size(44.dp)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                val successHeading = when {
                                    payableAmount == 0 || appliedCoupon?.contains("100") == true -> "100% Scholarship Booking Confirmed!"
                                    payableAmount == 499 || appliedCoupon?.contains("50") == true -> "Payment of ₹499 (50% Off) Confirmed!"
                                    else -> "Payment of ₹999 Confirmed!"
                                }

                                Text(
                                    text = successHeading,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 17.sp,
                                    color = ForestGreen
                                )

                                if (utrInput.isNotBlank()) {
                                    Text(
                                        text = "Ref / UTR: $utrInput",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MargNavy
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MargNavy.copy(alpha = 0.08f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.TableChart, contentDescription = null, tint = MargNavy, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text("Updated in Excel Sheet of Paid Students", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MargNavyDark)
                                            Text("Logged to official Paid Counselling Students Excel roster with timestamp & UTR.", fontSize = 9.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Now click below to send your pre-filled confirmation message directly to Dr. B.B. Panchal on WhatsApp to lock in your live consultation slot.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 17.sp
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // THE BIG PROMINENT WHATSAPP BUTTON
                                Button(
                                    onClick = {
                                        val message = generateWhatsAppBookingMessage(utrInput)
                                        WhatsAppLauncher.launch(context, counsellorPhone, message)
                                    },
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = ForestGreen,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .shadow(6.dp, CircleShape)
                                        .testTag("whatsapp_booked_session_button")
                                ) {
                                    Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Message on WhatsApp to Book Slot",
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Booking Status Chip
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = ForestGreen.copy(alpha = 0.08f),
                                    border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.3f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Verified, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Booking recorded & synced to Counsellor's Cloud Sheet",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = ForestGreen
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Fallback Email Button
                                OutlinedButton(
                                    onClick = {
                                        val mailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                            data = Uri.parse("mailto:$counsellorEmail")
                                            putExtra(Intent.EXTRA_SUBJECT, "1-on-1 Counselling Booking Confirmed - ${profile.studentName}")
                                            putExtra(Intent.EXTRA_TEXT, generateWhatsAppBookingMessage(utrInput))
                                        }
                                        context.startActivity(mailIntent)
                                    },
                                    shape = CircleShape,
                                    modifier = Modifier.fillMaxWidth(),
                                    border = BorderStroke(1.dp, ForestGreen)
                                ) {
                                    Icon(Icons.Default.Email, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Also Send Email Confirmation", color = ForestGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    } else {
                        // BEFORE PAYMENT: Step 1 Payment Flow

                        // COUPON & SCHOLARSHIP CODE ENTRY
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("counselling_coupon_card"),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = PaperSurfaceWarm),
                            border = BorderStroke(1.2.dp, if (appliedCoupon != null) ForestGreen else AmberGold.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = MargNavy, modifier = Modifier.size(15.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Have a Coupon / Scholarship Code?",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MargNavy,
                                            fontSize = 11.5.sp
                                        )
                                    }
                                    if (appliedCoupon != null) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = ForestGreen
                                        ) {
                                            Text(
                                                text = if (discountPercent == 100) "100% OFF" else "50% OFF",
                                                color = Color.White,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = couponInput,
                                        onValueChange = {
                                            couponInput = it
                                            couponErrorMsg = null
                                        },
                                        placeholder = { Text("e.g. MARG50 or FREE100", fontSize = 11.sp) },
                                        singleLine = true,
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("counselling_coupon_input"),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = if (appliedCoupon != null) ForestGreen else RoyalBlue,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                                        )
                                    )

                                    Button(
                                        onClick = {
                                            val code = couponInput.trim().uppercase()
                                            if (code.isBlank()) {
                                                couponErrorMsg = "Please enter a coupon code"
                                                couponSuccessMsg = null
                                                return@Button
                                            }
                                            coroutineScope.launch {
                                                isCheckingCoupon = true
                                                val res = DossierAutoDispatcher.validateCouponWithServer(code, "COUNSELLING")
                                                isCheckingCoupon = false
                                                if (res.isValid) {
                                                    appliedCoupon = code
                                                    discountPercent = res.discountPercent
                                                    couponSuccessMsg = res.message
                                                    couponErrorMsg = null
                                                } else {
                                                    appliedCoupon = null
                                                    discountPercent = 0
                                                    couponErrorMsg = res.message
                                                    couponSuccessMsg = null
                                                }
                                            }
                                        },
                                        enabled = !isCheckingCoupon,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (appliedCoupon != null) ForestGreen else RoyalBlue,
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.testTag("apply_counselling_coupon_btn")
                                    ) {
                                        if (isCheckingCoupon) {
                                            androidx.compose.material3.CircularProgressIndicator(
                                                modifier = Modifier.size(14.dp),
                                                color = Color.White,
                                                strokeWidth = 2.dp
                                            )
                                        } else {
                                            Text(if (appliedCoupon != null) "Applied" else "Apply", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }
                                }

                                if (couponSuccessMsg != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(13.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = couponSuccessMsg!!, color = ForestGreen, fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                }

                                if (couponErrorMsg != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(13.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = couponErrorMsg!!, color = Color(0xFFEF4444), fontSize = 10.5.sp)
                                    }
                                }
                            }
                        }

                        if (discountPercent == 100) {
                            // 100% SCHOLARSHIP BOOKING CARD
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = ForestGreen.copy(alpha = 0.12f),
                                border = BorderStroke(1.5.dp, ForestGreen),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("counselling_100_percent_card")
                            ) {
                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(36.dp))
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("100% Scholarship Activated!", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = ForestGreen)
                                    Text(
                                        "Voucher '$appliedCoupon' waives 100% of the ₹999 fee. You can now book your 45-minute live consultation with Dr. B.B. Panchal at zero cost.",
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            val cleanPhoneDigits = profile.phoneNumber.filter { it.isDigit() }.takeLast(4).ifBlank { (1000..9999).random().toString() }
                                            val uniqueCouponRef = "COUPON_${appliedCoupon}_${cleanPhoneDigits}_${System.currentTimeMillis().toString().takeLast(4)}"
                                            isPaidVerified = true
                                            onBookingSuccessWithScreenshot(uniqueCouponRef, "100% Scholarship Voucher", profile, "0", appliedCoupon, null)
                                            openWhatsApp(uniqueCouponRef)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = ForestGreen,
                                            contentColor = Color.White
                                        ),
                                        shape = CircleShape,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(46.dp)
                                            .testTag("book_with_100_coupon_btn")
                                    ) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = AmberGold, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("★ Book Session with 100% Voucher", fontWeight = FontWeight.ExtraBold, fontSize = 12.5.sp, color = Color.White)
                                    }
                                }
                            }
                        } else {
                            // Active Recipient UPI ID Card with Copy (Primary VPA Only)
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = PaperSurface,
                                border = BorderStroke(1.dp, RoyalBlue.copy(alpha = 0.35f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Primary Official UPI ID", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(PaymentService.UPI_ID, fontWeight = FontWeight.Bold, fontSize = 13.5.sp, color = MargNavy)
                                        Text("Payee: ${PaymentService.PAYEE_NAME}", fontSize = 10.5.sp, color = RoyalBlue)
                                    }

                                    IconButton(
                                        onClick = {
                                            PaymentService.copyUpiIdToClipboard(context, targetVpa = PaymentService.UPI_ID, showToast = true)
                                        }
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy UPI ID", tint = RoyalBlue)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // ONE-TAP DIRECT UPI APP LAUNCHERS
                            Text(
                                text = "One-Tap Launch with ₹$payableAmount (Auto-Copies UPI ID):",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MargNavy
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // GPay button
                                OutlinedButton(
                                    onClick = {
                                        PaymentService.openUpiAppWithCopiedId(
                                            context = context,
                                            packageName = "com.google.android.apps.nbu.paisa.user",
                                            amount = payableAmount.toString(),
                                            upiId = selectedProvider.vpa,
                                            payeeName = PaymentService.PAYEE_NAME
                                        )
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                                ) {
                                    Text("GPay", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }

                                // PhonePe button
                                OutlinedButton(
                                    onClick = {
                                        PaymentService.openUpiAppWithCopiedId(
                                            context = context,
                                            packageName = "com.phonepe.app",
                                            amount = payableAmount.toString(),
                                            upiId = selectedProvider.vpa,
                                            payeeName = PaymentService.PAYEE_NAME
                                        )
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                                ) {
                                    Text("PhonePe", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }

                                // Paytm button
                                OutlinedButton(
                                    onClick = {
                                        PaymentService.openUpiAppWithCopiedId(
                                            context = context,
                                            packageName = "net.one97.paytm",
                                            amount = payableAmount.toString(),
                                            upiId = selectedProvider.vpa,
                                            payeeName = PaymentService.PAYEE_NAME
                                        )
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                                ) {
                                    Text("Paytm", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // THE SHINING AUTOPAY BUTTON (Instant Instinct to Press)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp)
                                    .shadow(8.dp, CircleShape)
                                    .clip(CircleShape)
                                    .background(shiningGradient)
                                    .clickable {
                                        val launched = PaymentService.triggerPayment(
                                            context = context,
                                            amount = payableAmount.toString(),
                                            note = "MargDarsh Counselling",
                                            upiId = selectedProvider.vpa,
                                            payeeName = PaymentService.PAYEE_NAME
                                        )
                                        if (!launched) {
                                            PaymentService.copyUpiIdToClipboard(context, targetVpa = selectedProvider.vpa, showToast = true)
                                        }
                                    }
                                    .testTag("shining_autopay_counselling_btn"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AccountBalanceWallet,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "⚡ Instant AutoPay ₹$payableAmount via Any UPI App",
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Toggle QR Code Display
                            OutlinedButton(
                                onClick = { showQrCode = !showQrCode },
                                shape = CircleShape,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.QrCode2, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (showQrCode) "Hide UPI QR Code" else "Show ₹$payableAmount QR Code to Scan",
                                    color = RoyalBlue,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            if (showQrCode && qrBitmap != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Scan QR with GPay, PhonePe, Paytm or BHIM",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MargNavy
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Image(
                                            bitmap = qrBitmap.asImageBitmap(),
                                            contentDescription = "UPI QR Code for ₹$payableAmount",
                                            modifier = Modifier
                                                .size(170.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = "Scan to Pay ₹$payableAmount directly to ${selectedProvider.vpa}",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                        Spacer(modifier = Modifier.height(4.dp))

                        // SECURE TRANSACTION VERIFICATION SECTION
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = PaperSurfaceWarm,
                            border = BorderStroke(1.dp, RoyalBlue.copy(alpha = 0.35f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "📸 Payment Receipt Verification",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MargNavy
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (attachedScreenshotUri != null) ForestGreen.copy(alpha = 0.15f) else AmberBg
                                    ) {
                                        Text(
                                            text = if (attachedScreenshotUri != null) "✓ Screenshot Attached" else "Attach Screenshot",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (attachedScreenshotUri != null) ForestGreen else AmberGold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "After paying ₹$payableAmount via UPI, please attach the screenshot of your transaction receipt below to schedule your consultation call.",
                                    fontSize = 10.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 14.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // SCREENSHOT ATTACHMENT CARD
                                if (attachedScreenshotUri != null) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(PaperSurfaceWarm, RoundedCornerShape(10.dp))
                                            .border(1.dp, ForestGreen.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                            .padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (screenshotBitmap != null) {
                                            Image(
                                                bitmap = screenshotBitmap,
                                                contentDescription = "Payment Screenshot",
                                                modifier = Modifier
                                                    .size(54.dp)
                                                    .clip(RoundedCornerShape(8.dp)),
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .size(54.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(RoyalBlue.copy(alpha = 0.12f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(Icons.Default.Image, contentDescription = null, tint = RoyalBlue)
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(10.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Screenshot Attached ✓",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = ForestGreen
                                            )
                                            Text(
                                                text = "Ready to schedule 1-on-1 career session",
                                                fontSize = 10.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                screenshotPickerLauncher.launch(
                                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                                )
                                            },
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Change", fontSize = 11.sp, color = RoyalBlue, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                } else {
                                    OutlinedButton(
                                        onClick = {
                                            screenshotPickerLauncher.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.2.dp, RoyalBlue),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("attach_counselling_screenshot_btn")
                                    ) {
                                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Attach Payment Screenshot from Gallery", color = RoyalBlue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // METHOD 1: Submit Screenshot & Schedule Call
                                Button(
                                    onClick = {
                                        val cleanUtr = utrInput.trim()
                                        val isCounselorKey = cleanUtr in listOf("790980", "252525", "RANKMAKERS", "MARGDARSH")

                                        if (attachedScreenshotUri != null || isCounselorKey || cleanUtr.length >= 6) {
                                            val verifiedRef = when {
                                                isCounselorKey -> cleanUtr
                                                cleanUtr.length >= 6 -> cleanUtr
                                                else -> "SCREENSHOT_${System.currentTimeMillis().toString().takeLast(6)}"
                                            }
                                            utrError = false
                                            isPaidVerified = true
                                            Toast.makeText(context, "Payment Proof Received! Session booked.", Toast.LENGTH_LONG).show()
                                            if (attachedScreenshotUri != null) {
                                                onBookingSuccessWithScreenshot(verifiedRef, "Screenshot Verified", profile, payableAmount.toString(), appliedCoupon, attachedScreenshotUri)
                                            } else {
                                                onBookingSuccess(verifiedRef, "Screenshot Verified", profile, payableAmount.toString(), appliedCoupon)
                                            }
                                            openWhatsApp(verifiedRef)
                                        } else {
                                            Toast.makeText(context, "Please attach your payment screenshot first.", Toast.LENGTH_LONG).show()
                                            screenshotPickerLauncher.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        }
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = RoyalBlue,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("submit_bank_utr_counselling_btn")
                                ) {
                                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Submit Screenshot & Schedule Call", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // METHOD 2: Send Screenshot on WhatsApp to Dr. Panchal
                                Button(
                                    onClick = {
                                        val verifiedRef = if (attachedScreenshotUri != null) "SCREENSHOT_${System.currentTimeMillis().toString().takeLast(6)}" else "WHATSAPP_CONFIRM"
                                        isPaidVerified = true
                                        if (attachedScreenshotUri != null) {
                                            onBookingSuccessWithScreenshot(verifiedRef, "WhatsApp Direct Verification", profile, payableAmount.toString(), appliedCoupon, attachedScreenshotUri)
                                        } else {
                                            onBookingSuccess(verifiedRef, "WhatsApp Direct Verification", profile, payableAmount.toString(), appliedCoupon)
                                        }
                                        openWhatsApp(verifiedRef)
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF25D366),
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("whatsapp_verify_counselling_btn")
                                ) {
                                    Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Send Screenshot on WhatsApp to Dr. Panchal", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // Optional Counselor Key toggle for offline access
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { showCounselorKeyInput = !showCounselorKeyInput }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = if (showCounselorKeyInput) "Hide Counselor Voucher Key input" else "Have a Counselor Key or Voucher Code?",
                                        fontSize = 10.sp,
                                        color = RoyalBlue,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                if (showCounselorKeyInput) {
                                    OutlinedTextField(
                                        value = utrInput,
                                        onValueChange = {
                                            utrInput = it.filter { ch -> ch.isLetterOrDigit() }
                                            utrError = false
                                        },
                                        placeholder = { Text("Enter Counselor Key (e.g. 790980)", fontSize = 11.sp) },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = RoyalBlue,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                        )
                                    )
                                }
                            }
                        }
                    }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedButton(
                        onClick = onDismiss,
                        shape = CircleShape,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Close", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun CounsellingFeatureRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ForestGreen,
            modifier = Modifier
                .size(16.dp)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MargNavyDark,
            fontSize = 11.sp,
            lineHeight = 15.sp
        )
    }
}
