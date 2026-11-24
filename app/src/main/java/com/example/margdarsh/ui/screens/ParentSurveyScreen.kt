package com.example.margdarsh.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.material.icons.filled.CloudDone
import com.example.margdarsh.data.PARENT_QUESTIONS
import com.example.margdarsh.network.DispatchStatus
import com.example.margdarsh.network.DossierAutoDispatcher
import com.example.margdarsh.ui.theme.ForestGreen
import com.example.margdarsh.ui.theme.InkTextPrimary
import com.example.margdarsh.ui.theme.PaperBackground
import com.example.margdarsh.ui.theme.PaperSurface
import com.example.margdarsh.ui.theme.ProgressTrack
import com.example.margdarsh.ui.theme.WarmClay
import com.example.margdarsh.viewmodel.MargdarshUiState

@Composable
fun ParentSurveyScreen(
    uiState: MargdarshUiState,
    onAnswer: (questionId: String, value: String) -> Unit,
    onNavigateToResults: () -> Unit,
    modifier: Modifier = Modifier
) {
    val answeredCount = PARENT_QUESTIONS.count { uiState.parentAnswers[it.id] != null }
    val progress = answeredCount.toFloat() / PARENT_QUESTIONS.size.toFloat()
    val isAllDone = answeredCount == PARENT_QUESTIONS.size

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PaperBackground),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Header & Progress
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = WarmClay,
                        trackColor = ProgressTrack
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "$answeredCount of ${PARENT_QUESTIONS.size} answered",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Parent Perspectives",
                    style = MaterialTheme.typography.displayMedium,
                    color = WarmClay
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Your answers keep the recommendation grounded in what your family can actually support — budget, distance and your reading of your child.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = WarmClay.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, WarmClay.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDone,
                            contentDescription = null,
                            tint = WarmClay,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Auto-Synced to Master Counsellor Banty Panchal (cpulkit410@gmail.com) in real time.",
                            style = MaterialTheme.typography.labelSmall,
                            color = WarmClay,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Parent Questions List
        itemsIndexed(PARENT_QUESTIONS) { index, q ->
            val selectedValue = uiState.parentAnswers[q.id]

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("parent_question_${q.id}"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = WarmClay.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = q.section.uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = WarmClay,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "${index + 1}. ${q.text}",
                        style = MaterialTheme.typography.titleMedium,
                        color = ForestGreen,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        q.options.forEach { opt ->
                            val isSelected = selectedValue == opt.value

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) WarmClay.copy(alpha = 0.12f)
                                        else PaperSurface
                                    )
                                    .border(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) WarmClay else MaterialTheme.colorScheme.outlineVariant,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { onAnswer(q.id, opt.value) }
                                    .padding(horizontal = 16.dp, vertical = 14.dp)
                                    .testTag("option_${q.id}_${opt.value}"),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = opt.label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isSelected) InkTextPrimary else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    modifier = Modifier.weight(1f)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) WarmClay else MaterialTheme.colorScheme.outlineVariant,
                                            shape = CircleShape
                                        )
                                        .background(if (isSelected) WarmClay else PaperSurface),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colorScheme.surface)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Submission / Proceed Action
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PaperSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isAllDone) "All questions answered — your report is ready!"
                        else "Answer all questions to unlock the full recommendation report.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onNavigateToResults,
                        enabled = isAllDone || answeredCount > 0,
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForestGreen,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.testTag("see_recommendation_button")
                    ) {
                        Text("See Recommendation", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
