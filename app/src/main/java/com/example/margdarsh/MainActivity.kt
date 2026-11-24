package com.example.margdarsh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.margdarsh.ui.components.CounsellorReminderDialog
import com.example.margdarsh.ui.components.MargdarshBottomBar
import com.example.margdarsh.data.AcademicTest
import com.example.margdarsh.data.TestSubmission
import com.example.margdarsh.ui.components.AuthDialog
import com.example.margdarsh.ui.components.MargdarshTab
import com.example.margdarsh.ui.components.MargdarshTopBar
import com.example.margdarsh.ui.components.PremiumPaywallDialog
import com.example.margdarsh.ui.components.ProfileSurveyDialog
import com.example.margdarsh.ui.components.PsychometricIntroGateDialog
import com.example.margdarsh.ui.screens.ActiveTestScreen
import com.example.margdarsh.ui.screens.HomeScreen
import com.example.margdarsh.ui.screens.ParentSurveyScreen
import com.example.margdarsh.ui.screens.ResourcesScreen
import com.example.margdarsh.ui.screens.ResultsScreen
import com.example.margdarsh.ui.screens.SkillDevelopmentScreen
import com.example.margdarsh.ui.screens.StudentTestScreen
import com.example.margdarsh.ui.screens.TestLeaderboardScreen
import com.example.margdarsh.ui.screens.TestReviewScreen
import com.example.margdarsh.ui.screens.TestsHubScreen
import com.example.margdarsh.ui.theme.ForestGreen
import com.example.margdarsh.ui.theme.MargdarshTheme
import com.example.margdarsh.ui.theme.PaperBackground
import com.example.margdarsh.ui.theme.PaperSurface
import com.example.margdarsh.viewmodel.MargdarshViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MargdarshViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MargdarshTheme {
                MargdarshApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MargdarshApp(viewModel: MargdarshViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val currentAccount by viewModel.currentAccount.collectAsState()
    val allTests by viewModel.allTests.collectAsState()
    val submissions by viewModel.submissions.collectAsState()

    var currentTab by remember { mutableStateOf(MargdarshTab.HOME) }
    var activeTest by remember { mutableStateOf<AcademicTest?>(null) }
    var reviewTestPair by remember { mutableStateOf<Pair<AcademicTest, TestSubmission>?>(null) }
    var showLeaderboard by remember { mutableStateOf(false) }
    var showAuthDialog by remember { mutableStateOf(false) }

    if (currentTab != MargdarshTab.HOME && activeTest == null && reviewTestPair == null && !showLeaderboard) {
        BackHandler {
            currentTab = MargdarshTab.HOME
        }
    }

    Scaffold(
        topBar = {
            MargdarshTopBar(
                onHomeClick = { currentTab = MargdarshTab.HOME }
            )
        },
        bottomBar = {
            MargdarshBottomBar(
                currentTab = currentTab,
                onTabSelected = { targetTab ->
                    if ((targetTab == MargdarshTab.STUDENT || targetTab == MargdarshTab.PARENT) && !uiState.isLoggedIn) {
                        viewModel.setProfileSurveyOpen(true)
                    } else {
                        currentTab = targetTab
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MargdarshTab.HOME -> {
                    HomeScreen(
                        uiState = uiState,
                        onNavigateToStudent = {
                            if (!uiState.isLoggedIn) {
                                viewModel.setProfileSurveyOpen(true)
                            } else {
                                currentTab = MargdarshTab.STUDENT
                            }
                        },
                        onNavigateToParent = {
                            if (!uiState.isLoggedIn) {
                                viewModel.setProfileSurveyOpen(true)
                            } else {
                                currentTab = MargdarshTab.PARENT
                            }
                        },
                        onNavigateToResults = { currentTab = MargdarshTab.RESULTS },
                        onNavigateToResources = { currentTab = MargdarshTab.RESOURCES },
                        onNavigateToSkills = { currentTab = MargdarshTab.SKILLS },
                        onOpenProfileDialog = { viewModel.setProfileSurveyOpen(true) },
                        onRecordCounsellingPayment = { utr, notes, bookedProfile, amount, coupon ->
                            viewModel.recordCounsellingPayment(utr, notes, bookedProfile, amount, coupon)
                        },
                        onRecordCounsellingPaymentWithScreenshot = { utr, notes, bookedProfile, amount, coupon, uri ->
                            viewModel.recordCounsellingPayment(utr, notes, bookedProfile, amount, coupon, screenshotUri = uri)
                        }
                    )
                }
                MargdarshTab.STUDENT -> {
                    if (!uiState.isLoggedIn) {
                        RegistrationRequiredView(
                            title = "Student Aptitude Assessment Locked",
                            description = "Candidate registration with verified Google email is required before starting the 45 aptitude and psychometric questions.",
                            onRegisterClick = { viewModel.setProfileSurveyOpen(true) }
                        )
                    } else {
                        StudentTestScreen(
                            uiState = uiState,
                            onAnswer = { qId, value ->
                                viewModel.answerStudent(qId, value, autoAdvance = true)
                            },
                            onNavigateIndex = { idx ->
                                viewModel.setStudentIndex(idx)
                            },
                            onFinishToParent = {
                                currentTab = MargdarshTab.PARENT
                            }
                        )
                    }
                }
                MargdarshTab.PARENT -> {
                    if (!uiState.isLoggedIn) {
                        RegistrationRequiredView(
                            title = "Parent Perspectives Survey Locked",
                            description = "Candidate registration with verified Google email is required before completing the parent survey and budget aspirations.",
                            onRegisterClick = { viewModel.setProfileSurveyOpen(true) }
                        )
                    } else {
                        ParentSurveyScreen(
                            uiState = uiState,
                            onAnswer = { qId, value ->
                                viewModel.answerParent(qId, value)
                            },
                            onNavigateToResults = {
                                currentTab = MargdarshTab.RESULTS
                            }
                        )
                    }
                }
                MargdarshTab.RESOURCES -> {
                    ResourcesScreen(
                        uiState = uiState,
                        onUnlockResources = { utr, amount, title ->
                            viewModel.unlockResources(utr = utr, amount = amount, itemName = title)
                        },
                        onUnlockSingleResource = { resourceId, resourceTitle, utr ->
                            viewModel.unlockSingleResource(resourceId = resourceId, resourceTitle = resourceTitle, utr = utr)
                        },
                        onUnlockResourcesWithScreenshot = { utr, amount, title, uri ->
                            viewModel.unlockResources(utr = utr, amount = amount, itemName = title, screenshotUri = uri)
                        },
                        onUnlockSingleResourceWithScreenshot = { resourceId, resourceTitle, utr, uri ->
                            viewModel.unlockSingleResource(resourceId = resourceId, resourceTitle = resourceTitle, utr = utr, screenshotUri = uri)
                        },
                        onUpdateUpiId = { newUpi ->
                            viewModel.updatePersonalUpiId(newUpi)
                        }
                    )
                }
                MargdarshTab.SKILLS -> {
                    SkillDevelopmentScreen()
                }
                MargdarshTab.RESULTS -> {
                    ResultsScreen(
                        uiState = uiState,
                        detailedReport = viewModel.getDetailedReport(),
                        onNavigateToStudent = { currentTab = MargdarshTab.STUDENT },
                        onNavigateToParent = { currentTab = MargdarshTab.PARENT },
                        onResetSession = {
                            viewModel.retakeAssessment()
                            currentTab = MargdarshTab.STUDENT
                        },
                        onOpenProfileDialog = { viewModel.setProfileSurveyOpen(true) },
                        onCloseProfileDialog = { viewModel.setProfileSurveyOpen(false) },
                        onSaveProfile = { profile -> viewModel.updateProfile(profile) },
                        onOpenCounsellorDialog = { viewModel.setCounsellorDialogVisible(true) },
                        onCloseCounsellorDialog = { viewModel.setCounsellorDialogVisible(false) },
                        onUnlockPremium = { utr -> viewModel.unlockDetailedAudit(utr = utr) },
                        onOpenPaywallDialog = { viewModel.setPaywallDialogVisible(true) },
                        onClosePaywallDialog = { viewModel.setPaywallDialogVisible(false) },
                        onSyncDossier = { viewModel.dispatchFullDossierNow() },
                        reportText = viewModel.generateComprehensiveReportText(),
                        onRecordCounsellingPayment = { utr, notes, bookedProfile, amount, coupon ->
                            viewModel.recordCounsellingPayment(utr, notes, bookedProfile, amount, coupon)
                        },
                        onRecordCounsellingPaymentWithScreenshot = { utr, notes, bookedProfile, amount, coupon, uri ->
                            viewModel.recordCounsellingPayment(utr, notes, bookedProfile, amount, coupon, screenshotUri = uri)
                        }
                    )
                }
                MargdarshTab.TESTS -> {
                    when {
                        activeTest != null -> {
                            val curTest = activeTest!!
                            ActiveTestScreen(
                                test = curTest,
                                onSubmit = { answers, timeTaken ->
                                    val sub = viewModel.submitAcademicTest(curTest, answers, timeTaken)
                                    activeTest = null
                                    reviewTestPair = Pair(curTest, sub)
                                },
                                onCancel = { activeTest = null }
                            )
                        }
                        reviewTestPair != null -> {
                            val pair = reviewTestPair!!
                            TestReviewScreen(
                                test = pair.first,
                                submission = pair.second,
                                onBack = { reviewTestPair = null },
                                onOpenLeaderboard = {
                                    reviewTestPair = null
                                    showLeaderboard = true
                                }
                            )
                        }
                        showLeaderboard -> {
                            val targetClass = currentAccount?.studentClass ?: "10"
                            TestLeaderboardScreen(
                                studentClass = targetClass,
                                currentAccount = currentAccount,
                                leaderboardEntries = viewModel.getClassLeaderboard(targetClass),
                                onBack = { showLeaderboard = false }
                            )
                        }
                        else -> {
                            TestsHubScreen(
                                currentAccount = currentAccount,
                                allTests = allTests,
                                submissions = submissions,
                                onStartTest = { activeTest = it },
                                onReviewTest = { t, s -> reviewTestPair = Pair(t, s) },
                                onOpenLeaderboard = { showLeaderboard = true },
                                onOpenAuthDialog = { showAuthDialog = true },
                                onChangeClass = { newClass -> viewModel.updateStudentClass(newClass) }
                            )
                        }
                    }
                }
            }

            // 0. Academic Student Account Dialog
            if (showAuthDialog) {
                AuthDialog(
                    isOpen = showAuthDialog,
                    onDismiss = { showAuthDialog = false },
                    onRegister = { name, phone, email, targetClass, password ->
                        viewModel.registerStudentAccount(name, phone, email, targetClass, password)
                    },
                    onLogin = { phoneOrEmail, password ->
                        viewModel.loginStudentAccount(phoneOrEmail, password)
                    },
                    currentAccount = currentAccount
                )
            }

            // 1. Mandatory Initial Registration Gate
            if (uiState.isProfileSurveyOpen) {
                ProfileSurveyDialog(
                    initialProfile = uiState.studentProfile,
                    onDismiss = {
                        if (uiState.studentProfile.studentName.isNotBlank() && uiState.studentProfile.phoneNumber.isNotBlank()) {
                            viewModel.setProfileSurveyOpen(false)
                        }
                    },
                    onSave = { profile ->
                        viewModel.updateProfile(profile)
                    }
                )
            }

            // 2. "Why Take Psychometric Test" Gate Dialog
            if (uiState.showPsychometricIntroGate && !uiState.isStudentDone) {
                PsychometricIntroGateDialog(
                    profile = uiState.studentProfile,
                    onStartTest = {
                        viewModel.setPsychometricIntroGateVisible(false)
                        currentTab = MargdarshTab.STUDENT
                    }
                )
            }

            // 3. High-priority Counsellor Advisory Notice (Shows only on Home screen when not dismissed)
            if (uiState.showCounsellorReminderDialog && !uiState.isProfileSurveyOpen && !uiState.showPsychometricIntroGate && currentTab == MargdarshTab.HOME) {
                CounsellorReminderDialog(
                    profile = uiState.studentProfile,
                    onDismiss = { viewModel.dismissCounsellorReminderDialog() },
                    onBookingSuccess = { utr, notes, bookedProfile, amount, coupon ->
                        viewModel.recordCounsellingPayment(utr, notes, bookedProfile, amount, coupon)
                        viewModel.dismissCounsellorReminderDialog()
                    },
                    onBookingSuccessWithScreenshot = { utr, notes, bookedProfile, amount, coupon, uri ->
                        viewModel.recordCounsellingPayment(utr, notes, bookedProfile, amount, coupon, screenshotUri = uri)
                        viewModel.dismissCounsellorReminderDialog()
                    }
                )
            }

            // 4. Official 35-Page Diagnostic Dossier Premium Paywall (₹499)
            if (uiState.isPaywallDialogOpen) {
                PremiumPaywallDialog(
                    studentName = uiState.studentProfile.studentName,
                    onDismiss = { viewModel.setPaywallDialogVisible(false) },
                    onUnlock = { utr ->
                        val is100 = utr.contains("100")
                        val is50 = utr.contains("50")
                        val amt = if (is100) "0" else if (is50) "249" else "499"
                        val cpn = if (utr.startsWith("COUPON_")) utr.removePrefix("COUPON_") else null
                        viewModel.unlockDetailedAudit(utr = utr, amount = amt, couponCode = cpn)
                    },
                    onUnlockDetailed = { utr, amt, cpn, uri ->
                        viewModel.unlockDetailedAudit(utr = utr, amount = amt, couponCode = cpn, screenshotUri = uri)
                    }
                )
            }
        }
    }
}

@Composable
private fun RegistrationRequiredView(
    title: String,
    description: String,
    onRegisterClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PaperBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = PaperSurface,
            border = BorderStroke(1.5.dp, ForestGreen.copy(alpha = 0.4f)),
            shadowElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = ForestGreen.copy(alpha = 0.12f),
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = ForestGreen,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onRegisterClick,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForestGreen,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google_logo),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Sign in with Google / Register",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

