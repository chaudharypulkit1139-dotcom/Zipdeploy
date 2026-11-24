package com.example.margdarsh.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.margdarsh.network.DossierAutoDispatcher
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.margdarsh.data.AcademicTest
import com.example.margdarsh.data.StudentAccount
import com.example.margdarsh.data.TestSubmission
import com.example.margdarsh.ui.theme.ForestGreen
import com.example.margdarsh.ui.theme.GoldAccent
import com.example.margdarsh.ui.theme.InkTextPrimary
import com.example.margdarsh.ui.theme.InkTextSecondary
import com.example.margdarsh.ui.theme.MargNavy
import com.example.margdarsh.ui.theme.PaperBackground
import com.example.margdarsh.ui.theme.PaperSurface
import com.example.margdarsh.ui.theme.RoyalBlue
import com.example.margdarsh.ui.theme.WarmClay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class TestSubTab {
    ONGOING,
    UPCOMING,
    PREVIOUS
}

@Composable
fun TestsHubScreen(
    currentAccount: StudentAccount?,
    allTests: List<AcademicTest>,
    submissions: List<TestSubmission>,
    onStartTest: (AcademicTest) -> Unit,
    onReviewTest: (AcademicTest, TestSubmission) -> Unit,
    onOpenLeaderboard: () -> Unit,
    onOpenAuthDialog: () -> Unit,
    onChangeClass: (String) -> Unit
) {
    val studentClass = currentAccount?.studentClass ?: "10"
    val classTests = allTests.filter { it.targetClass.replace("Class ", "") == studentClass }

    val ongoingTests = classTests.filter { test ->
        val hasSubmitted = submissions.any { it.testId == test.id }
        test.isOngoing && !hasSubmitted
    }
    val upcomingTests = classTests.filter { it.isUpcoming }
    val previousTests = classTests.filter { test ->
        val hasSubmitted = submissions.any { it.testId == test.id }
        test.isPrevious || hasSubmitted
    }

    var selectedTab by remember { mutableStateOf(TestSubTab.ONGOING) }
    var classMenuOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PaperBackground)
    ) {
        // Top Header Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = PaperSurface,
            tonalElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Academic Test Hub",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MargNavy
                        )
                        Text(
                            text = "Class-wise mock tests, benchmarking & review",
                            style = MaterialTheme.typography.bodySmall,
                            color = InkTextSecondary
                        )
                    }

                    // Class Dropdown Selector
                    Box {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .border(1.dp, ForestGreen, RoundedCornerShape(20.dp))
                                .clickable { classMenuOpen = true }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            color = ForestGreen.copy(alpha = 0.08f)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = ForestGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Class $studentClass ▼",
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreen,
                                    fontSize = 13.sp
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = classMenuOpen,
                            onDismissRequest = { classMenuOpen = false }
                        ) {
                            listOf("8", "9", "10", "11", "12").forEach { c ->
                                DropdownMenuItem(
                                    text = { Text("Class $c") },
                                    onClick = {
                                        onChangeClass(c)
                                        classMenuOpen = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Points & Leaderboard Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Points Card
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(GoldAccent.copy(alpha = 0.15f))
                            .border(1.dp, GoldAccent.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stars,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${currentAccount?.totalPoints ?: 50} Points",
                            fontWeight = FontWeight.Bold,
                            color = MargNavy,
                            fontSize = 14.sp
                        )
                    }

                    // View Leaderboard Button
                    Button(
                        onClick = onOpenLeaderboard,
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("hub_view_leaderboard_btn")
                    ) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Class Leaderboard", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Account Status Banner
        if (currentAccount == null) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = GoldAccent.copy(alpha = 0.12f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.45f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = MargNavy,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Sign in to save scores & rank",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MargNavy
                            )
                            Text(
                                text = "Track progress, earn badges & compete in Class $studentClass",
                                fontSize = 11.sp,
                                color = InkTextSecondary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onOpenAuthDialog,
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("test_hub_sign_in_btn")
                    ) {
                        Text("Sign In", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = ForestGreen.copy(alpha = 0.08f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreen.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = ForestGreen,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = currentAccount.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = MargNavy
                            )
                            Text(
                                text = "Class ${currentAccount.studentClass} • ${currentAccount.email}",
                                fontSize = 11.sp,
                                color = InkTextSecondary
                            )
                        }
                    }
                    TextButton(
                        onClick = onOpenAuthDialog,
                        modifier = Modifier.testTag("test_hub_switch_account_btn")
                    ) {
                        Text("Account", fontSize = 12.sp, color = ForestGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        val portalUrl = if (DossierAutoDispatcher.customGoogleSheetWebhookUrl.contains("?")) {
            DossierAutoDispatcher.customGoogleSheetWebhookUrl + "&page=teacher"
        } else {
            DossierAutoDispatcher.customGoogleSheetWebhookUrl + "?page=teacher"
        }

        // Sub Tabs (Ongoing, Upcoming, Previous)
        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            containerColor = PaperSurface,
            contentColor = ForestGreen,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                    color = ForestGreen,
                    height = 3.dp
                )
            }
        ) {
            Tab(
                selected = selectedTab == TestSubTab.ONGOING,
                onClick = { selectedTab = TestSubTab.ONGOING },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Ongoing", fontWeight = FontWeight.Bold)
                        if (ongoingTests.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(ForestGreen)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = ongoingTests.size.toString(),
                                    color = PaperBackground,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                modifier = Modifier.testTag("tab_sub_ongoing")
            )
            Tab(
                selected = selectedTab == TestSubTab.UPCOMING,
                onClick = { selectedTab = TestSubTab.UPCOMING },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Upcoming", fontWeight = FontWeight.Bold)
                        if (upcomingTests.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(WarmClay)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = upcomingTests.size.toString(),
                                    color = PaperBackground,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                modifier = Modifier.testTag("tab_sub_upcoming")
            )
            Tab(
                selected = selectedTab == TestSubTab.PREVIOUS,
                onClick = { selectedTab = TestSubTab.PREVIOUS },
                text = { Text("Previous", fontWeight = FontWeight.Bold) },
                modifier = Modifier.testTag("tab_sub_previous")
            )
        }

        // Content Area
        val currentList = when (selectedTab) {
            TestSubTab.ONGOING -> ongoingTests
            TestSubTab.UPCOMING -> upcomingTests
            TestSubTab.PREVIOUS -> previousTests
        }

        if (currentList.isEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Icon(
                        imageVector = when (selectedTab) {
                            TestSubTab.ONGOING -> Icons.Default.FactCheck
                            TestSubTab.UPCOMING -> Icons.Default.Event
                            TestSubTab.PREVIOUS -> Icons.Default.AssignmentTurnedIn
                        },
                        contentDescription = null,
                        tint = InkTextSecondary.copy(alpha = 0.5f),
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = when (selectedTab) {
                            TestSubTab.ONGOING -> "No ongoing tests for Class $studentClass right now"
                            TestSubTab.UPCOMING -> "No upcoming tests scheduled for Class $studentClass"
                            TestSubTab.PREVIOUS -> "No previous tests completed yet"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = InkTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Teachers upload scheduled tests via the Teacher Web Portal outside this app.",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkTextSecondary
                    )
                }

                item {
                    TeacherConsoleCard(portalUrl = portalUrl)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(currentList) { test ->
                    val submission = submissions.firstOrNull { it.testId == test.id }
                    TestCard(
                        test = test,
                        submission = submission,
                        tab = selectedTab,
                        onJoin = { onStartTest(test) },
                        onReview = {
                            if (submission != null) {
                                onReviewTest(test, submission)
                            }
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    TeacherConsoleCard(portalUrl = portalUrl)
                }
            }
        }
    }
}

@Composable
fun TeacherConsoleCard(portalUrl: String) {
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = PaperSurface,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, RoyalBlue.copy(alpha = 0.25f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(RoyalBlue.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = RoyalBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Teacher & Admin Web Portal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MargNavy
                    )
                    Text(
                        text = "Create tests, set syllabus & inspect live student submissions",
                        fontSize = 11.sp,
                        color = InkTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Use this dedicated portal in any web browser to publish MCQs with explanations directly to Google Sheets.",
                fontSize = 12.sp,
                color = InkTextSecondary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(portalUrl))
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            Toast.makeText(context, "Could not open browser", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .weight(1.3f)
                        .height(38.dp)
                        .testTag("open_teacher_portal_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                ) {
                    Icon(Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Open Portal", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Margdarsh Teacher Portal", portalUrl)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Teacher Portal URL copied!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .testTag("copy_teacher_portal_link_btn"),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, tint = MargNavy, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy Link", fontSize = 12.sp, color = MargNavy, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun TestCard(
    test: AcademicTest,
    submission: TestSubmission?,
    tab: TestSubTab,
    onJoin: () -> Unit,
    onReview: () -> Unit
) {
    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("test_card_${test.id}"),
        colors = CardDefaults.cardColors(containerColor = PaperSurface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Subject & Class row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ForestGreen.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = test.subject.uppercase(),
                            color = ForestGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Class ${test.targetClass}",
                        color = InkTextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Status Pill
                if (submission != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(ForestGreen)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Score: ${submission.score}/${submission.totalMarks}",
                            color = PaperBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                } else if (test.isOngoing) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(RoyalBlue)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "LIVE NOW",
                            color = PaperBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = test.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MargNavy
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Syllabus preview
            if (test.syllabus.isNotBlank()) {
                Text(
                    text = "Syllabus: ${test.syllabus}",
                    style = MaterialTheme.typography.bodySmall,
                    color = InkTextSecondary,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Metrics row: Duration & Marks
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = InkTextSecondary, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${test.durationMinutes} Mins", fontSize = 12.sp, color = InkTextSecondary)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Stars, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${test.totalMarks} Marks (${test.questions.size} MCQs)", fontSize = 12.sp, color = InkTextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action section depending on state
            when {
                submission != null -> {
                    // Already completed - Show review button & points badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "+${submission.pointsAwarded} pts earned",
                                fontWeight = FontWeight.Bold,
                                color = ForestGreen,
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            onClick = onReview,
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("review_test_btn_${test.id}")
                        ) {
                            Text("View Answers & Explanations", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                test.isOngoing -> {
                    // Ongoing and unattempted - Show Join Test Button
                    Button(
                        onClick = onJoin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("join_test_btn_${test.id}"),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Join Test", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
                test.isUpcoming -> {
                    // Upcoming - Show countdown or schedule info
                    val startStr = try { sdf.format(Date(test.startTimeEpochMs)) } catch (_: Exception) { "Upcoming" }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(WarmClay.copy(alpha = 0.08f))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Event, contentDescription = null, tint = WarmClay, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Opens on: $startStr",
                            color = WarmClay,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                else -> {
                    // Expired without submission
                    OutlinedButton(
                        onClick = onJoin, // Allow practicing expired tests
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Take as Practice Test", color = MargNavy, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
