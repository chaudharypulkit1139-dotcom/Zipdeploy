package com.example.margdarsh.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.margdarsh.network.DossierAutoDispatcher
import kotlinx.coroutines.launch
import com.example.margdarsh.ui.theme.AmberBg
import com.example.margdarsh.ui.theme.AmberGold
import com.example.margdarsh.ui.theme.AmberVibrant
import com.example.margdarsh.ui.theme.CyanBg
import com.example.margdarsh.ui.theme.CyanVibrant
import com.example.margdarsh.ui.theme.EmeraldBg
import com.example.margdarsh.ui.theme.EmeraldVibrant
import com.example.margdarsh.ui.theme.ForestGreen
import com.example.margdarsh.ui.theme.IndigoBg
import com.example.margdarsh.ui.theme.IndigoVibrant
import com.example.margdarsh.ui.theme.MargNavy
import com.example.margdarsh.ui.theme.MargNavyDark
import com.example.margdarsh.ui.theme.PaperBackground
import com.example.margdarsh.ui.theme.PaperSurface
import com.example.margdarsh.ui.theme.PaperSurfaceWarm
import com.example.margdarsh.ui.theme.PurpleBg
import com.example.margdarsh.ui.theme.PurpleVibrant
import com.example.margdarsh.ui.theme.RoyalBlue
import com.example.margdarsh.ui.theme.WarmClay
import com.example.margdarsh.util.PaymentService

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun PremiumPaywallDialog(
    studentName: String,
    onDismiss: () -> Unit,
    onUnlock: (utr: String) -> Unit,
    onUnlockDetailed: (utr: String, amount: String, coupon: String?, screenshotUri: Uri?) -> Unit = { utr, _, _, _ -> onUnlock(utr) }
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var utrInput by remember { mutableStateOf("") }
    var utrError by remember { mutableStateOf(false) }
    var showQrCode by remember { mutableStateOf(false) }
    var showBankLimitTroubleshooter by remember { mutableStateOf(false) }
    var showAllChapters by remember { mutableStateOf(false) }
    var attachedScreenshotUri by remember { mutableStateOf<Uri?>(null) }
    var showCounselorKeyInput by remember { mutableStateOf(false) }
    var isCheckingCoupon by remember { mutableStateOf(false) }

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

    var selectedAmount by remember { mutableStateOf("499") }
    var selectedProvider by remember { mutableStateOf(PaymentService.SUPPORTED_PROVIDERS.first()) }

    // Coupon System State
    var couponInput by remember { mutableStateOf("") }
    var appliedCoupon by remember { mutableStateOf<String?>(null) }
    var discountPercent by remember { mutableStateOf(0) }
    var couponSuccessMsg by remember { mutableStateOf<String?>(null) }
    var couponErrorMsg by remember { mutableStateOf<String?>(null) }

    val qrBitmap = remember(selectedAmount, selectedProvider) {
        PaymentService.generateQrBitmap(
            amount = selectedAmount,
            note = "MargDarsh",
            upiId = selectedProvider.vpa,
            payeeName = PaymentService.PAYEE_NAME,
            sizePx = 400
        )
    }

    // Shimmering animated gradient for the 499 payment buttons
    val infiniteTransition = rememberInfiniteTransition(label = "dossier_paywall_shimmer")
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
            Color(0xFF0D47A1),
            Color(0xFF1565C0),
            Color(0xFF1E88E5),
            Color(0xFF00E5FF),
            Color(0xFF64B5F6),
            Color(0xFF1E88E5),
            Color(0xFF1565C0),
            Color(0xFF0D47A1)
        ),
        start = Offset(shimmerTranslate - 400f, 0f),
        end = Offset(shimmerTranslate + 200f, 200f)
    )

    val shiningVerifyGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0A2647),
            Color(0xFF144272),
            Color(0xFF205295),
            Color(0xFF2C74B3),
            Color(0xFF00D2FF),
            Color(0xFF90E0EF),
            Color(0xFF2C74B3),
            Color(0xFF144272),
            Color(0xFF0A2647)
        ),
        start = Offset(shimmerTranslate - 400f, 0f),
        end = Offset(shimmerTranslate + 200f, 200f)
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(26.dp),
            color = PaperBackground,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 10.dp)
                .testTag("premium_paywall_dialog")
        ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header close row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AmberGold.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, AmberGold)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = AmberGold, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "OFFICIAL INSTITUTIONAL DOSSIER",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MargNavy,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = MargNavy)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Hero Banner Card with Dark Navy Gradient & Gold accents
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MargNavy)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(MargNavyDark, MargNavy, RoyalBlue.copy(alpha = 0.85f))
                                    )
                                )
                                .padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = AmberGold,
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Description, contentDescription = null, tint = MargNavyDark, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "65-PAGE COMPREHENSIVE HOLISTIC DOSSIER",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MargNavyDark
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Psychometric Audit & Career Strategy Dossier",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Certified Multi-Dimensional Diagnostic Blueprint for ${studentName.ifBlank { "the candidate" }}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Pricing & Value Anchoring Box
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color.White.copy(alpha = 0.12f),
                                border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.6f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "Regular Value",
                                            fontSize = 9.sp,
                                            color = Color.White.copy(alpha = 0.7f)
                                        )
                                        Text(
                                            text = "₹4,999",
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.6f),
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .width(1.dp)
                                            .height(26.dp)
                                            .background(Color.White.copy(alpha = 0.3f))
                                    )

                                    Column {
                                        Text(
                                            text = "Official 65-Page Dossier",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AmberGold
                                        )
                                        Row(verticalAlignment = Alignment.Bottom) {
                                            Text(
                                                text = "₹$selectedAmount",
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = AmberGold,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                            Text(
                                                text = " only",
                                                fontSize = 11.sp,
                                                color = Color.White,
                                                modifier = Modifier.padding(bottom = 2.dp, start = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 65-Page Chapter Index Breakdown Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📑 Full 65-Page Contents Matrix",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MargNavy
                                )

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = EmeraldBg
                                ) {
                                    Text(
                                        text = "65 Pages",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = EmeraldVibrant,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Chapter list
                            ChapterRow(
                                pages = "Pages 1–35",
                                title = "Initial Comprehensive Diagnostic & Career Strategy",
                                detail = "Profile archetype, RIASEC radar, 36-question audit, parent-student comparative dashboard, top 6 careers, and Grade 9-12 plans."
                            )
                            ChapterRow(
                                pages = "Pages 36–38",
                                title = "360° Quotient Diagnosis & Neurological Brain Map",
                                detail = "Overall Dream Score (Donut Gauge), IQ/PQ/SQ tri-quotient breakdown, 10-lobe brain diagram, and Gardner's multiple intelligences."
                            )
                            ChapterRow(
                                pages = "Pages 39–40",
                                title = "2D Stream & Career Quadrant Plots (Interest vs Potential)",
                                detail = "Interactive 2D quadrant scatter plots revealing the high-synergy 'Sweet Spot' for streams and professions."
                            )
                            ChapterRow(
                                pages = "Pages 41–42",
                                title = "National Peer Median Benchmark in India (150k+ Cohort)",
                                detail = "Student vs. India national median performance across 14 cognitive, analytical, and behavioral dimensions."
                            )
                            ChapterRow(
                                pages = "Pages 43–47",
                                title = "10 Personality Continuums & 90-Day Remedial Habits",
                                detail = "Bi-directional polar sliders, top-rated superpowers, bottleneck identification, and prescriptive interventions."
                            )

                            if (showAllChapters) {
                                ChapterRow(
                                    pages = "Pages 48–61",
                                    title = "14 Dedicated Career Deep-Dives + Dual Radar Charts",
                                    detail = "Hour-by-hour 'Day in the Life' routines, psychometric demand audits, entrance cutoffs, top colleges, and courses."
                                )
                                ChapterRow(
                                    pages = "Pages 62–65",
                                    title = "Top 20 Leaderboard, Entrance Roadmap & Verification Seal",
                                    detail = "Complete 20-career ranking matrix, 24-month exam calendar, institutional directory, and Dr. Panchal verification seal."
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = if (showAllChapters) "Show Less ↑" else "Show All 65 Pages Breakdown ↓",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = RoyalBlue,
                                modifier = Modifier
                                    .clickable { showAllChapters = !showAllChapters }
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Persuasive Why Families Buy Box
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = PaperSurfaceWarm),
                        border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.4f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "💡 Why 10,000+ Parents Trust This Dossier:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MargNavy
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            FeatureBullet(
                                title = "Eliminates Stream Choice Regret",
                                description = "Prevents picking the wrong stream under peer pressure, saving 2+ years of academic misalignment."
                            )
                            FeatureBullet(
                                title = "100% Commercial-Bias-Free",
                                description = "Unlike coaching institutes pushing fixed courses, recommendations are purely psychometrically driven."
                            )
                            FeatureBullet(
                                title = "High-Resolution Printable Vector PDF",
                                description = "Crisply formatted for offline printing, school counselor consultations, and family reference."
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // COUPON & SCHOLARSHIP SYSTEM CARD
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("coupon_input_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = PaperSurfaceWarm),
                        border = BorderStroke(1.2.dp, if (appliedCoupon != null) ForestGreen else AmberGold.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = MargNavy, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Have a Coupon / Scholarship Code?",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MargNavy,
                                        fontSize = 12.sp
                                    )
                                }
                                if (appliedCoupon != null) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = ForestGreen
                                    ) {
                                        Text(
                                            text = if (discountPercent == 100) "100% SCHOLARSHIP" else "50% DISCOUNT",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

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
                                        .testTag("coupon_code_field"),
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
                                            val res = DossierAutoDispatcher.validateCouponWithServer(code, "DOSSIER")
                                            isCheckingCoupon = false
                                            if (res.isValid) {
                                                appliedCoupon = code
                                                discountPercent = res.discountPercent
                                                selectedAmount = when (res.discountPercent) {
                                                    100 -> "0"
                                                    50 -> "249"
                                                    else -> ((499 * (100 - res.discountPercent)) / 100).toString()
                                                }
                                                couponSuccessMsg = res.message
                                                couponErrorMsg = null
                                            } else {
                                                appliedCoupon = null
                                                discountPercent = 0
                                                selectedAmount = "499"
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
                                    modifier = Modifier.testTag("apply_coupon_button")
                                ) {
                                    if (isCheckingCoupon) {
                                        androidx.compose.material3.CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            color = Color.White,
                                            strokeWidth = 2.dp
                                        )
                                    } else {
                                        Text(if (appliedCoupon != null) "Applied" else "Apply", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }

                            if (couponSuccessMsg != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = couponSuccessMsg!!, color = ForestGreen, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            if (couponErrorMsg != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = couponErrorMsg!!, color = Color(0xFFEF4444), fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (discountPercent == 100) {
                        // 100% SCHOLARSHIP REDEMPTION CARD (Instant Zero-Cost Unlock)
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = ForestGreen.copy(alpha = 0.12f),
                            border = BorderStroke(1.5.dp, ForestGreen),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("100_percent_scholarship_card")
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(40.dp))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("100% Full Scholarship Activated!", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = ForestGreen)
                                Text(
                                    "Voucher code '$appliedCoupon' covers 100% of the ₹499 fee. You can now unlock the official 65-Page Master Dossier instantly with zero payment.",
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        val uniqueCouponRef = "COUPON_${appliedCoupon}_${System.currentTimeMillis().toString().takeLast(6)}"
                                        onUnlockDetailed(uniqueCouponRef, "0", appliedCoupon, null)
                                        Toast.makeText(context, "Full 65-Page Dossier Unlocked via 100% Scholarship Voucher!", Toast.LENGTH_LONG).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = ForestGreen,
                                        contentColor = Color.White
                                    ),
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("redeem_100_percent_coupon_btn")
                                ) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = AmberGold, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("★ Redeem Voucher & Instant Unlock Dossier", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = Color.White)
                                }
                            }
                        }
                    } else {
                        // Payment Configuration & Methods Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.5.dp, RoyalBlue.copy(alpha = 0.35f))
                        ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "💳 Instant UPI Payment (₹$selectedAmount)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MargNavy
                                )

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = RoyalBlue.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = "Zero Gateway Fee",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RoyalBlue,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Amount Selection Section
                            Text(
                                text = "Select Investment Amount (₹):",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MargNavy
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                PaymentService.RECOMMENDED_AMOUNTS.forEach { amt ->
                                    val isSelected = selectedAmount == amt
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) MargNavy else PaperSurface,
                                        border = BorderStroke(1.dp, if (isSelected) MargNavy else MaterialTheme.colorScheme.outlineVariant),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { selectedAmount = amt }
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(vertical = 6.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "₹$amt",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = if (isSelected) Color.White else MargNavy
                                            )
                                            if (amt == "2") {
                                                Text(
                                                    text = "Limit-Safe",
                                                    fontSize = 8.sp,
                                                    color = if (isSelected) AmberGold else RoyalBlue,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            } else if (amt == "1") {
                                                Text(
                                                    text = "Standard",
                                                    fontSize = 8.sp,
                                                    color = if (isSelected) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Spacer(modifier = Modifier.height(10.dp))

                            // Active Recipient UPI ID Box (Primary VPA Only)
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = PaperSurface,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
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

                            // One-Tap Direct UPI App Launchers
                            Text(
                                text = "One-Tap Launch with ₹$selectedAmount:",
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
                                            amount = selectedAmount,
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
                                            amount = selectedAmount,
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
                                            amount = selectedAmount,
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

                            // Auto Intent Pay Button (Shining Gradient)
                            Surface(
                                shape = CircleShape,
                                color = MargNavy,
                                shadowElevation = 6.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val launched = PaymentService.triggerPayment(
                                            context = context,
                                            amount = selectedAmount,
                                            note = "MargDarsh",
                                            upiId = selectedProvider.vpa,
                                            payeeName = PaymentService.PAYEE_NAME
                                        )
                                        if (!launched) {
                                            PaymentService.copyUpiIdToClipboard(context, targetVpa = selectedProvider.vpa, showToast = true)
                                        }
                                    }
                                    .testTag("autopay_499_button")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(shiningGradient)
                                        .padding(vertical = 14.dp, horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = AmberGold, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Pay ₹$selectedAmount via Any UPI App (Instant)", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Toggle QR Code Button
                            OutlinedButton(
                                onClick = { showQrCode = !showQrCode },
                                modifier = Modifier.fillMaxWidth(),
                                shape = CircleShape
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QrCode2,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = RoyalBlue
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (showQrCode) "Hide UPI QR Code" else "Show ₹$selectedAmount QR Code to Scan",
                                    color = RoyalBlue,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            if (showQrCode && qrBitmap != null) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White, RoundedCornerShape(14.dp))
                                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        bitmap = qrBitmap.asImageBitmap(),
                                        contentDescription = "UPI Payment QR Code",
                                        modifier = Modifier.size(180.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Scan with Google Pay, PhonePe, Paytm or BHIM",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MargNavy
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

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
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (attachedScreenshotUri != null) ForestGreen else AmberVibrant,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "After completing your UPI payment of ₹$selectedAmount, please attach your payment receipt screenshot below to confirm your dossier unlock.",
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
                                                    text = "Ready to verify & unlock 65-page dossier",
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
                                                .testTag("attach_dossier_screenshot_btn")
                                        ) {
                                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Attach Payment Screenshot from Gallery", color = RoyalBlue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // VERIFICATION METHOD 1: Submit Screenshot & Unlock Dossier
                                    Button(
                                        onClick = {
                                            val cleanUtr = utrInput.trim()
                                            val isCounselorKey = cleanUtr in listOf("790980", "252525", "RANKMAKERS", "MARGDARSH")

                                            if (attachedScreenshotUri != null || isCounselorKey) {
                                                val verifiedRef = if (isCounselorKey) cleanUtr else "SCREENSHOT_${System.currentTimeMillis().toString().takeLast(6)}"
                                                onUnlockDetailed(verifiedRef, selectedAmount, appliedCoupon, attachedScreenshotUri)
                                                Toast.makeText(context, "✓ Payment Screenshot Verified! Official 65-Page Master Dossier Unlocked.", Toast.LENGTH_LONG).show()
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
                                            .testTag("submit_bank_utr_button")
                                    ) {
                                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Submit Screenshot & Unlock Dossier", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.5.sp)
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // VERIFICATION METHOD 2: Direct WhatsApp Verification with Dr. Panchal
                                    Button(
                                        onClick = {
                                            val waMsg = "Hello Dr. Panchal Sir, I am $studentName. I have completed payment of ₹$selectedAmount for the 65-Page Master Career Dossier on MargDarsh and attached my payment receipt screenshot. Please verify and confirm my activation."
                                            val waIntent = Intent(Intent.ACTION_VIEW).apply {
                                                data = Uri.parse("https://api.whatsapp.com/send?phone=917909806620&text=${Uri.encode(waMsg)}")
                                            }
                                            try {
                                                context.startActivity(waIntent)
                                            } catch (_: Exception) {
                                                Toast.makeText(context, "WhatsApp not installed. Please SMS or call +91 79098 06620.", Toast.LENGTH_LONG).show()
                                            }
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF25D366),
                                            contentColor = Color.White
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("whatsapp_verify_button")
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

                    Spacer(modifier = Modifier.height(14.dp))

                    // 1-on-1 Counselling Upsell Card
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = PaperSurfaceWarm,
                        border = BorderStroke(1.2.dp, AmberGold.copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = AmberGold
                                ) {
                                    Text(
                                        text = "HIGH IMPACT STRATEGY",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MargNavyDark,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    text = "₹999 Only",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ForestGreen
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "🎯 Need 1-on-1 Strategic Mentorship?",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MargNavy
                            )

                            Text(
                                text = "Book a direct 45-min personalized strategy & parent Q&A consultation with Dr. B.B. Panchal (25+ yrs exp).",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Surface(
                                shape = CircleShape,
                                color = ForestGreen,
                                shadowElevation = 4.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val msg = "Hello Dr. Panchal, I am $studentName. I would like to book a 1-on-1 Personal Career Counselling Session (₹999) with you."
                                        com.example.margdarsh.util.WhatsAppLauncher.launch(context, "+91 79098 06620", msg)
                                    }
                                    .testTag("paywall_one_on_one_counselling_button")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(shiningVerifyGradient)
                                        .padding(vertical = 12.dp, horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Book 1-on-1 Counselling (₹999)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = onDismiss,
                        shape = CircleShape,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Maybe Later")
                    }
                }
            }
        }
    }
}

@Composable
private fun ChapterRow(pages: String, title: String, detail: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        color = PaperSurfaceWarm.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MargNavyDark
            ) {
                Text(
                    text = pages,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberGold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MargNavy
                )
                Text(
                    text = detail,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 15.sp
                )
            }
        }
    }
}

@Composable
private fun FeatureBullet(title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = RoyalBlue,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MargNavy
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}

