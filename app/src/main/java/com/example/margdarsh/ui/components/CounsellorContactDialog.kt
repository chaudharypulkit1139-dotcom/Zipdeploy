package com.example.margdarsh.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.margdarsh.R
import com.example.margdarsh.data.StudentProfile
import com.example.margdarsh.ui.theme.ForestGreen
import com.example.margdarsh.ui.theme.PaperBackground
import com.example.margdarsh.ui.theme.PaperSurfaceWarm
import com.example.margdarsh.ui.theme.WarmClay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounsellorContactDialog(
    studentName: String,
    reportText: String,
    onDismiss: () -> Unit,
    onBookingSuccess: (utr: String, notes: String, profile: StudentProfile, amount: String, couponCode: String?) -> Unit = { _, _, _, _, _ -> },
    profile: StudentProfile = StudentProfile(studentName = studentName),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val counsellorName = "Dr. B.B. Panchal"
    val counsellorPhone = "+91 79098 06620"
    val counsellorEmail = "rankmakersindore@gmail.com"
    var showCounsellingPaymentDialog by remember { mutableStateOf(false) }

    // Shimmering animation for 1-on-1 Counselling button
    val infiniteTransition = rememberInfiniteTransition(label = "counsellor_contact_shimmer")
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
            counsellorPhone = counsellorPhone,
            counsellorEmail = counsellorEmail,
            onDismiss = { showCounsellingPaymentDialog = false },
            onBookingSuccess = { utr, notes, bookedProfile, amount, coupon ->
                onBookingSuccess(utr, notes, bookedProfile, amount, coupon)
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .testTag("counsellor_unlock_dialog"),
        content = {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = PaperBackground,
                border = BorderStroke(1.5.dp, WarmClay.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Top Bar with Close
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
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = WarmClay,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "RESTRICTED TIER-1 BLUEPRINT",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = WarmClay,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp).testTag("close_counsellor_dialog_button")
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Unlock Exclusive Tier-1 Career Roadmap",
                        style = MaterialTheme.typography.headlineMedium,
                        color = ForestGreen,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "This top-tier opportunity commands starting salaries from ₹18L – ₹35L+ PA (scaling to ₹90L+ PA). Because admission depends on specialized subject electives, entrance test strategy, and cutoffs, this unredacted blueprint is unlocked through a dedicated advisory consultation.",
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Counsellor Card with Photo
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = PaperSurfaceWarm),
                        border = BorderStroke(1.dp, WarmClay.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.dr_bb_panchal_counsellor_1789197215041),
                                    contentDescription = counsellorName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(ForestGreen)
                                )

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
                                            contentDescription = null,
                                            tint = ForestGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Text(
                                        text = "Senior Career Counsellor • 25+ Yrs Exp.",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = WarmClay
                                    )
                                    Text(
                                        text = "Alison Univ Certified • Bodhami Registered",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Benefit bullet points
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(16.dp).padding(top = 2.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Unlocks unredacted college cutoffs & scholarship quotas", style = MaterialTheme.typography.bodySmall)
                                }
                                Row(verticalAlignment = Alignment.Top) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(16.dp).padding(top = 2.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Prevents irreversible Class 11 stream & elective mistakes", style = MaterialTheme.typography.bodySmall)
                                }
                                Row(verticalAlignment = Alignment.Top) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(16.dp).padding(top = 2.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Direct parent-student 1-on-1 discussion on budget & mobility", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 1-on-1 Counselling Card & Booking CTA
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = PaperSurfaceWarm,
                        border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🎯 1-on-1 Master Strategy Call",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = ForestGreen
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = ForestGreen.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = "₹999 Only",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = ForestGreen,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Direct 45-minute live consultation with Dr. B.B. Panchal covering customized stream selection, parent alignment, and college roadmap.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
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
                            .testTag("book_one_on_one_counselling_dialog_button")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(shiningCounsellingGradient)
                                .padding(vertical = 13.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Book 1-on-1 Counselling (₹999)", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Secondary Action: Email Dossier
                    OutlinedButton(
                        onClick = {
                            sendEmailToCounsellor(context, counsellorEmail, studentName, reportText)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("send_email_dossier_button"),
                        shape = CircleShape,
                        border = BorderStroke(1.2.dp, ForestGreen)
                    ) {
                        Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Email Dossier to Counsellor", color = ForestGreen, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Secondary Action Buttons Row: Call & WhatsApp
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$counsellorPhone"))
                                context.startActivity(dialIntent)
                            },
                            modifier = Modifier.weight(1f).testTag("call_banty_button"),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = WarmClay,
                                contentColor = androidx.compose.ui.graphics.Color.White
                            )
                        ) {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Call Now", color = androidx.compose.ui.graphics.Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                openWhatsApp(context, "917909806620", studentName)
                            },
                            modifier = Modifier.weight(1f).testTag("whatsapp_banty_button"),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, ForestGreen),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreen)
                        ) {
                            Text("WhatsApp", fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Copy Full Report
                    OutlinedButton(
                        onClick = {
                            copyReportToClipboard(context, reportText)
                        },
                        modifier = Modifier.fillMaxWidth().testTag("copy_report_dialog_button"),
                        shape = CircleShape
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Copy Full Report to Clipboard", fontSize = 12.sp)
                    }
                }
            }
        }
    )
}

fun sendEmailToCounsellor(context: Context, counsellorEmail: String, studentName: String, reportText: String) {
    val studentDisplayName = studentName.ifBlank { "Candidate" }
    val subject = "[Margdarsh Career Dossier] $studentDisplayName - Assessment Report & Strategy Session"
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$counsellorEmail")
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, reportText)
    }
    try {
        context.startActivity(Intent.createChooser(emailIntent, "Send Dossier to Counsellor"))
    } catch (e: Exception) {
        Toast.makeText(context, "Could not open email client: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

fun openWhatsApp(context: Context, phoneFormatted: String, studentName: String) {
    val message = "Hello Dr. B.B. Panchal, I have completed the Margdarsh Career Assessment for ${studentName.ifBlank { "my child" }} and would like to schedule a 1-on-1 career strategy session."
    com.example.margdarsh.util.WhatsAppLauncher.launch(context, phoneFormatted, message)
}

fun copyReportToClipboard(context: Context, reportText: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Margdarsh Career Dossier", reportText)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Full report copied to clipboard!", Toast.LENGTH_SHORT).show()
}
