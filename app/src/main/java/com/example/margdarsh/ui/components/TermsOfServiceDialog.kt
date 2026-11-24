package com.example.margdarsh.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.margdarsh.ui.theme.ForestGreen
import com.example.margdarsh.ui.theme.MargNavy
import com.example.margdarsh.ui.theme.PaperBackground
import com.example.margdarsh.ui.theme.PaperSurfaceWarm
import com.example.margdarsh.ui.theme.WarmClay

@Composable
fun TermsOfServiceDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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
                border = BorderStroke(1.5.dp, ForestGreen.copy(alpha = 0.6f)),
                shadowElevation = 12.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp)
                    .fillMaxHeight(0.92f)
                    .testTag("terms_of_service_dialog")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    // Header Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = ForestGreen.copy(alpha = 0.12f),
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Description,
                                        contentDescription = null,
                                        tint = ForestGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Terms of Service",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MargNavy
                                )
                                Text(
                                    text = "MargDarsh • Google Play Published",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("close_terms_of_service_btn")
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Scrollable content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(scrollState)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ForestGreen.copy(alpha = 0.08f),
                            border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.25f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "DPDP Act, IT Act 2000 & Google Play Compliant",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreen
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Last Updated: October 2026 • In partnership with Rankmakers",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        TermsSectionHeader("1. Acceptance & Minor Consent")
                        Text(
                            text = "MargDarsh is designed for school students (Classes 8–12) and their families. Use of this application by minors requires the active supervision, guidance, and consent of a parent or legal guardian.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TermsSectionHeader("2. Core Features & Services")
                        TermsBullet("Psychometric Testing", "Scientific 30-question evaluation measuring 7 cognitive and personality trait dimensions.")
                        TermsBullet("Parent Survey", "Collaborative 8-point preference survey capturing budget allocation and relocation mobility.")
                        TermsBullet("Stream Fit Advice", "Curated stream recommendations (Science PCM/PCB, Commerce, Humanities) for CBSE, ICSE, and State Boards.")
                        TermsBullet("Diagnostic Dossiers", "Automated generation of 35-page and comprehensive 65-page PDF career reports.")
                        TermsBullet("1-on-1 Counselling", "Scheduled virtual sessions with certified advisors including Dr. B.B. Panchal.")

                        Spacer(modifier = Modifier.height(12.dp))

                        TermsSectionHeader("3. Fees, Payments & Refunds")
                        TermsBullet("Free Tier", "Complimentary psychometric testing, parent survey, initial stream fit, and career insights.")
                        TermsBullet("Premium Services", "Comprehensive 65-page dossiers (₹499), 1-on-1 virtual counselling (₹999), and subject handbooks (₹18).")
                        TermsBullet("Payment Method", "UPI transfer/QR (HDFC VPA) with UTR number or receipt verification.")
                        TermsBullet("Refund Policy", "Instant digital reports/downloads are non-refundable once generated. 1-on-1 counselling sessions may be rescheduled or cancelled with full refund up to 24 hours prior to appointment.")

                        Spacer(modifier = Modifier.height(12.dp))

                        TermsSectionHeader("4. Educational & Advisory Disclaimer")
                        Text(
                            text = "Psychometric assessments and career guidance reports are advisory decision-support tools. MargDarsh and Rankmakers do not guarantee admission into specific colleges/institutes (e.g., IITs, AIIMS, IIMs) or employment/salary packages.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TermsSectionHeader("5. Intellectual Property & Conduct")
                        Text(
                            text = "All question banks, scoring logic, dossier layouts, and handbooks are proprietary property of MargDarsh and Rankmakers. Users agree not to submit fraudulent transaction details or redistribute proprietary materials commercially.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        TermsSectionHeader("6. Jurisdiction & Dispute Resolution")
                        Text(
                            text = "These terms are governed by the laws of India. Any legal dispute shall be subject to the exclusive jurisdiction of the competent courts in Indore, Madhya Pradesh, India.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Grievance Officer Card
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = PaperSurfaceWarm),
                            border = BorderStroke(1.dp, WarmClay.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Grievance Redressal & Help", fontWeight = FontWeight.Bold, fontSize = 12.5.sp, color = MargNavy)
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Email, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("rankmakersindore@gmail.com", fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurface)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(15.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("+91 79098 06620 (Indore, M.P.)", fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurface)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("accept_terms_of_service_btn")
                        ) {
                            Text("I Agree & Accept", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TermsSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 13.5.sp,
        fontWeight = FontWeight.Bold,
        color = MargNavy,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun TermsBullet(boldPrefix: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        Text("• ", fontSize = 12.sp, color = ForestGreen, fontWeight = FontWeight.Bold)
        Text(
            text = "$boldPrefix: $desc",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 16.5.sp
        )
    }
}
