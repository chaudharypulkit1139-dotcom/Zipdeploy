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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.margdarsh.data.StudentAccount
import com.example.margdarsh.data.TestLeaderboardEntry
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
fun TestLeaderboardScreen(
    studentClass: String,
    currentAccount: StudentAccount?,
    leaderboardEntries: List<TestLeaderboardEntry>,
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
    }

    val fastestStudent = leaderboardEntries.firstOrNull { it.isFastest }
    val growthStudent = leaderboardEntries.firstOrNull { it.isHighestGrowth }

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
                IconButton(onClick = onBack, modifier = Modifier.testTag("leaderboard_back_btn")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MargNavy)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Class $studentClass Leaderboard",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MargNavy
                    )
                    Text(
                        text = "Rankings based on test score points, speed & growth",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkTextSecondary
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Spotlight Boxes: Fastest Solver & Highest Growth
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Fastest Solver Spotlight
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("spotlight_fastest_card"),
                        colors = CardDefaults.cardColors(containerColor = RoyalBlue.copy(alpha = 0.08f)),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RoyalBlue.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(RoyalBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Bolt, contentDescription = null, tint = PaperBackground, modifier = Modifier.size(15.dp))
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("FASTEST SOLVER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RoyalBlue)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = fastestStudent?.studentName ?: "Rohan Verma",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MargNavy,
                                maxLines = 1
                            )
                            Text(
                                text = "${fastestStudent?.fastestCompletionSeconds ?: 290}s avg completion",
                                fontSize = 11.sp,
                                color = InkTextSecondary
                            )
                        }
                    }

                    // Highest Growth Spotlight
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("spotlight_growth_card"),
                        colors = CardDefaults.cardColors(containerColor = ForestGreen.copy(alpha = 0.08f)),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreen.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(ForestGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = PaperBackground, modifier = Modifier.size(15.dp))
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("HIGHEST GROWTH", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = growthStudent?.studentName ?: "Priya Patel",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MargNavy,
                                maxLines = 1
                            )
                            Text(
                                text = "+${growthStudent?.growthRatePct?.toInt() ?: 22}% accuracy rise",
                                fontSize = 11.sp,
                                color = InkTextSecondary
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Class Rankings",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MargNavy
                    )
                    Text(
                        text = "Total Points",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = InkTextSecondary
                    )
                }
            }

            // Leaderboard list
            items(leaderboardEntries) { entry ->
                val isSelf = currentAccount != null && entry.studentId == currentAccount.studentId

                val rankColor = when (entry.rank) {
                    1 -> Color(0xFFD4AF37) // Gold
                    2 -> Color(0xFF94A3B8) // Silver
                    3 -> Color(0xFFCD7F32) // Bronze
                    else -> InkTextSecondary
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("leaderboard_item_${entry.rank}"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelf) ForestGreen.copy(alpha = 0.08f) else PaperSurface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = if (isSelf) androidx.compose.foundation.BorderStroke(1.5.dp, ForestGreen) else androidx.compose.foundation.BorderStroke(1.dp, InkTextSecondary.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Rank Badge
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(rankColor.copy(alpha = 0.15f))
                                .border(1.dp, rankColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "#${entry.rank}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = rankColor
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Student Info
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isSelf) "${entry.studentName} (You)" else entry.studentName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MargNavy
                                )
                                if (entry.isFastest) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("⚡", fontSize = 12.sp)
                                }
                                if (entry.isHighestGrowth) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("📈", fontSize = 12.sp)
                                }
                            }
                            Text(
                                text = "${entry.testsTaken} tests • %.1f%% accuracy".format(entry.avgAccuracy),
                                fontSize = 12.sp,
                                color = InkTextSecondary
                            )
                        }

                        // Total Points Pill
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Stars, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${entry.totalPoints}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MargNavy
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                // Explanatory points rule card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = PaperSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, InkTextSecondary.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("How Points Are Awarded:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MargNavy)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("• Base Points: 10 pts per correct MCQ mark.", fontSize = 12.sp, color = InkTextSecondary)
                        Text("• Accuracy Milestone: +50 pts for ≥90% accuracy, +25 pts for ≥75%.", fontSize = 12.sp, color = InkTextSecondary)
                        Text("• ⚡ Speed Bonus: +30 pts for finishing in top 50% time bracket with ≥70% accuracy.", fontSize = 12.sp, color = InkTextSecondary)
                        Text("• 📈 Growth Bonus: +40 pts for improving accuracy compared to your previous test.", fontSize = 12.sp, color = InkTextSecondary)
                    }
                }
            }
        }
    }
}
