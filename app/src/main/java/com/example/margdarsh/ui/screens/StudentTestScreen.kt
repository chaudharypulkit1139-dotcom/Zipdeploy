package com.example.margdarsh.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.margdarsh.data.LIKERT_LABELS
import com.example.margdarsh.data.QuestionType
import com.example.margdarsh.data.STUDENT_QUESTIONS
import com.example.margdarsh.data.StudentQuestion
import com.example.margdarsh.ui.theme.ForestGreen
import com.example.margdarsh.ui.theme.InkTextPrimary
import com.example.margdarsh.ui.theme.MargNavy
import com.example.margdarsh.ui.theme.PaperBackground
import com.example.margdarsh.ui.theme.PaperSurface
import com.example.margdarsh.ui.theme.ProgressTrack
import com.example.margdarsh.ui.theme.WarmClay
import com.example.margdarsh.viewmodel.MargdarshUiState

@Composable
fun StudentTestScreen(
    uiState: MargdarshUiState,
    onAnswer: (questionId: String, value: String) -> Unit,
    onNavigateIndex: (Int) -> Unit,
    onFinishToParent: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentIndex = uiState.currentStudentIndex.coerceIn(0, STUDENT_QUESTIONS.size - 1)
    val question = STUDENT_QUESTIONS[currentIndex]
    val answeredCount = uiState.studentAnswers.size
    val currentAnswer = uiState.studentAnswers[question.id]
    val progress = answeredCount.toFloat() / STUDENT_QUESTIONS.size.toFloat()

    val circleOuterSizes = listOf(46.dp, 38.dp, 32.dp, 38.dp, 46.dp)
    val circleInnerSizes = listOf(18.dp, 14.dp, 10.dp, 14.dp, 18.dp)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PaperBackground),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp)
    ) {
        // Progress bar and section badge
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
                        color = ForestGreen,
                        trackColor = ProgressTrack
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Question ${currentIndex + 1} of ${STUDENT_QUESTIONS.size}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (question.type == QuestionType.LIKERT) WarmClay.copy(alpha = 0.15f) else ForestGreen.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = question.section.uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = if (question.type == QuestionType.LIKERT) WarmClay else ForestGreen,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = PaperSurface
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (question.type == QuestionType.LIKERT) Icons.Default.Psychology else Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = ForestGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when (question.type) {
                                    QuestionType.MCQ_APTITUDE -> "Logic MCQ"
                                    QuestionType.SCENARIO_PREFERENCE -> "Scenario MCQ"
                                    QuestionType.LIKERT -> "Trait Scale"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Question Statement
        item {
            AnimatedContent(
                targetState = question,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "question_text"
            ) { targetQ ->
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp)) {
                    Text(
                        text = targetQ.text,
                        style = MaterialTheme.typography.headlineLarge,
                        color = ForestGreen,
                        fontSize = 22.sp,
                        lineHeight = 30.sp
                    )

                    if (targetQ.contextSnippet.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = PaperSurface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = WarmClay,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = targetQ.contextSnippet,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Options according to question type
        item {
            when (question.type) {
                QuestionType.MCQ_APTITUDE, QuestionType.SCENARIO_PREFERENCE -> {
                    // Render 4 choice options (A, B, C, D)
                    Column(
                        modifier = Modifier.fillMaxWidth().testTag("mcq_options_container"),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val optionLetters = listOf("A", "B", "C", "D")
                        question.options.forEachIndexed { optIndex, opt ->
                            val letter = optionLetters.getOrElse(optIndex) { "${optIndex + 1}" }
                            val isSelected = currentAnswer == opt.id

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable {
                                        onAnswer(question.id, opt.id)
                                    }
                                    .testTag("mcq_option_${opt.id}"),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) ForestGreen.copy(alpha = 0.10f) else MaterialTheme.colorScheme.surface
                                ),
                                border = BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) ForestGreen else MaterialTheme.colorScheme.outline
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) ForestGreen else PaperSurface)
                                            .border(
                                                width = 1.dp,
                                                color = if (isSelected) ForestGreen else MaterialTheme.colorScheme.outlineVariant,
                                                shape = CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = letter,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else InkTextPrimary
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Text(
                                        text = opt.text,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isSelected) ForestGreen else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        modifier = Modifier.weight(1f),
                                        lineHeight = 20.sp
                                    )

                                    if (isSelected) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = ForestGreen,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                QuestionType.LIKERT -> {
                    // Likert Scale 1 to 5 Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("likert_options_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "How much do you agree?",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                LIKERT_LABELS.forEachIndexed { i, label ->
                                    val scoreStr = "${i + 1}"
                                    val isSelected = currentAnswer == scoreStr

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable {
                                                onAnswer(question.id, scoreStr)
                                            }
                                            .padding(vertical = 8.dp)
                                            .testTag("likert_option_$scoreStr")
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(circleOuterSizes[i])
                                                .clip(CircleShape)
                                                .background(
                                                    if (isSelected) ForestGreen.copy(alpha = 0.15f)
                                                    else PaperSurface
                                                )
                                                .border(
                                                    width = if (isSelected) 2.dp else 1.dp,
                                                    color = if (isSelected) ForestGreen else MaterialTheme.colorScheme.outlineVariant,
                                                    shape = CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isSelected) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(circleInnerSizes[i])
                                                        .clip(CircleShape)
                                                        .background(ForestGreen)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        if (i == 0 || i == 2 || i == 4) {
                                            Text(
                                                text = label,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontSize = 10.sp,
                                                textAlign = TextAlign.Center,
                                                color = if (isSelected) ForestGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        } else {
                                            Text(
                                                text = "Somewhat",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontSize = 9.sp,
                                                textAlign = TextAlign.Center,
                                                color = if (isSelected) ForestGreen else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
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

        // Navigation actions: Previous / Next / Continue
        item {
            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        if (currentIndex > 0) {
                            onNavigateIndex(currentIndex - 1)
                        }
                    },
                    enabled = currentIndex > 0,
                    shape = CircleShape,
                    modifier = Modifier.testTag("student_prev_button")
                ) {
                    Text("Previous")
                }

                Button(
                    onClick = {
                        if (currentIndex < STUDENT_QUESTIONS.size - 1) {
                            onNavigateIndex(currentIndex + 1)
                        } else {
                            onFinishToParent()
                        }
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentIndex == STUDENT_QUESTIONS.size - 1) ForestGreen else MargNavy,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.testTag("student_next_button")
                ) {
                    Text(
                        text = if (currentIndex < STUDENT_QUESTIONS.size - 1) "Next Question"
                        else "Continue to Parent Survey",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Question quick-navigator grid
        item {
            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PaperSurface)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(
                        text = "Question Overview (${answeredCount}/${STUDENT_QUESTIONS.size})",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val rows = STUDENT_QUESTIONS.chunked(9)
                    rows.forEach { chunk ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.5.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            chunk.forEach { q ->
                                val qIdx = STUDENT_QUESTIONS.indexOf(q)
                                val hasAnswer = uiState.studentAnswers[q.id] != null
                                val isCurrent = qIdx == currentIndex

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(28.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(
                                            when {
                                                isCurrent -> ForestGreen
                                                hasAnswer -> ForestGreen.copy(alpha = 0.25f)
                                                else -> PaperBackground
                                            }
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (isCurrent) ForestGreen else MaterialTheme.colorScheme.outlineVariant,
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .clickable { onNavigateIndex(qIdx) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${qIdx + 1}",
                                        fontSize = 10.sp,
                                        fontWeight = if (isCurrent || hasAnswer) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isCurrent) MaterialTheme.colorScheme.onPrimary else InkTextPrimary
                                    )
                                }
                            }
                            if (chunk.size < 10) {
                                repeat(10 - chunk.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
