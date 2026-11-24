package com.example.margdarsh.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.margdarsh.R
import com.example.margdarsh.data.CareerTag
import com.example.margdarsh.data.DAY_IN_THE_LIFE_STORIES
import com.example.margdarsh.data.DetailedPsychometricAuditReport
import com.example.margdarsh.data.DetailedReportGenerator
import com.example.margdarsh.data.GrowthEdgeSkillItem
import com.example.margdarsh.data.PARENT_QUESTIONS
import com.example.margdarsh.data.ParentStudentDivergenceEvaluator
import com.example.margdarsh.data.QuestionPsychologicalAudit
import com.example.margdarsh.data.STUDENT_QUESTIONS
import com.example.margdarsh.data.ScoredCareer
import com.example.margdarsh.data.Stream
import com.example.margdarsh.data.StudentCoreSkillItem
import com.example.margdarsh.data.TraitKey
import com.example.margdarsh.network.DispatchStatus
import com.example.margdarsh.network.DossierAutoDispatcher
import com.example.margdarsh.ui.components.CounsellorContactDialog
import com.example.margdarsh.ui.components.PremiumPaywallDialog
import com.example.margdarsh.ui.components.ProfileSurveyDialog
import com.example.margdarsh.ui.components.sendEmailToCounsellor
import com.example.margdarsh.util.MargdarshReportPdfGenerator
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
import com.example.margdarsh.ui.theme.InkTextPrimary
import com.example.margdarsh.ui.theme.MargNavy
import com.example.margdarsh.ui.theme.MargNavyDark
import com.example.margdarsh.ui.theme.PaperBackground
import com.example.margdarsh.ui.theme.PaperSurface
import com.example.margdarsh.ui.theme.PaperSurfaceWarm
import com.example.margdarsh.ui.theme.ProgressTrack
import com.example.margdarsh.ui.theme.PurpleBg
import com.example.margdarsh.ui.theme.PurpleVibrant
import com.example.margdarsh.ui.theme.RoseBg
import com.example.margdarsh.ui.theme.RoseVibrant
import com.example.margdarsh.ui.theme.RoyalBlue
import com.example.margdarsh.ui.theme.WarmClay
import com.example.margdarsh.ui.theme.WarningAmber
import com.example.margdarsh.viewmodel.MargdarshUiState

@Composable
fun ResultsScreen(
    uiState: MargdarshUiState,
    detailedReport: DetailedPsychometricAuditReport,
    onNavigateToStudent: () -> Unit,
    onNavigateToParent: () -> Unit,
    onResetSession: () -> Unit,
    onOpenProfileDialog: () -> Unit,
    onOpenCounsellorDialog: () -> Unit,
    onCloseCounsellorDialog: () -> Unit,
    onSaveProfile: (com.example.margdarsh.data.StudentProfile) -> Unit,
    onCloseProfileDialog: () -> Unit,
    onUnlockPremium: (utr: String) -> Unit,
    onOpenPaywallDialog: () -> Unit,
    onClosePaywallDialog: () -> Unit,
    reportText: String,
    onSyncDossier: () -> Unit = {},
    onRecordCounsellingPayment: (utr: String, notes: String, profile: com.example.margdarsh.data.StudentProfile, amount: String, couponCode: String?) -> Unit = { _, _, _, _, _ -> },
    onRecordCounsellingPaymentWithScreenshot: (utr: String, notes: String, profile: com.example.margdarsh.data.StudentProfile, amount: String, couponCode: String?, screenshotUri: Uri?) -> Unit = { utr, notes, p, amt, cpn, _ -> onRecordCounsellingPayment(utr, notes, p, amt, cpn) },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showSummaryDialog by remember { mutableStateOf(false) }
    var showCounsellingPaymentDialog by remember { mutableStateOf(false) }
    val studentAnswered = uiState.studentAnswers.size
    val parentAnswered = uiState.parentAnswers.size
    val isReady = uiState.isReportReady && uiState.streamResult != null

    // Shimmering animated gradient for the ₹499 Dossier Unlock button to match other payment buttons
    val infiniteTransition = rememberInfiniteTransition(label = "dossier_shimmer")
    val shimmerTranslate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translation"
    )

    val shiningDossierGradient = Brush.linearGradient(
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

    val shiningCounsellingGradient = Brush.linearGradient(
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

    if (showCounsellingPaymentDialog) {
        com.example.margdarsh.ui.components.CounsellingPaymentDialog(
            profile = uiState.studentProfile,
            counsellorPhone = "+91 79098 06620",
            counsellorEmail = "rankmakersindore@gmail.com",
            onDismiss = { showCounsellingPaymentDialog = false },
            onBookingSuccess = { utr, notes, bookedProfile, amount, coupon ->
                onRecordCounsellingPayment(utr, notes, bookedProfile, amount, coupon)
            },
            onBookingSuccessWithScreenshot = { utr, notes, bookedProfile, amount, coupon, screenshotUri ->
                onRecordCounsellingPaymentWithScreenshot(utr, notes, bookedProfile, amount, coupon, screenshotUri)
            }
        )
    }

    if (!isReady) {
        // Incomplete assessment state
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(PaperBackground),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("report_not_ready_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MargNavy.copy(alpha = 0.1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MargNavy,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Assessment in Progress",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MargNavy
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Complete the 36-question psychometric evaluation and parent context to unlock the comprehensive dossier and career matching engine.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Progress bars
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ProgressRow(
                                label = "Student Psychometric Test",
                                progress = studentAnswered.toFloat() / STUDENT_QUESTIONS.size.toFloat(),
                                count = "$studentAnswered / ${STUDENT_QUESTIONS.size}"
                            )
                            ProgressRow(
                                label = "Parent Survey",
                                progress = parentAnswered.toFloat() / PARENT_QUESTIONS.size.toFloat(),
                                count = "$parentAnswered / ${PARENT_QUESTIONS.size}"
                            )
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (studentAnswered < STUDENT_QUESTIONS.size) {
                                Button(
                                    onClick = onNavigateToStudent,
                                    modifier = Modifier.fillMaxWidth().testTag("continue_student_button"),
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MargNavy,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text("Continue Student Assessment", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }

                            if (parentAnswered < PARENT_QUESTIONS.size) {
                                OutlinedButton(
                                    onClick = onNavigateToParent,
                                    modifier = Modifier.fillMaxWidth().testTag("continue_parent_button"),
                                    shape = CircleShape
                                ) {
                                    Text("Complete Parent Survey", color = MargNavy, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }
        }
        return
    }

    // Complete Report State
    val streamResult = uiState.streamResult!!
    val traits = uiState.traitScores
    val careers = uiState.recommendedCareers
    val profile = uiState.studentProfile
    val isReportUnlocked = uiState.isDetailedAuditUnlocked
    val topStream = streamResult.stream
    var selectedStreamFilter by remember(topStream) { mutableStateOf<Stream?>(topStream) }
    val filteredCareers = remember(careers, selectedStreamFilter) {
        if (selectedStreamFilter == null) {
            careers
        } else {
            careers.filter { it.career.stream == selectedStreamFilter }
        }
    }

    val traitOrder = listOf(
        TraitKey.ANALYTICAL,
        TraitKey.SPATIAL,
        TraitKey.VERBAL,
        TraitKey.ENTERPRISING,
        TraitKey.SOCIAL,
        TraitKey.CREATIVE,
        TraitKey.PRACTICAL
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PaperBackground),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Student Profile Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("student_profile_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(MargNavy),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = profile.studentName.ifBlank { "Candidate Profile" },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MargNavy
                                )
                                Text(
                                    text = "${profile.studentClass} • ${profile.schoolName.ifBlank { "School" }}, ${profile.city.ifBlank { "India" }}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = onOpenProfileDialog,
                            shape = CircleShape,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("edit_profile_button")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = MargNavy)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit", fontSize = 11.sp, color = MargNavy)
                        }
                    }

                    if (profile.parentName.isNotBlank() || profile.phoneNumber.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = PaperSurface
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                if (profile.parentName.isNotBlank()) {
                                    Text(
                                        text = "Parent: ${profile.parentName}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.sp
                                    )
                                }
                                if (profile.phoneNumber.isNotBlank()) {
                                    Text(
                                        text = "📞 ${profile.phoneNumber}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Stream Recommendation Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("stream_recommendation_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = PaperSurfaceWarm),
                border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
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
                                text = "RECOMMENDED STREAM",
                                style = MaterialTheme.typography.labelSmall,
                                color = MargNavyDark,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                downloadAndShareReport(context, profile.studentName, reportText)
                            },
                            modifier = Modifier.size(32.dp).testTag("share_report_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = MargNavy,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${streamResult.stream.displayName}: ${streamResult.stream.subtitle}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MargNavy,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = streamResult.rationale,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = onResetSession,
                        shape = CircleShape,
                        modifier = Modifier.testTag("start_over_button")
                    ) {
                        Text("Retake Assessment", color = MargNavy)
                    }
                }
            }
        }

        // Cognitive & Trait Profile Chart
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("trait_profile_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Text(
                        text = "COGNITIVE & TRAIT SCORES",
                        style = MaterialTheme.typography.labelMedium,
                        color = MargNavy,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    traitOrder.forEach { trait ->
                        val score = traits[trait] ?: 0
                        val progressFraction = (score / 100f).coerceIn(0f, 1f)

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = trait.label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "$score%",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = RoyalBlue,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            LinearProgressIndicator(
                                progress = { progressFraction },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = RoyalBlue,
                                trackColor = ProgressTrack
                            )
                        }
                    }
                }
            }
        }

        // Master 35-Page Official Diagnostic Dossier Announcement Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("report_exclusive_dossier_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, AmberGold.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
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
                                text = "35-PAGE MASTER DOSSIER EXCLUSIVE",
                                style = MaterialTheme.typography.labelSmall,
                                color = MargNavyDark,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = RoyalBlue.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "OFFICIAL PDF REPORT",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = RoyalBlue,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Parent vs. Student Comparative Study & Master Report (35 Pages)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MargNavy
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "The comprehensive side-by-side Parent vs. Student Comparative Study is generated exclusively inside your official 35-Page Diagnostic Dossier. It features 5 parent question psychological audits, cognitive harmony meters, divergence vectors, family mediation scripts, RIASEC radar graphs, and 6 full 'Day in the Life' career journeys.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PaperSurfaceWarm,
                            border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.3f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("📊 Comparative Matrix", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = MargNavy)
                                Text("Parent Intent & Student Aptitude", fontSize = 9.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PaperSurfaceWarm,
                            border = BorderStroke(1.dp, RoyalBlue.copy(alpha = 0.25f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("🎨 Day in the Life", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = RoyalBlue)
                                Text("6 Full Vibrant Careers & Comp", fontSize = 9.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { showSummaryDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("preview_35page_index_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoyalBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = AmberGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Preview 35-Page Master Dossier Index", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        // Stream-Related Career Opportunities Section (Grouped under recommended stream)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "STREAM-ALIGNED CAREER OPPORTUNITIES",
                        style = MaterialTheme.typography.labelMedium,
                        color = MargNavy,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (selectedStreamFilter == topStream) ForestGreen.copy(alpha = 0.15f) else AmberGold.copy(alpha = 0.25f)
                    ) {
                        Text(
                            text = if (selectedStreamFilter == topStream) "TOP PICK: ${topStream.displayName.uppercase()}" else (selectedStreamFilter?.displayName?.uppercase() ?: "ALL STREAMS"),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (selectedStreamFilter == topStream) ForestGreen else MargNavyDark,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                // Stream Explanation Banner
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (selectedStreamFilter == topStream) ForestGreen.copy(alpha = 0.08f) else PaperSurfaceWarm,
                    border = BorderStroke(1.2.dp, if (selectedStreamFilter == topStream) ForestGreen.copy(alpha = 0.35f) else WarmClay.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (selectedStreamFilter == topStream) Icons.Default.Verified else Icons.Default.RocketLaunch,
                                contentDescription = null,
                                tint = if (selectedStreamFilter == topStream) ForestGreen else WarmClay,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (selectedStreamFilter == topStream) {
                                    "Primary Stream: ${topStream.displayName} Opportunities"
                                } else if (selectedStreamFilter != null) {
                                    "Exploring ${selectedStreamFilter!!.displayName} Stream Options"
                                } else {
                                    "All Stream Opportunities (${careers.size} Careers)"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (selectedStreamFilter == topStream) ForestGreen else MargNavy
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (selectedStreamFilter == topStream) {
                                "These careers strictly leverage your recommended ${topStream.displayName} curriculum (${topStream.subtitle}). Grouped to provide maximum clarity for Class 11-12 preparation."
                            } else if (selectedStreamFilter != null) {
                                "Comparing your cognitive profile fit against alternative ${selectedStreamFilter!!.displayName} disciplines."
                            } else {
                                "Explore cross-disciplinary career paths across Science, Commerce, and Humanities."
                            },
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp
                        )
                    }
                }

                // Stream Filter Selector Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. Recommended Stream Chip (Primary)
                    val isTopSelected = selectedStreamFilter == topStream
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isTopSelected) ForestGreen else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, if (isTopSelected) ForestGreen else MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .clickable { selectedStreamFilter = topStream }
                            .testTag("filter_stream_recommended")
                    ) {
                        Text(
                            text = "⭐ ${topStream.displayName} (Top)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isTopSelected) Color.White else ForestGreen,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                        )
                    }

                    // 2. Other streams
                    Stream.entries.filter { it != topStream }.forEach { otherStream ->
                        val isOtherSelected = selectedStreamFilter == otherStream
                        val otherEmoji = when (otherStream) {
                            Stream.SCIENCE -> "🔬"
                            Stream.COMMERCE -> "💼"
                            Stream.HUMANITIES -> "🏛️"
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isOtherSelected) MargNavy else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, if (isOtherSelected) MargNavy else MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .clickable { selectedStreamFilter = otherStream }
                                .testTag("filter_stream_${otherStream.name.lowercase()}")
                        ) {
                            Text(
                                text = "$otherEmoji ${otherStream.displayName}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isOtherSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }

                    // 3. All Streams Chip
                    val isAllSelected = selectedStreamFilter == null
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isAllSelected) MargNavy else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, if (isAllSelected) MargNavy else MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .clickable { selectedStreamFilter = null }
                            .testTag("filter_stream_all")
                    ) {
                        Text(
                            text = "All",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isAllSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        val displayedCareers = if (isReportUnlocked) filteredCareers else filteredCareers.take(4)

        items(displayedCareers) { scoredCareer ->
            CareerCard(scoredCareer)
        }

        // Unlocked note or Locked Teaser Card
        if (isReportUnlocked && filteredCareers.size > 4) {
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = ForestGreen.copy(alpha = 0.08f),
                    border = BorderStroke(1.2.dp, ForestGreen.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("unlocked_all_careers_dossier_note")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(ForestGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "All ${filteredCareers.size} Careers Detailed in Master Dossier",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = ForestGreen
                            )
                            Text(
                                text = "Initial report displays your top 4 strategic pathways. All remaining career profiles, dual radars, day-in-the-life timelines, and entrance roadmaps are compiled in your unlocked 65-Page PDF below.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        } else if (!isReportUnlocked && filteredCareers.size > 4) {
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = PaperSurfaceWarm,
                    border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenPaywallDialog() }
                        .testTag("locked_careers_teaser_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(AmberGold.copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = MargNavyDark, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "+${filteredCareers.size - 4} More ${selectedStreamFilter?.displayName ?: "Stream"} Careers Locked",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = MargNavy
                                )
                                Text(
                                    text = "Detailed radar profiles, compensation curves & entrance roadmaps in 65-page report",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AmberGold
                        ) {
                            Text(
                                text = "UNLOCK ALL",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MargNavyDark,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }
        }

        // --- OFFICIAL 65-PAGE INSTITUTIONAL DOSSIER SHOWCASE & DOWNLOAD ACTION CARD (NOW AFTER JOB OPPORTUNITIES!) ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("official_dossier_showcase_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, if (isReportUnlocked) RoyalBlue.copy(alpha = 0.5f) else AmberGold.copy(alpha = 0.6f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // Header with Gold Ribbon & Page Badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isReportUnlocked) RoyalBlue.copy(alpha = 0.12f) else AmberGold.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, if (isReportUnlocked) RoyalBlue else AmberGold)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isReportUnlocked) Icons.Default.Verified else Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (isReportUnlocked) RoyalBlue else AmberGold,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isReportUnlocked) "OFFICIAL 65-PAGE DOSSIER READY" else "65-PAGE MASTER HOLISTIC DOSSIER",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isReportUnlocked) RoyalBlue else MargNavy
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PaperSurfaceWarm,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "65 Pages • Printable PDF",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MargNavy
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isReportUnlocked) "Comprehensive 65-Page Psychometric & Neurological Blueprint" else "⚡ Unlock Complete 65-Page Master Holistic Dossier",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MargNavy
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (isReportUnlocked) {
                            "Your full unredacted 65-page dossier is unlocked and verified by Master Counsellor Dr. B.B. Panchal. Includes 10 Neurological Brain Areas, 20+ Career Radars, and 24-Month Roadmap."
                        } else {
                            "You have reviewed your initial 4 job opportunities. Unlock the complete unredacted 65-page dossier including 20+ career deep dives, 10 neurological brain areas, national median benchmarks, and entrance exam roadmap."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 6 Value Highlights Grid
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DossierFeatureChip(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.Psychology,
                                text = "10 Brain Strength Areas"
                            )
                            DossierFeatureChip(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.TrendingUp,
                                text = "National Median Benchmarks"
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DossierFeatureChip(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.Person,
                                text = "Parent Divergence Matrix"
                            )
                            DossierFeatureChip(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.RocketLaunch,
                                text = "20+ Career Compatibility Radars"
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DossierFeatureChip(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.Edit,
                                text = "Class 9-12 Execution Blueprint"
                            )
                            DossierFeatureChip(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.BusinessCenter,
                                text = "24-Month Exam Roadmap & Seal"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Value Pricing Banner (When Locked)
                    if (!isReportUnlocked) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PaperSurfaceWarm,
                            border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Private Institutional Value: ₹4,999",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                    Text(
                                        text = "⚡ Official 65-Page Dossier: ₹499",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ForestGreen
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = AmberGold
                                ) {
                                    Text(
                                        text = "90% OFF",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MargNavyDark,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Primary Download / Unlock CTA Button (Visually Appealing)
                    if (isReportUnlocked) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = RoyalBlue,
                            shadowElevation = 6.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    MargdarshReportPdfGenerator.generateAndDownloadReportPdf(
                                        context = context,
                                        profile = profile,
                                        streamResult = uiState.streamResult,
                                        traits = traits,
                                        detailedReport = detailedReport,
                                        careers = careers,
                                        parentAnswers = uiState.parentAnswers,
                                        studentAnswers = uiState.studentAnswers,
                                        isUnlocked = true,
                                        openAfterSave = true
                                    )
                                }
                                .testTag("download_report_button")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(shiningDossierGradient)
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Download Official 65-Page Holistic Dossier",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        text = "Unlocked • 75 Questions • Brain Map • India Benchmark • 65 Pages",
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MargNavy,
                            shadowElevation = 8.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpenPaywallDialog() }
                                .testTag("download_report_button")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(shiningDossierGradient)
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.LockOpen, contentDescription = null, tint = AmberGold, modifier = Modifier.size(22.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "⚡ Unlock 65-Page Premium Dossier (₹499)",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        text = "65-Page Comprehensive Report • Brain Map • National Benchmark • ₹499",
                                        color = AmberGold,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Secondary Action Buttons: Summary Preview Dialog, Share, Email
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showSummaryDialog = true },
                            modifier = Modifier
                                .weight(1.2f)
                                .testTag("preview_summary_dialog_button"),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Preview 65-Page Index", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RoyalBlue)
                        }

                        OutlinedButton(
                            onClick = {
                                if (isReportUnlocked) {
                                    MargdarshReportPdfGenerator.shareReportPdf(
                                        context = context,
                                        profile = profile,
                                        streamResult = uiState.streamResult,
                                        traits = traits,
                                        detailedReport = detailedReport,
                                        careers = careers,
                                        parentAnswers = uiState.parentAnswers,
                                        studentAnswers = uiState.studentAnswers,
                                        isUnlocked = true
                                    )
                                } else {
                                    onOpenPaywallDialog()
                                }
                            },
                            modifier = Modifier
                                .weight(0.9f)
                                .testTag("share_report_button"),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, tint = MargNavy, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Share PDF", color = MargNavy, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                sendEmailToCounsellor(context, "cpulkit410@gmail.com", profile.studentName, reportText)
                            },
                            modifier = Modifier
                                .weight(0.9f)
                                .testTag("gmail_counsellor_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AmberGold,
                                contentColor = MargNavyDark
                            ),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, tint = MargNavyDark, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Email", color = MargNavyDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // "Day in the Life" Career Stories Section
        item {
            DayInTheLifeCareerStoriesSection()
        }

        // Counselor Contact Box
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.dr_bb_panchal_counsellor_1789197215041),
                            contentDescription = "Dr. B.B. Panchal",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(58.dp)
                                .clip(CircleShape)
                                .background(ForestGreen)
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Dr. B.B. Panchal",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MargNavy,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = ForestGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = "MASTER CAREER COUNSELLOR & STRATEGIST",
                                style = MaterialTheme.typography.labelSmall,
                                color = AmberGold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(PaperSurface)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Hotline", style = MaterialTheme.typography.bodySmall)
                        Text(
                            "+91 79098 06620",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MargNavy
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 1-on-1 Counselling Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.35f)),
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
                                Text(
                                    text = "🎯 1-on-1 Personal Career Mentorship",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = ForestGreen
                                )
                                Text(
                                    text = "45-min Live Consultation • Custom Action Plan",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = ForestGreen.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = "₹999",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ForestGreen,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = CircleShape,
                        color = ForestGreen,
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showCounsellingPaymentDialog = true
                            }
                            .testTag("results_book_one_on_one_counselling_button")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(shiningCounsellingGradient)
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Book 1-on-1 Counselling (₹999)", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = {
                                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+917909806620"))
                                context.startActivity(dialIntent)
                            },
                            modifier = Modifier.weight(1f).testTag("call_counselor_button"),
                            shape = CircleShape,
                            border = BorderStroke(1.2.dp, MargNavy)
                        ) {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = MargNavy, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Call", color = MargNavy, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                sendEmailToCounsellor(context, "cpulkit410@gmail.com", profile.studentName, reportText)
                            },
                            modifier = Modifier.weight(1f).testTag("email_counselor_button"),
                            shape = CircleShape,
                            border = BorderStroke(1.2.dp, MargNavy)
                        ) {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = MargNavy, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Email", color = MargNavy, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Dialogs
    if (uiState.isProfileSurveyOpen) {
        ProfileSurveyDialog(
            initialProfile = uiState.studentProfile,
            onDismiss = onCloseProfileDialog,
            onSave = onSaveProfile
        )
    }

    if (uiState.isCounsellorDialogVisible) {
        CounsellorContactDialog(
            studentName = uiState.studentProfile.studentName,
            reportText = reportText,
            profile = uiState.studentProfile,
            onDismiss = onCloseCounsellorDialog,
            onBookingSuccess = { utr, notes, bookedProfile, amount, coupon ->
                onRecordCounsellingPayment(utr, notes, bookedProfile, amount, coupon)
                onCloseCounsellorDialog()
            }
        )
    }

    if (showSummaryDialog) {
        val isReportUnlocked = uiState.isDetailedAuditUnlocked
        DossierSummaryDialog(
            profile = profile,
            isUnlocked = isReportUnlocked,
            onDismiss = { showSummaryDialog = false },
            onDownload = {
                showSummaryDialog = false
                MargdarshReportPdfGenerator.generateAndDownloadReportPdf(
                    context = context,
                    profile = profile,
                    streamResult = uiState.streamResult,
                    traits = traits,
                    detailedReport = detailedReport,
                    careers = careers,
                    parentAnswers = uiState.parentAnswers,
                    studentAnswers = uiState.studentAnswers,
                    isUnlocked = isReportUnlocked,
                    openAfterSave = true
                )
            },
            onUnlock = {
                showSummaryDialog = false
                onOpenPaywallDialog()
            }
        )
    }
}

@Composable
private fun CareerCard(scoredCareer: ScoredCareer) {
    val career = scoredCareer.career
    var isExpanded by remember { mutableStateOf(false) }

    // Stream-based dynamic vibrant palettes
    val (streamGradient, streamColor, streamBg, streamEmoji) = when (career.stream) {
        Stream.SCIENCE -> {
            if (career.title.contains("Doctor", ignoreCase = true) ||
                career.title.contains("Biotech", ignoreCase = true) ||
                career.title.contains("Medical", ignoreCase = true) ||
                career.title.contains("Psycholog", ignoreCase = true)
            ) {
                listOf(
                    listOf(Color(0xFFE11D48), Color(0xFFFB7185)),
                    RoseVibrant,
                    RoseBg,
                    "🩺"
                )
            } else {
                listOf(
                    listOf(Color(0xFF0284C7), Color(0xFF4F46E5)),
                    RoyalBlue,
                    IndigoBg,
                    "🔬"
                )
            }
        }
        Stream.COMMERCE -> listOf(
            listOf(Color(0xFF059669), Color(0xFF10B981)),
            EmeraldVibrant,
            EmeraldBg,
            "💼"
        )
        Stream.HUMANITIES -> listOf(
            listOf(Color(0xFF7C3AED), Color(0xFFDB2777)),
            PurpleVibrant,
            PurpleBg,
            "🏛️"
        )
    }

    @Suppress("UNCHECKED_CAST")
    val gradientColors = streamGradient as List<Color>
    val accentColor = streamColor as Color
    val accentBg = streamBg as Color
    val emoji = streamEmoji as String

    val tagText = when (career.tag) {
        CareerTag.HIGH_GROWTH -> "🚀 High Growth"
        CareerTag.EMERGING -> "✨ Emerging Tech"
        CareerTag.PREMIUM_ELITE -> "👑 Elite / Tier-1"
        CareerTag.STABLE -> "🛡️ High Job Security"
        CareerTag.UNCOMMON -> "🎯 High Niche Scope"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, accentColor.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Top Gradient Accent Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(gradientColors))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = emoji,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 14.sp
                            )
                        }
                        Text(
                            text = career.stream.displayName.uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }

                    // Fit Match Badge with High Glow Contrast
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = AmberGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${scoredCareer.fit}% FIT",
                                style = MaterialTheme.typography.labelMedium,
                                color = MargNavy,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            // Main Content Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Title and Tag Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = career.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MargNavy
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = career.workType,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = accentBg,
                        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = tagText,
                            style = MaterialTheme.typography.labelSmall,
                            color = accentColor,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = career.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // High-Impact 2x2 Metric Grid with Colorful Surface Tiles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Salary Pill (Emerald Green Accent)
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = EmeraldBg,
                        border = BorderStroke(1.dp, EmeraldVibrant.copy(alpha = 0.25f))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("💰", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "EARNING POTENTIAL",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = EmeraldVibrant,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 10.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = career.salaryLabel,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MargNavyDark,
                                fontSize = 12.sp
                            )
                            if (career.salaryEntry.isNotBlank()) {
                                Text(
                                    text = "Entry: ${career.salaryEntry}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = EmeraldVibrant,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    // Growth / Market Demand Pill (Cyan/Blue Accent)
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = CyanBg,
                        border = BorderStroke(1.dp, CyanVibrant.copy(alpha = 0.25f))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("📈", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "MARKET DEMAND",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CyanVibrant,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 10.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = career.growthRate,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MargNavyDark,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Scope: ${career.scope}",
                                style = MaterialTheme.typography.labelSmall,
                                color = CyanVibrant,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Top Recruiters Pill (Indigo / Purple Accent)
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = IndigoBg,
                        border = BorderStroke(1.dp, IndigoVibrant.copy(alpha = 0.25f))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🏢", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "TOP RECRUITERS",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = IndigoVibrant,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 10.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = career.topRecruiters,
                                style = MaterialTheme.typography.bodySmall,
                                color = MargNavyDark,
                                fontSize = 11.sp,
                                maxLines = 2
                            )
                        }
                    }

                    // Education & Route Pill (Amber Accent)
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = AmberBg,
                        border = BorderStroke(1.dp, AmberVibrant.copy(alpha = 0.25f))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🎓", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "EDUCATION ROUTE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AmberVibrant,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 10.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = career.costLabel,
                                style = MaterialTheme.typography.bodySmall,
                                color = MargNavyDark,
                                fontSize = 11.sp,
                                maxLines = 2
                            )
                        }
                    }
                }

                // Trait Alignment Chips
                if (career.traits.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Psychometric Traits:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.sp
                        )
                        career.traits.take(3).forEach { trait ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = PaperSurfaceWarm,
                                border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = trait.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MargNavy,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                // Expandable Career Roadmap Button & Section
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = PaperSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isExpanded = !isExpanded }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = accentColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isExpanded) "Hide Career Roadmap & Benefits" else "View Strategic Degree Pathway & Benefits",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = accentColor,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(10.dp))

                            if (career.roadmap.isNotBlank()) {
                                Text(
                                    text = "🗺️ Recommended 4-Year Academic Pathway:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MargNavy
                                )
                                Text(
                                    text = career.roadmap,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 17.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            if (career.benefits.isNotBlank()) {
                                Text(
                                    text = "🌟 Core Perks & Long-Term Advantages:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MargNavy
                                )
                                Text(
                                    text = career.benefits,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 17.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            if (career.salaryExperienced.isNotBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = EmeraldBg,
                                    border = BorderStroke(1.dp, EmeraldVibrant.copy(alpha = 0.3f))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("🏆", fontSize = 14.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Experienced / Leadership Earning Ceiling: ${career.salaryExperienced}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = EmeraldVibrant,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressRow(label: String, progress: Float, count: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
            Text(count, style = MaterialTheme.typography.bodySmall, color = MargNavy, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = RoyalBlue,
            trackColor = ProgressTrack
        )
    }
}

@Composable
private fun ParentStudentDivergenceDashboardCard(
    divergenceData: com.example.margdarsh.data.ParentStudentComparativeDashboardData,
    studentName: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("divergence_dashboard_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, RoyalBlue.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MargNavy
                ) {
                    Text(
                        text = "COMPARATIVE DASHBOARD",
                        style = MaterialTheme.typography.labelSmall,
                        color = AmberGold,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EmeraldBg,
                    border = BorderStroke(1.dp, EmeraldVibrant.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "Harmony: ${divergenceData.overallHarmonyScore}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = EmeraldVibrant,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Student Strengths vs. Parental Expectations",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MargNavy
            )

            Text(
                text = divergenceData.primaryDivergenceSummary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 17.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            divergenceData.divergencePoints.forEach { point ->
                var isExpanded by remember { mutableStateOf(false) }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { isExpanded = !isExpanded },
                    color = PaperSurface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = point.dimension,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MargNavy
                            )

                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(android.graphics.Color.parseColor(point.divergenceColorHex)).copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = point.divergenceLevel,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(android.graphics.Color.parseColor(point.divergenceColorHex)),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Side by Side split
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Left: Student
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                color = IndigoBg
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "🎯 Student Aptitude",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = IndigoVibrant,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = point.studentAptitude,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            // Right: Parent
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                color = AmberBg
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "🏛️ Parent Expectation",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = AmberVibrant,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = point.parentExpectation,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = EmeraldBg,
                                border = BorderStroke(1.dp, EmeraldVibrant.copy(alpha = 0.3f))
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "🤝 Counselor Bridging Strategy:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = ForestGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = point.counselorBridgingStrategy,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayInTheLifeCareerStoriesSection() {
    var selectedStoryIndex by remember { mutableStateOf(0) }
    val stories = DAY_IN_THE_LIFE_STORIES
    val activeStory = stories.getOrElse(selectedStoryIndex) { stories.first() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("day_in_the_life_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = WarmClay
                ) {
                    Text(
                        text = "REAL-WORLD GLIMPSE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = "4 Premium Snapshots",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "“Day in the Life” Career Snapshots",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MargNavy
            )

            Text(
                text = "Experience what top-performing professionals actually do on a typical day.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Selector tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                stories.forEachIndexed { index, story ->
                    val isSelected = index == selectedStoryIndex
                    val shortName = when (story.id) {
                        "data_scientist" -> "AI / Data"
                        "product_designer" -> "UI/UX"
                        "corporate_lawyer" -> "M&A Law"
                        "investment_banker" -> "FinBank"
                        else -> story.careerTitle.take(8)
                    }
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { selectedStoryIndex = index },
                        color = if (isSelected) MargNavy else PaperSurface,
                        border = BorderStroke(1.dp, if (isSelected) MargNavy else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = shortName,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) AmberGold else MargNavy,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 7.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Active Story Details Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = PaperSurfaceWarm,
                border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Text(
                        text = activeStory.careerTitle,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MargNavy
                    )
                    Text(
                        text = "${activeStory.roleBadge} • ${activeStory.industry}",
                        style = MaterialTheme.typography.labelSmall,
                        color = RoyalBlue
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = activeStory.coreMission,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "🕒 Typical Hour-by-Hour Workflow:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MargNavy
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    activeStory.schedule.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MargNavyDark
                            ) {
                                Text(
                                    text = item.time,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AmberGold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.activityTitle,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MargNavy
                                )
                                Text(
                                    text = item.description,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = EmeraldBg,
                        border = BorderStroke(1.dp, EmeraldVibrant.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = "💡 High School Pro-Tip:",
                                style = MaterialTheme.typography.labelSmall,
                                color = ForestGreen,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = activeStory.highSchoolProTip,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "💰 Comp Potential: ${activeStory.averageCompRange}",
                        style = MaterialTheme.typography.labelSmall,
                        color = EmeraldVibrant,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun downloadAndShareReport(context: Context, studentName: String, reportContent: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_SUBJECT, "MargDarsh Career Dossier: $studentName")
        putExtra(Intent.EXTRA_TEXT, reportContent)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Share or Save Career Dossier")
    context.startActivity(shareIntent)
}

@Composable
private fun DossierFeatureChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = PaperSurface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(13.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = MargNavy,
                maxLines = 1
            )
        }
    }
}

@Composable
fun DossierSummaryDialog(
    profile: com.example.margdarsh.data.StudentProfile,
    isUnlocked: Boolean,
    onDismiss: () -> Unit,
    onDownload: () -> Unit,
    onUnlock: () -> Unit
) {
    val chapters = listOf(
        Triple("Module 1 (Pages 1–4)", "Executive Cover & 7-Trait Thinking Graphs", "Comprehensive profile summary, Holland RIASEC radar, national percentiles, and cognitive stamina curves."),
        Triple("Module 2 (Pages 5–7)", "Core Superpowers & Stream Blueprint", "Innate cognitive assets, 90-day growth multiplier roadmaps, and High School stream suitability meters."),
        Triple("Module 3 (Pages 8–19)", "45-Question In-Depth Psychological Audit", "Complete breakdown of all 45 questions with psychological construct, subconscious insight, and academic impact."),
        Triple("Module 4 (Pages 20–22)", "Parent Diagnostic & Comparative Divergence Matrix", "Parent question intent audits, Cognitive Harmony Index (0-100%), divergence vectors, and family mediation playbook."),
        Triple("Module 5 (Pages 23–29)", "Top 6 Careers & Vibrant 'Day in the Life'", "6 dedicated deep dives with 2030 hour-by-hour operational journeys, career growth, compensation ladders, and recruiter matrices."),
        Triple("Module 6 (Pages 30–35)", "4-Year Grade 9-12 Master Blueprint & Exams", "Year-by-year milestone execution roadmap, Top 25 Exam matrix, directory, scholarship guide, and Dr. B.B. Panchal validation seal."),
        Triple("Module 7 (Pages 36–41)", "360° Quotient Diagnosis & Personality Continuums", "Overall Brain Score, IQ, PQ, SQ, and 10 bi-directional personality continuums with extreme trait markers."),
        Triple("Module 8 (Pages 42–47)", "10 Neurological Brain Strength Map & Benchmarks", "Neurological brain diagram with Frontal, Broca, Wernicke, Occipital, Parietal lobes and national median comparisons."),
        Triple("Module 9 (Pages 48–57)", "Top 20 Dedicated Career Dossiers & Dual Radars", "Dual-polygon radars (Student vs Industry Benchmark), 'Day in the Life' timelines, entrance tests, and premier colleges."),
        Triple("Module 10 (Pages 58–65)", "Master 20-Career Compatibility Matrix & Family Concordat", "Complete leaderboard matrix across 20 primary careers, 24-month entrance timeline, institutional directory, and counselor verification seal.")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .testTag("dossier_summary_dialog"),
        confirmButton = {},
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(PaperBackground)
            ) {
                // Header Bar with Gold Gradient Accent
                Surface(
                    color = MargNavyDark,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
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
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = MargNavyDark, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "INSTITUTIONAL REPORT",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MargNavyDark
                                    )
                                }
                            }

                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White.copy(alpha = 0.8f))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (isUnlocked) "Official 65-Page Master Holistic Dossier" else "Official 65-Page Master Diagnostic Dossier",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "Prepared for: ${profile.studentName.ifBlank { "Candidate" }} • ${profile.studentClass}",
                            fontSize = 11.sp,
                            color = AmberGold,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Scrollable Table of Contents
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PaperSurfaceWarm,
                            border = BorderStroke(1.dp, AmberGold.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Shield, contentDescription = null, tint = AmberVibrant, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Official Career Assessment Standard",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = MargNavy
                                    )
                                    Text(
                                        text = "Generated using Holland's RIASEC model and Big-5 Psychometric Taxonomy.",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    items(chapters) { (module, title, desc) ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = RoyalBlue.copy(alpha = 0.1f)
                                    ) {
                                        Text(
                                            text = module,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = RoyalBlue,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = ForestGreen,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = title,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MargNavy
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = desc,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }

                // Footer CTA
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        if (isUnlocked) {
                            Button(
                                onClick = onDownload,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("dialog_download_pdf_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = RoyalBlue,
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Download 65-Page PDF Dossier", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        } else {
                            val dialogTransition = rememberInfiniteTransition(label = "summary_dialog_shimmer")
                            val dialogShimmerTranslate by dialogTransition.animateFloat(
                                initialValue = 0f,
                                targetValue = 1200f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
                                    repeatMode = RepeatMode.Restart
                                ),
                                label = "dialog_shimmer_translation"
                            )

                            val dialogShiningGradient = Brush.linearGradient(
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
                                start = Offset(dialogShimmerTranslate - 400f, 0f),
                                end = Offset(dialogShimmerTranslate + 200f, 200f)
                            )

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MargNavy,
                                shadowElevation = 6.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onUnlock() }
                                    .testTag("dialog_unlock_pdf_button")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(dialogShiningGradient)
                                        .padding(horizontal = 16.dp, vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(Icons.Default.LockOpen, contentDescription = null, tint = AmberGold, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Unlock Complete 65-Page Dossier (₹499)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
