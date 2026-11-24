package com.example.margdarsh.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
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
import com.example.margdarsh.ui.theme.ForestGreen
import com.example.margdarsh.ui.theme.GoldAccent
import com.example.margdarsh.ui.theme.InkTextPrimary
import com.example.margdarsh.ui.theme.InkTextSecondary
import com.example.margdarsh.ui.theme.MargNavy
import com.example.margdarsh.ui.theme.PaperBackground
import com.example.margdarsh.ui.theme.PaperSurface
import com.example.margdarsh.ui.theme.WarmClay
import kotlinx.coroutines.delay

@Composable
fun ActiveTestScreen(
    test: AcademicTest,
    onSubmit: (answers: Map<String, Int>, timeTakenSeconds: Int) -> Unit,
    onCancel: () -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<String, Int>() }
    val totalSeconds = test.durationMinutes * 60
    var secondsRemaining by remember { mutableIntStateOf(totalSeconds) }
    var timeTakenSeconds by remember { mutableIntStateOf(0) }
    var showSubmitConfirmation by remember { mutableStateOf(false) }
    var showExitConfirmation by remember { mutableStateOf(false) }

    // Intercept back button
    BackHandler {
        showExitConfirmation = true
    }

    // Timer countdown
    LaunchedEffect(Unit) {
        while (secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining--
            timeTakenSeconds++
        }
        // Auto-submit when time expires
        onSubmit(userAnswers.toMap(), timeTakenSeconds)
    }

    val minutes = secondsRemaining / 60
    val secs = secondsRemaining % 60
    val timerString = "%02d:%02d".format(minutes, secs)
    val currentQuestion = test.questions.getOrNull(currentIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PaperBackground)
    ) {
        // Exam Top Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = PaperSurface,
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showExitConfirmation = true },
                    modifier = Modifier.testTag("test_exit_btn")
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Exit Test", tint = InkTextSecondary)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = test.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MargNavy,
                        maxLines = 1
                    )
                    Text(
                        text = "Class ${test.targetClass} • ${test.subject}",
                        style = MaterialTheme.typography.bodySmall,
                        color = InkTextSecondary
                    )
                }

                // Countdown Timer Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (secondsRemaining <= 120) WarmClay.copy(alpha = 0.15f) else ForestGreen.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = if (secondsRemaining <= 120) WarmClay else ForestGreen,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = timerString,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (secondsRemaining <= 120) WarmClay else ForestGreen
                    )
                }
            }
        }

        // Question Navigator Palette Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = PaperSurface.copy(alpha = 0.6f),
            tonalElevation = 1.dp
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(test.questions) { index, q ->
                    val isAnswered = userAnswers.containsKey(q.id)
                    val isCurrent = index == currentIndex
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCurrent -> MargNavy
                                    isAnswered -> ForestGreen
                                    else -> InkTextSecondary.copy(alpha = 0.15f)
                                }
                            )
                            .clickable { currentIndex = index },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (index + 1).toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (isCurrent || isAnswered) PaperBackground else InkTextPrimary
                        )
                    }
                }
            }
        }

        if (currentQuestion == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No questions found in this test")
            }
        } else {
            // Main Question Body
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Question Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Question ${currentIndex + 1} of ${test.questions.size}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MargNavy
                    )
                    Text(
                        text = "+${currentQuestion.marks} Marks",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreen
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Question Statement Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = PaperSurface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = currentQuestion.question,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = InkTextPrimary,
                        modifier = Modifier.padding(16.dp),
                        lineHeight = 24.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Select your answer:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = InkTextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Options List
                val selectedOption = userAnswers[currentQuestion.id]
                currentQuestion.options.forEachIndexed { optIndex, optionText ->
                    val isSelected = selectedOption == optIndex
                    val optionLabel = ('A'.code + optIndex).toChar()

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clickable {
                                userAnswers[currentQuestion.id] = optIndex
                            }
                            .testTag("option_${currentQuestion.id}_$optIndex"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) ForestGreen.copy(alpha = 0.08f) else PaperSurface
                        ),
                        shape = RoundedCornerShape(10.dp),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, ForestGreen) else androidx.compose.foundation.BorderStroke(1.dp, InkTextSecondary.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { userAnswers[currentQuestion.id] = optIndex },
                                colors = RadioButtonDefaults.colors(selectedColor = ForestGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "($optionLabel)  $optionText",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) ForestGreen else InkTextPrimary
                            )
                        }
                    }
                }
            }

            // Bottom Navigation Footer
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = PaperSurface,
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous Button
                    OutlinedButton(
                        onClick = { if (currentIndex > 0) currentIndex-- },
                        enabled = currentIndex > 0,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Prev")
                    }

                    // Next or Finish Button
                    if (currentIndex < test.questions.size - 1) {
                        Button(
                            onClick = { currentIndex++ },
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("next_question_btn")
                        ) {
                            Text("Next")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    } else {
                        Button(
                            onClick = { showSubmitConfirmation = true },
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("submit_exam_btn")
                        ) {
                            Text("Submit Test", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Submit Confirmation Dialog
    if (showSubmitConfirmation) {
        val answeredCount = userAnswers.size
        val totalCount = test.questions.size
        val unattempted = totalCount - answeredCount

        AlertDialog(
            onDismissRequest = { showSubmitConfirmation = false },
            title = { Text("Submit Test Confirmation", fontWeight = FontWeight.Bold, color = MargNavy) },
            text = {
                Column {
                    Text("You have answered $answeredCount out of $totalCount questions.")
                    if (unattempted > 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Warning: $unattempted question(s) are currently unattempted.",
                            color = WarmClay,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Are you ready to submit and calculate your class rank points?")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitConfirmation = false
                        onSubmit(userAnswers.toMap(), timeTakenSeconds)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    modifier = Modifier.testTag("confirm_submit_test_btn")
                ) {
                    Text("Yes, Submit Now")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showSubmitConfirmation = false }) {
                    Text("Review Questions")
                }
            }
        )
    }

    // Exit Confirmation Dialog
    if (showExitConfirmation) {
        AlertDialog(
            onDismissRequest = { showExitConfirmation = false },
            title = { Text("Leave Ongoing Test?", fontWeight = FontWeight.Bold, color = WarmClay) },
            text = {
                Text("If you leave now, your answers will not be recorded and no points will be awarded.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExitConfirmation = false
                        onCancel()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WarmClay)
                ) {
                    Text("Leave Test")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showExitConfirmation = false }) {
                    Text("Continue Test")
                }
            }
        )
    }
}
