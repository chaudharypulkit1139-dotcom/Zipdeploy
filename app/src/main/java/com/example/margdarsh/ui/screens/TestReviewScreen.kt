package com.example.margdarsh.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.margdarsh.data.AcademicTest
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

@Composable
fun TestReviewScreen(
    test: AcademicTest,
    submission: TestSubmission,
    onBack: () -> Unit,
    onOpenLeaderboard: () -> Unit
) {
    BackHandler {
        onBack()
    }

    val minutes = submission.timeTakenSeconds / 60
    val secs = submission.timeTakenSeconds % 60
    val timeTakenStr = "%d min %02d sec".format(minutes, secs)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PaperBackground)
    ) {
        // Top Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = PaperSurface,
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("review_back_btn")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MargNavy)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Test Analysis & Explanations",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MargNavy
                    )
                    Text(
                        text = "${test.title} • Class ${test.targetClass}",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkTextSecondary
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Score Summary Hero Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = PaperSurface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Your Score",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = InkTextSecondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${submission.score} / ${submission.totalMarks}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreen
                                )
                            }

                            // Accuracy Ring Pill
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ForestGreen.copy(alpha = 0.1f))
                                    .border(1.dp, ForestGreen.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "%.1f%% Accuracy".format(submission.accuracyPct),
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreen,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Points and bonuses breakdown
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Total points
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                color = GoldAccent.copy(alpha = 0.15f)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Stars, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(15.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Points Earned", fontSize = 11.sp, color = InkTextSecondary)
                                    }
                                    Text(
                                        text = "+${submission.pointsAwarded} pts",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = MargNavy
                                    )
                                }
                            }

                            // Time Taken
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                color = PaperBackground
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = InkTextSecondary, modifier = Modifier.size(15.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Time Taken", fontSize = 11.sp, color = InkTextSecondary)
                                    }
                                    Text(
                                        text = timeTakenStr,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MargNavy
                                    )
                                }
                            }
                        }

                        // Badges achieved in this test
                        if (submission.speedBonus > 0 || submission.growthBonus > 0) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (submission.speedBonus > 0) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(RoyalBlue.copy(alpha = 0.12f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Bolt, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("⚡ +${submission.speedBonus} Speed Bonus", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RoyalBlue)
                                        }
                                    }
                                }
                                if (submission.growthBonus > 0) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(ForestGreen.copy(alpha = 0.12f))
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("📈 +${submission.growthBonus} Growth Bonus", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Leaderboard Button
                        Button(
                            onClick = onOpenLeaderboard,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("See Class Leaderboard & Rank", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Question-by-Question Review (${test.questions.size} Questions)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MargNavy
                )
            }

            // Questions with Explanations
            itemsIndexed(test.questions) { index, question ->
                val userChoice = submission.answers[question.id]
                val isCorrect = userChoice != null && userChoice == question.correctIndex
                val isUnattempted = userChoice == null

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = PaperSurface),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Header with status indicator
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Question ${index + 1}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MargNavy
                            )

                            // Status Chip
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        when {
                                            isCorrect -> ForestGreen.copy(alpha = 0.12f)
                                            isUnattempted -> InkTextSecondary.copy(alpha = 0.12f)
                                            else -> WarmClay.copy(alpha = 0.12f)
                                        }
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = when {
                                            isCorrect -> Icons.Default.CheckCircle
                                            isUnattempted -> Icons.Default.AccessTime
                                            else -> Icons.Default.Close
                                        },
                                        contentDescription = null,
                                        tint = when {
                                            isCorrect -> ForestGreen
                                            isUnattempted -> InkTextSecondary
                                            else -> WarmClay
                                        },
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = when {
                                            isCorrect -> "Correct (+${question.marks})"
                                            isUnattempted -> "Unattempted"
                                            else -> "Incorrect (0)"
                                        },
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when {
                                            isCorrect -> ForestGreen
                                            isUnattempted -> InkTextSecondary
                                            else -> WarmClay
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Question statement
                        Text(
                            text = question.question,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = InkTextPrimary,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Options
                        question.options.forEachIndexed { optIdx, optText ->
                            val isCorrectOpt = optIdx == question.correctIndex
                            val isUserPicked = userChoice == optIdx
                            val label = ('A'.code + optIdx).toChar()

                            val (bgColor, borderColor, textColor) = when {
                                isCorrectOpt -> Triple(
                                    ForestGreen.copy(alpha = 0.12f),
                                    ForestGreen,
                                    ForestGreen
                                )
                                isUserPicked && !isCorrectOpt -> Triple(
                                    WarmClay.copy(alpha = 0.12f),
                                    WarmClay,
                                    WarmClay
                                )
                                else -> Triple(
                                    PaperBackground,
                                    InkTextSecondary.copy(alpha = 0.15f),
                                    InkTextPrimary
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(bgColor)
                                    .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "($label)  $optText",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = textColor,
                                        fontWeight = if (isCorrectOpt || isUserPicked) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (isCorrectOpt) {
                                        Text("✓ Correct Answer", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                                    } else if (isUserPicked) {
                                        Text("✗ Your Choice", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WarmClay)
                                    }
                                }
                            }
                        }

                        // Explanation Box
                        if (question.explanation.isNotBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GoldAccent.copy(alpha = 0.08f))
                                    .border(1.dp, GoldAccent.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Lightbulb, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Teacher's Step-by-Step Explanation", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MargNavy)
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = question.explanation,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = InkTextPrimary,
                                        lineHeight = 18.sp
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
