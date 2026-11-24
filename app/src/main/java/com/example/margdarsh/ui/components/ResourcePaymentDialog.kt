package com.example.margdarsh.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.margdarsh.ui.theme.AmberBg
import com.example.margdarsh.ui.theme.AmberGold
import com.example.margdarsh.ui.theme.ForestGreen
import com.example.margdarsh.ui.theme.InkTextPrimary
import com.example.margdarsh.ui.theme.MargNavy
import com.example.margdarsh.ui.theme.MargNavyDark
import com.example.margdarsh.ui.theme.PaperBackground
import com.example.margdarsh.ui.theme.PaperSurface
import com.example.margdarsh.ui.theme.PaperSurfaceWarm
import com.example.margdarsh.ui.theme.RoyalBlue
import com.example.margdarsh.util.PaymentService

@Composable
fun ResourcePaymentDialog(
    initialUpiId: String = PaymentService.UPI_ID,
    resourceTitle: String = "All Strategic Career Handbooks",
    amountInr: String = "18.00",
    onDismiss: () -> Unit,
    onPaymentSuccess: (utr: String, upiIdUsed: String) -> Unit,
    onPaymentSuccessWithScreenshot: (utr: String, upiIdUsed: String, screenshotUri: Uri?) -> Unit = { utr, upi, _ -> onPaymentSuccess(utr, upi) },
    onUpdateUpiId: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var selectedProvider by remember { mutableStateOf(PaymentService.SUPPORTED_PROVIDERS.first()) }
    var customUtrText by remember { mutableStateOf("") }
    var utrError by remember { mutableStateOf(false) }
    var showQrCode by remember { mutableStateOf(false) }
    var attachedScreenshotUri by remember { mutableStateOf<Uri?>(null) }
    var showManualUtrField by remember { mutableStateOf(false) }

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

    val cleanAmount = amountInr.substringBefore(".").ifBlank { "18" }

    val qrBitmap = remember(cleanAmount, selectedProvider) {
        PaymentService.generateQrBitmap(
            amount = cleanAmount,
            note = "MargDarsh Handbooks",
            upiId = selectedProvider.vpa,
            payeeName = PaymentService.PAYEE_NAME,
            sizePx = 400
        )
    }

    val infiniteTransition = rememberInfiniteTransition(label = "resource_autopay_shimmer")
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
            Color(0xFF10375C),
            Color(0xFF1D5D9B),
            Color(0xFF75C2F6),
            Color(0xFFF4D160),
            Color(0xFF1D5D9B),
            Color(0xFF10375C)
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
                .padding(vertical = 16.dp)
                .testTag("resource_payment_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.5.dp, RoyalBlue.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Header Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MargNavy)
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
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = MargNavyDark, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "OFFICIAL MERCHANT UPI VERIFIED",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MargNavyDark,
                                        fontWeight = FontWeight.ExtraBold
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

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Unlock Career Handbooks & Blueprints",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Instant digital download of all career stream guides (PCM, PCB, Commerce, Arts, Neutral, Vocational).",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Pricing Summary Card
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = PaperSurfaceWarm,
                        border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = resourceTitle,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MargNavy
                                )
                                Text(
                                    text = "All 6 Official Stream Handbooks (PDFs)",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ForestGreen.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "₹$cleanAmount",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ForestGreen,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

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

                    // ONE-TAP DIRECT UPI APP LAUNCHERS
                    Text(
                        text = "One-Tap Launch with ₹$cleanAmount (Auto-Copies UPI ID):",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MargNavy
                    )

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
                                    amount = cleanAmount,
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
                                    amount = cleanAmount,
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
                                    amount = cleanAmount,
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

                    // PRIMARY ACTION BUTTON: Shining AutoPay via UPI Intent
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .shadow(6.dp, CircleShape)
                            .clip(CircleShape)
                            .background(shiningGradient)
                            .clickable {
                                val launched = PaymentService.triggerPayment(
                                    context = context,
                                    amount = cleanAmount,
                                    note = "MargDarsh Handbooks",
                                    upiId = selectedProvider.vpa,
                                    payeeName = PaymentService.PAYEE_NAME
                                )
                                if (!launched) {
                                    PaymentService.copyUpiIdToClipboard(context, targetVpa = selectedProvider.vpa, showToast = true)
                                }
                            }
                            .testTag("pay_rs1_upi_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color.White, modifier = Modifier.size(19.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "⚡ Instant AutoPay ₹$cleanAmount via Any UPI App",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Toggle QR Code Display
                    OutlinedButton(
                        onClick = { showQrCode = !showQrCode },
                        shape = CircleShape,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.QrCode2, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (showQrCode) "Hide UPI QR Code" else "Show ₹$cleanAmount QR Code to Scan",
                            color = RoyalBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    if (showQrCode && qrBitmap != null) {
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
                                    contentDescription = "UPI QR Code for ₹$cleanAmount",
                                    modifier = Modifier
                                        .size(170.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Scan with Any App to Pay ₹$cleanAmount directly to ${selectedProvider.vpa}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // ATTACH PAYMENT SCREENSHOT (Primary Verification)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = PaperSurface,
                        border = BorderStroke(1.2.dp, if (attachedScreenshotUri != null) ForestGreen else RoyalBlue.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AddPhotoAlternate,
                                        contentDescription = null,
                                        tint = if (attachedScreenshotUri != null) ForestGreen else RoyalBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Attach Payment Screenshot",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = MargNavyDark
                                    )
                                }
                                if (attachedScreenshotUri != null) {
                                    Surface(shape = CircleShape, color = ForestGreen.copy(alpha = 0.15f)) {
                                        Text(
                                            text = "✓ ATTACHED",
                                            color = ForestGreen,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            if (attachedScreenshotUri != null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White, RoundedCornerShape(10.dp))
                                        .padding(8.dp),
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
                                                .background(RoyalBlue.copy(alpha = 0.1f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Default.Image, contentDescription = null, tint = RoyalBlue)
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Receipt Screenshot Ready",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = MargNavyDark
                                        )
                                        Text(
                                            text = "Verified for instant handbook download",
                                            fontSize = 10.5.sp,
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
                                        .testTag("attach_screenshot_btn")
                                ) {
                                    Icon(Icons.Default.CloudUpload, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Select Screenshot from Gallery / Photos", color = RoyalBlue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                Text(
                                    text = "After paying ₹$cleanAmount via UPI, please attach the screenshot of your transaction receipt here.",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 4.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // SUBMIT SCREENSHOT & DOWNLOAD BUTTON
                    Button(
                        onClick = {
                            if (attachedScreenshotUri != null) {
                                val finalRef = "SCREENSHOT_${System.currentTimeMillis().toString().takeLast(6)}"
                                Toast.makeText(context, "✓ Screenshot Received! Unlocking Handbooks...", Toast.LENGTH_SHORT).show()
                                onPaymentSuccessWithScreenshot(finalRef, selectedProvider.vpa, attachedScreenshotUri)
                                onPaymentSuccess(finalRef, selectedProvider.vpa)
                            } else {
                                Toast.makeText(context, "Please attach your payment screenshot first.", Toast.LENGTH_SHORT).show()
                                screenshotPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForestGreen,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("auto_verify_unlock_btn")
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Submit Screenshot & Instant Unlock", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 13.sp)
                    }

                    // Helper note explaining Merchant UPI
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ForestGreen.copy(alpha = 0.12f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Verified Merchant Gateway: In-app auto payment is fully enabled for Google Pay, PhonePe, Paytm & BHIM.",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 10.sp,
                                color = MargNavyDark,
                                lineHeight = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
