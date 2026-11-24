package com.example.margdarsh.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TableChart
import com.example.margdarsh.network.DossierAutoDispatcher
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.margdarsh.R
import com.example.margdarsh.data.PARENT_QUESTIONS
import com.example.margdarsh.data.STUDENT_QUESTIONS
import com.example.margdarsh.network.DispatchStatus
import com.example.margdarsh.ui.components.MargdarshHeroBrandBanner
import com.example.margdarsh.ui.theme.ForestGreen
import com.example.margdarsh.ui.theme.InkTextPrimary
import com.example.margdarsh.ui.theme.MargNavy
import com.example.margdarsh.ui.theme.PaperBackground
import com.example.margdarsh.ui.theme.PaperSurface
import com.example.margdarsh.ui.theme.PaperSurfaceWarm
import com.example.margdarsh.ui.theme.WarmClay
import com.example.margdarsh.viewmodel.MargdarshUiState

@Composable
fun HomeScreen(
    uiState: MargdarshUiState,
    onNavigateToStudent: () -> Unit,
    onNavigateToParent: () -> Unit,
    onNavigateToResults: () -> Unit,
    onNavigateToResources: () -> Unit,
    onNavigateToSkills: () -> Unit,
    onOpenProfileDialog: () -> Unit,
    onRecordCounsellingPayment: (utr: String, notes: String, profile: com.example.margdarsh.data.StudentProfile, amount: String, couponCode: String?) -> Unit = { _, _, _, _, _ -> },
    onRecordCounsellingPaymentWithScreenshot: (utr: String, notes: String, profile: com.example.margdarsh.data.StudentProfile, amount: String, couponCode: String?, screenshotUri: Uri?) -> Unit = { utr, notes, p, amt, cpn, _ -> onRecordCounsellingPayment(utr, notes, p, amt, cpn) },
    modifier: Modifier = Modifier
) {
    val studentAnswered = uiState.studentAnswers.size
    val parentAnswered = uiState.parentAnswers.size
    val profile = uiState.studentProfile
    val isProfileFilled = profile.studentName.isNotBlank() && profile.phoneNumber.isNotBlank()
    val isTestsCompleted = uiState.isStudentDone && uiState.isParentDone

    val counsellorPhone = "+917909806620"
    val counsellorName = "Dr. B.B. Panchal"
    val context = LocalContext.current
    var showCounsellingPaymentDialog by remember { mutableStateOf(false) }
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }
    var showTermsOfServiceDialog by remember { mutableStateOf(false) }

    // Shimmering animation for 1-on-1 Counselling button
    val infiniteTransition = rememberInfiniteTransition(label = "home_counselling_shimmer")
    val shimmerTranslate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translation"
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
            profile = profile,
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

    if (showPrivacyPolicyDialog) {
        com.example.margdarsh.ui.components.PrivacyPolicyDialog(
            onDismiss = { showPrivacyPolicyDialog = false }
        )
    }

    if (showTermsOfServiceDialog) {
        com.example.margdarsh.ui.components.TermsOfServiceDialog(
            onDismiss = { showTermsOfServiceDialog = false }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PaperBackground),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Hero Brand Banner & Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MargdarshHeroBrandBanner(
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Mapping the path from classroom to career.",
                    style = MaterialTheme.typography.displayMedium,
                    color = ForestGreen,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Text(
                    text = "A scientific psychometric assessment, aptitude evaluation, and parental context engine designed for Indian students and families.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }

        // Counselor Feature Card with Photo
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("counsellor_feature_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = PaperSurfaceWarm),
                border = BorderStroke(1.2.dp, WarmClay.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.dr_bb_panchal_counsellor_1789197215041),
                            contentDescription = counsellorName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(ForestGreen)
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = counsellorName,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = ForestGreen,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = ForestGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "Senior English Teacher & Career Counsellor",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Bodhami Registered • Alison Univ U.K. Certified",
                                style = MaterialTheme.typography.labelSmall,
                                color = WarmClay,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Marks alone show what you've memorized—not the high-paying careers where you will naturally excel. Connect directly with Dr. Panchal for stream planning, personal 1-on-1 career mapping & parent mediation.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 1-on-1 Counselling Highlight Box
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
                                    text = "🎯 1-on-1 Strategic Mentorship",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = ForestGreen
                                )
                                Text(
                                    text = "45-min Live Session • Stream Selection • Parent Q&A",
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
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ForestGreen,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        shape = CircleShape,
                        color = ForestGreen,
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showCounsellingPaymentDialog = true
                            }
                            .testTag("home_book_one_on_one_counselling_button")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(shiningCounsellingGradient)
                                .padding(vertical = 13.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Book 1-on-1 Counselling (₹999)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val message = "Hello Dr. Panchal, I am ${profile.studentName.ifBlank { "a student" }} (${profile.studentClass}). I am exploring my career options on Margdarsh and have a query."
                                com.example.margdarsh.util.WhatsAppLauncher.launch(context, counsellorPhone, message)
                            },
                            shape = CircleShape,
                            border = BorderStroke(1.2.dp, ForestGreen),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("home_whatsapp_counsellor_button")
                        ) {
                            Text("General Query", fontSize = 12.sp, color = ForestGreen, fontWeight = FontWeight.SemiBold)
                        }

                        OutlinedButton(
                            onClick = {
                                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$counsellorPhone"))
                                context.startActivity(callIntent)
                            },
                            shape = CircleShape,
                            border = BorderStroke(1.2.dp, ForestGreen),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("home_call_counsellor_button")
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Call Now", fontSize = 12.sp, color = ForestGreen, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // Assessment Status Section (Conditional: If completed, show ONLY Result button. If pending, show Test buttons)
        if (isTestsCompleted) {
            // TESTS ARE COMPLETED: Do NOT show test buttons! Only show Results Dossier & Outcomes.
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_assessment_completed_card"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = PaperSurface),
                    border = BorderStroke(1.5.dp, ForestGreen)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = ForestGreen.copy(alpha = 0.12f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = ForestGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ASSESSMENTS COMPLETE • REPORT READY",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ForestGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Your Psychometric Dossier is Generated!",
                            style = MaterialTheme.typography.headlineMedium,
                            color = ForestGreen,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        val recStream = uiState.streamResult?.stream?.displayName ?: "Personalized Stream"
                        Text(
                            text = "Recommended Pathway: $recStream. Full breakdown of salaries, Tier-1 colleges, and entrance timelines ready.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = onNavigateToResults,
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ForestGreen,
                                contentColor = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("view_results_only_button")
                        ) {
                            Icon(Icons.Default.Assessment, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Open Career Dossier & Results", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        } else {
            // TESTS NOT COMPLETED: Show test phase action buttons
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("phase1_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PaperSurface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
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
                                shape = CircleShape,
                                color = ForestGreen.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = "STEP 1 • STUDENT TEST",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = ForestGreen,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Text(
                                text = if (uiState.isStudentDone) "Completed" else "$studentAnswered / ${STUDENT_QUESTIONS.size} questions",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (uiState.isStudentDone) ForestGreen else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Student Aptitude & Psychometric Assessment",
                            style = MaterialTheme.typography.headlineLarge,
                            color = ForestGreen
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Evaluates analytical, spatial, verbal, enterprising, and creative traits through MCQs and scenario puzzles.",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onNavigateToStudent,
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ForestGreen,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.testTag("student_start_button")
                        ) {
                            Text(
                                text = if (uiState.isStudentDone) "Review Student Answers" else if (studentAnswered > 0) "Continue Test" else "Start Student Test",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Phase 2 Card: Parent Perspectives
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("phase2_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PaperSurfaceWarm),
                    border = BorderStroke(1.dp, WarmClay.copy(alpha = 0.25f))
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
                                shape = CircleShape,
                                color = WarmClay.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "STEP 2 • PARENT PERSPECTIVES",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = WarmClay,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Text(
                                text = if (uiState.isParentDone) "Completed" else "$parentAnswered / ${PARENT_QUESTIONS.size} questions",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (uiState.isParentDone) WarmClay else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Parent Survey",
                            style = MaterialTheme.typography.headlineLarge,
                            color = WarmClay
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Align family budget, coaching preferences, geographical mobility, and career expectations.",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedButton(
                            onClick = onNavigateToParent,
                            shape = CircleShape,
                            border = BorderStroke(1.dp, WarmClay),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = WarmClay),
                            modifier = Modifier.testTag("parent_start_button")
                        ) {
                            Text(
                                text = if (uiState.isParentDone) "Edit Answers" else if (parentAnswered > 0) "Continue Survey" else "Start Parent Test"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // NEW SECTION 1: Resource Material (PDFs)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_resources_section_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PaperSurface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
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
                        Surface(shape = CircleShape, color = ForestGreen.copy(alpha = 0.12f)) {
                            Text(
                                text = "RESOURCE MATERIAL",
                                style = MaterialTheme.typography.labelSmall,
                                color = ForestGreen,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Text("Free PDF Access", color = ForestGreen, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Parents' Career Resource Vault",
                        style = MaterialTheme.typography.titleLarge,
                        color = ForestGreen,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Exhaustive 77-page Commerce & Neutral Career directories, 75+ degrees, college cutoffs, and stream risk audits.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onNavigateToResources,
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForestGreen,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("open_resources_button")
                    ) {
                        Icon(Icons.Default.AutoStories, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Browse & Download PDF Handbooks", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // NEW SECTION 2: Skill Development (10 Skills)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("home_skills_section_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PaperSurface),
                border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.25f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Surface(shape = CircleShape, color = ForestGreen.copy(alpha = 0.12f)) {
                        Text(
                            text = "HIGH-INCOME SKILLS",
                            style = MaterialTheme.typography.labelSmall,
                            color = ForestGreen,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "10 Essential Future-Proof Skills",
                        style = MaterialTheme.typography.titleLarge,
                        color = ForestGreen,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Curated actionable guides for AI Prompting, Financial Intelligence, Public Speaking, Python, Data Analytics, and Negotiation.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = onNavigateToSkills,
                        shape = CircleShape,
                        border = BorderStroke(1.dp, ForestGreen),
                        modifier = Modifier.testTag("open_skills_button")
                    ) {
                        Icon(Icons.Default.Lightbulb, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Explore 10 Skills & Roadmaps", color = ForestGreen)
                    }
                }
            }
        }

        // NEW SECTION 3: Scholarship Button with "Coming Soon" Tag
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Toast.makeText(context, "Scholarship Portal is launching soon! Notifications will be sent via WhatsApp.", Toast.LENGTH_SHORT).show()
                    }
                    .testTag("scholarship_coming_soon_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PaperSurfaceWarm),
                border = BorderStroke(1.dp, WarmClay.copy(alpha = 0.35f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = WarmClay.copy(alpha = 0.15f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.School, contentDescription = null, tint = WarmClay, modifier = Modifier.size(22.dp))
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Scholarship Finder",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = InkTextPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = CircleShape,
                                    color = WarmClay
                                ) {
                                    Text(
                                        text = "COMING SOON",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                            Text(
                                text = "Merit & means-based financial aid worth up to ₹5 Lakhs/yr.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // NEW SECTION 4: Online Learning with "Coming Soon" Tag
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Toast.makeText(context, "Online Learning Masterclasses launching next month!", Toast.LENGTH_SHORT).show()
                    }
                    .testTag("online_learning_coming_soon_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PaperSurface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = ForestGreen.copy(alpha = 0.12f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Language, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(22.dp))
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Online Learning Hub",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = InkTextPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = CircleShape,
                                    color = ForestGreen
                                ) {
                                    Text(
                                        text = "COMING SOON",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                            Text(
                                text = "Curated certified masterclasses from premier educators.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Footer: Legal, Privacy Policy & Partner Info
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Privacy Policy",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen,
                        modifier = Modifier
                            .clickable { showPrivacyPolicyDialog = true }
                            .padding(horizontal = 6.dp, vertical = 6.dp)
                            .testTag("home_privacy_policy_btn")
                    )
                    Text("•", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                    Text(
                        text = "Terms of Service",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen,
                        modifier = Modifier
                            .clickable { showTermsOfServiceDialog = true }
                            .padding(horizontal = 6.dp, vertical = 6.dp)
                            .testTag("home_terms_of_service_btn")
                    )
                    Text("•", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                    Text(
                        text = "Data Safety",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen,
                        modifier = Modifier
                            .clickable { showPrivacyPolicyDialog = true }
                            .padding(horizontal = 6.dp, vertical = 6.dp)
                            .testTag("home_data_safety_btn")
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "MargDarsh Career Intelligence © 2026 • In partnership with Rankmakers",
                    fontSize = 10.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
