package com.example.margdarsh.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.margdarsh.R
import com.example.margdarsh.data.StudentProfile
import com.example.margdarsh.ui.theme.ForestGreen
import com.example.margdarsh.ui.theme.PaperBackground
import com.example.margdarsh.ui.theme.PaperSurfaceWarm
import com.example.margdarsh.ui.theme.WarmClay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounsellorReminderDialog(
    profile: StudentProfile,
    onDismiss: () -> Unit,
    onBookingSuccess: (utr: String, notes: String, profile: StudentProfile, amount: String, couponCode: String?) -> Unit = { _, _, _, _, _ -> },
    onBookingSuccessWithScreenshot: (utr: String, notes: String, profile: StudentProfile, amount: String, couponCode: String?, screenshotUri: Uri?) -> Unit = { utr, n, p, a, c, _ -> onBookingSuccess(utr, n, p, a, c) },
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val counsellorName = "Dr. B.B. Panchal"
    val counsellorPhone = "+917909806620"
    val counsellorDisplayPhone = "+91 79098 06620"
    val counsellorEmail = "rankmakersindore@gmail.com"
    val scrollState = rememberScrollState()
    var showCounsellingPaymentDialog by remember { mutableStateOf(false) }

    // Shimmering animated gradient for 1-on-1 Counselling button
    val infiniteTransition = rememberInfiniteTransition(label = "reminder_counselling_shimmer")
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
        CounsellingPaymentDialog(
            profile = profile,
            counsellorPhone = counsellorDisplayPhone,
            counsellorEmail = counsellorEmail,
            onDismiss = { showCounsellingPaymentDialog = false },
            onBookingSuccess = { utr, notes, bookedProfile, amount, coupon ->
                onBookingSuccess(utr, notes, bookedProfile, amount, coupon)
            },
            onBookingSuccessWithScreenshot = { utr, notes, bookedProfile, amount, coupon, uri ->
                onBookingSuccessWithScreenshot(utr, notes, bookedProfile, amount, coupon, uri)
            }
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .systemBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(26.dp),
                color = PaperBackground,
                border = BorderStroke(1.5.dp, WarmClay.copy(alpha = 0.6f)),
                shadowElevation = 12.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp)
                    .fillMaxHeight(0.88f)
                    .testTag("counsellor_reminder_dialog")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(22.dp)
                ) {
                    // Header Badge & Close Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = WarmClay.copy(alpha = 0.15f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = WarmClay,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "CAREER ADVISORY NOTICE",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = WarmClay,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("close_reminder_dialog_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dismiss",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Secure Your Child's Future with Professional Career Guidance",
                        style = MaterialTheme.typography.headlineMedium,
                        color = ForestGreen,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val studentDisplayName = if (profile.studentName.isNotBlank()) profile.studentName else "Student"
                    Text(
                        text = "Hello $studentDisplayName, marks alone only show what you've memorized—not which careers you will excel in. Without strategic subject and entrance planning, students risk settling for lower-paying streams or missing out on Tier-1 colleges.",
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Counselor Card with Photo
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = PaperSurfaceWarm),
                        border = BorderStroke(1.dp, WarmClay.copy(alpha = 0.35f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
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
                                        text = "Senior Career Counsellor • Bodhami Registered",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Director System & Planning - EduTech Classes",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = WarmClay,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "25+ Years Experience (Alison Univ U.K. Certified)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Speak with Dr. Panchal today to review your psychometric test results, select stream combinations, and build your roadmap to high-paying careers.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // 1-on-1 Private Consultation Badge Box
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.4f)),
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
                                            text = "🎯 1-on-1 Private Mentorship",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = ForestGreen
                                        )
                                        Text(
                                            text = "45-Min Live Career & Stream Diagnostic with Parent Q&A",
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
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Primary 1-on-1 Counselling Action Button (Shining Animated Gradient)
                    Surface(
                        shape = CircleShape,
                        color = ForestGreen,
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showCounsellingPaymentDialog = true
                            }
                            .testTag("book_one_on_one_counselling_button")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(shiningCounsellingGradient)
                                .padding(vertical = 14.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Book 1-on-1 Counselling (₹999)",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$counsellorPhone"))
                                context.startActivity(callIntent)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("call_counsellor_reminder_button"),
                            shape = CircleShape,
                            border = BorderStroke(1.5.dp, ForestGreen)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null,
                                tint = ForestGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Call Directly",
                                color = ForestGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                val mailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:$counsellorEmail")
                                    putExtra(Intent.EXTRA_SUBJECT, "Margdarsh 1-on-1 Career Counselling - ${profile.studentName}")
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Respected Dr. Panchal,\n\nI am ${profile.studentName} from ${profile.city} (Class: ${profile.studentClass}). I want to schedule a 1-on-1 Career Counselling Session (₹999).\nPhone: ${profile.phoneNumber}\n\nThank you."
                                    )
                                }
                                context.startActivity(mailIntent)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("email_counsellor_reminder_button"),
                            shape = CircleShape,
                            border = BorderStroke(1.5.dp, WarmClay)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = WarmClay,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Email Inquiry",
                                color = WarmClay,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
