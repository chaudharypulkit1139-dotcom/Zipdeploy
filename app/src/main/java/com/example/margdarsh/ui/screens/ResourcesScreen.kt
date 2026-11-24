package com.example.margdarsh.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.margdarsh.data.LOCKED_PARENT_RESOURCES
import com.example.margdarsh.data.ResourceMaterial
import com.example.margdarsh.ui.components.MargdarshLogoCompact
import com.example.margdarsh.ui.components.ResourcePaymentDialog
import com.example.margdarsh.ui.theme.AmberGold
import com.example.margdarsh.ui.theme.AmberVibrant
import com.example.margdarsh.ui.theme.CyanBg
import com.example.margdarsh.ui.theme.CyanVibrant
import com.example.margdarsh.ui.theme.EmeraldBg
import com.example.margdarsh.ui.theme.EmeraldVibrant
import com.example.margdarsh.ui.theme.ForestGreen
import com.example.margdarsh.ui.theme.IndigoBg
import com.example.margdarsh.ui.theme.IndigoVibrant
import com.example.margdarsh.ui.theme.InkTextPrimary
import com.example.margdarsh.ui.theme.MargNavy
import com.example.margdarsh.ui.theme.MargNavyDark
import com.example.margdarsh.ui.theme.PaperBackground
import com.example.margdarsh.ui.theme.PaperSurface
import com.example.margdarsh.ui.theme.PaperSurfaceWarm
import com.example.margdarsh.ui.theme.PurpleBg
import com.example.margdarsh.ui.theme.PurpleVibrant
import com.example.margdarsh.ui.theme.RoyalBlue
import com.example.margdarsh.ui.theme.WarmClay
import com.example.margdarsh.util.MargdarshPdfDownloader
import com.example.margdarsh.viewmodel.MargdarshUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ResourcesScreen(
    uiState: MargdarshUiState,
    onUnlockResources: (utr: String, amount: String, title: String) -> Unit = { _, _, _ -> },
    onUnlockSingleResource: (resourceId: String, resourceTitle: String, utr: String) -> Unit = { _, _, _ -> },
    onUnlockResourcesWithScreenshot: (utr: String, amount: String, title: String, screenshotUri: Uri?) -> Unit = { utr, amt, title, _ -> onUnlockResources(utr, amt, title) },
    onUnlockSingleResourceWithScreenshot: (resourceId: String, resourceTitle: String, utr: String, screenshotUri: Uri?) -> Unit = { id, title, utr, _ -> onUnlockSingleResource(id, title, utr) },
    onUpdateUpiId: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val studentName = uiState.studentProfile.studentName.ifBlank { "Candidate" }

    var showPaymentDialog by remember { mutableStateOf(false) }
    var paymentTargetResource by remember { mutableStateOf<ResourceMaterial?>(null) }
    var previewResource by remember { mutableStateOf<ResourceMaterial?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PaperBackground),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Vault Hero Showcase Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("parent_resource_vault_header"),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = MargNavyDark),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(MargNavyDark, MargNavy, Color(0xFF1E3A8A))
                            )
                        )
                        .padding(22.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Top Brand Row: Logo on left with full space, Bundle Badge on right
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            MargdarshLogoCompact(
                                iconSize = 26.dp,
                                textSize = 16,
                                onDarkBackground = true
                            )

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (uiState.isResourcesUnlocked) ForestGreen else AmberGold
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Icon(
                                        imageVector = if (uiState.isResourcesUnlocked) Icons.Default.CheckCircle else Icons.Default.WorkspacePremium,
                                        contentDescription = null,
                                        tint = if (uiState.isResourcesUnlocked) Color.White else MargNavyDark,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (uiState.isResourcesUnlocked) "ALL UNLOCKED" else "₹70 ALL BUNDLE",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (uiState.isResourcesUnlocked) Color.White else MargNavyDark,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "4 Comprehensive Strategic Career Handbooks",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            lineHeight = 26.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Exhaustive publication-grade PDF and presentation manuals with 297+ curated pages: 100+ careers, 75+ commerce degrees, stream-neutral pathways, science tech blueprints & tier-1 cutoff benchmarks.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.85f),
                            lineHeight = 18.sp,
                            fontSize = 12.5.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.12f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("4 Handbooks", color = AmberGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Real Publications", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.12f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("297+ Pages", color = AmberGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Full Directories", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.12f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("₹70 Bundle", color = AmberGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("₹18 Each", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (uiState.isResourcesUnlocked) {
                            Button(
                                onClick = {
                                    MargdarshPdfDownloader.downloadAllGuides(
                                        context = context,
                                        resources = LOCKED_PARENT_RESOURCES,
                                        studentName = studentName,
                                        utr = uiState.enteredUtrCode.ifBlank { "PAID_VERIFIED" }
                                    )
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ForestGreen,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("download_all_guides_btn")
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Download All 4 Publications to Device", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        } else {
                            Button(
                                onClick = {
                                    paymentTargetResource = null
                                    showPaymentDialog = true
                                },
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AmberGold,
                                    contentColor = MargNavyDark
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("unlock_all_guides_rs1_btn")
                            ) {
                                Icon(Icons.Default.LockOpen, contentDescription = null, tint = MargNavyDark, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Unlock Complete Handbooks (₹70 All Bundle)", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = MargNavyDark)
                            }
                        }
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Official Publications & Directories",
                    style = MaterialTheme.typography.titleLarge,
                    color = MargNavy,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = CircleShape,
                    color = if (uiState.isResourcesUnlocked) ForestGreen.copy(alpha = 0.12f) else RoyalBlue.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = if (uiState.isResourcesUnlocked) "4 Unlocked" else "₹18 Each",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (uiState.isResourcesUnlocked) ForestGreen else RoyalBlue,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // 4 COMPREHENSIVE REAL RESOURCE CARDS
        items(LOCKED_PARENT_RESOURCES) { res ->
            val isUnlocked = uiState.isResourcesUnlocked || uiState.unlockedResourceIds.contains(res.id)
            RealHandbookCard(
                resource = res,
                studentName = studentName,
                isUnlocked = isUnlocked,
                onUnlockClick = {
                    paymentTargetResource = res
                    showPaymentDialog = true
                },
                onPreviewClick = {
                    previewResource = res
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    // RESOURCE PAYMENT DIALOG (₹70 FOR COMBINED BUNDLE • ₹18 FOR SINGLE GUIDE)
    if (showPaymentDialog) {
        val target = paymentTargetResource
        val isBundle = target == null
        val title = if (isBundle) "All 4 Strategic Career Handbooks (Complete Bundle)" else (target?.title ?: "Strategic Career Handbook")
        val amount = if (isBundle) "70.00" else "18.00"
        ResourcePaymentDialog(
            initialUpiId = uiState.personalUpiId,
            resourceTitle = title,
            amountInr = amount,
            onDismiss = {
                showPaymentDialog = false
                paymentTargetResource = null
            },
            onPaymentSuccess = { utr, upiUsed ->
                showPaymentDialog = false
                if (target != null) {
                    onUnlockSingleResource(target.id, target.title, utr)
                    MargdarshPdfDownloader.generateAndDownloadPdf(
                        context = context,
                        resource = target,
                        studentName = studentName,
                        utr = utr
                    )
                } else {
                    onUnlockResources(utr, "70", "All Strategic Career Dossiers")
                    MargdarshPdfDownloader.downloadAllGuides(
                        context = context,
                        resources = LOCKED_PARENT_RESOURCES,
                        studentName = studentName,
                        utr = utr
                    )
                }
                paymentTargetResource = null
            },
            onPaymentSuccessWithScreenshot = { utr, upiUsed, screenshotUri ->
                showPaymentDialog = false
                if (target != null) {
                    onUnlockSingleResourceWithScreenshot(target.id, target.title, utr, screenshotUri)
                    onUnlockSingleResource(target.id, target.title, utr)
                    MargdarshPdfDownloader.generateAndDownloadPdf(
                        context = context,
                        resource = target,
                        studentName = studentName,
                        utr = utr
                    )
                } else {
                    onUnlockResourcesWithScreenshot(utr, "70", "All Strategic Career Dossiers", screenshotUri)
                    onUnlockResources(utr, "70", "All Strategic Career Dossiers")
                    MargdarshPdfDownloader.downloadAllGuides(
                        context = context,
                        resources = LOCKED_PARENT_RESOURCES,
                        studentName = studentName,
                        utr = utr
                    )
                }
                paymentTargetResource = null
            },
            onUpdateUpiId = onUpdateUpiId
        )
    }

    // DETAILED PREVIEW DIALOG
    if (previewResource != null) {
        val res = previewResource!!
        val isUnlocked = uiState.isResourcesUnlocked || uiState.unlockedResourceIds.contains(res.id)
        AlertDialog(
            onDismissRequest = { previewResource = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = MargNavy, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = res.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MargNavy
                    )
                }
            },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PaperSurfaceWarm
                        ) {
                            Text(
                                text = "${res.pagesCount} Pages • ${res.fileSizeMb} • Format: ${res.fileType} • ${res.category}",
                                style = MaterialTheme.typography.labelSmall,
                                color = InkTextPrimary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    item {
                        Text(
                            text = res.detailedSummary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = InkTextPrimary,
                            lineHeight = 20.sp
                        )
                    }

                    item {
                        Text(
                            text = "Table of Contents & Core Modules:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MargNavy
                        )
                    }

                    items(res.sampleChapters) { ch ->
                        Row(verticalAlignment = Alignment.Top) {
                            Text("✓ ", color = ForestGreen, fontWeight = FontWeight.Bold)
                            Text(ch, style = MaterialTheme.typography.bodySmall, color = InkTextPrimary)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Target Audience: ${res.targetAudience}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
            },
            confirmButton = {
                if (isUnlocked) {
                    Button(
                        onClick = {
                            MargdarshPdfDownloader.generateAndDownloadPdf(
                                context = context,
                                resource = res,
                                studentName = studentName,
                                utr = "PAID_VERIFIED"
                            )
                            previewResource = null
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForestGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Download Real ${res.fileType}", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = {
                            previewResource = null
                            paymentTargetResource = res
                            showPaymentDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RoyalBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.LockOpen, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Unlock for ₹18 Only", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { previewResource = null }) {
                    Text("Close")
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RealHandbookCard(
    resource: ResourceMaterial,
    studentName: String,
    isUnlocked: Boolean,
    onUnlockClick: () -> Unit,
    onPreviewClick: () -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }

    // Theme palette per handbook category
    val (headerGradient, badgeColor) = when (resource.id) {
        "careers_after_school" -> listOf(Color(0xFF1E3A8A), Color(0xFF2563EB)) to AmberGold
        "after_12th_commerce" -> listOf(Color(0xFF065F46), Color(0xFF059669)) to Color(0xFFA7F3D0)
        "neutral_career_options" -> listOf(Color(0xFF581C87), Color(0xFF7C3AED)) to Color(0xFFDDD6FE)
        else -> listOf(Color(0xFF0F766E), Color(0xFF0D9488)) to Color(0xFF99F6E4)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("resource_item_${resource.id}"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = PaperSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            1.2.dp,
            if (isUnlocked) ForestGreen.copy(alpha = 0.5f) else Color.LightGray.copy(alpha = 0.4f)
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Distinct Top Header Strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(headerGradient))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = resource.highlightBadge,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp
                    )

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "${resource.pagesCount} PAGES • ${resource.fileSizeMb} ${resource.fileType}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = resource.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MargNavy,
                            lineHeight = 22.sp,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = resource.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Surface(
                        shape = CircleShape,
                        color = if (isUnlocked) ForestGreen.copy(alpha = 0.15f) else AmberGold.copy(alpha = 0.25f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (isUnlocked) Icons.Default.Verified else Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (isUnlocked) ForestGreen else MargNavyDark,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isUnlocked) "UNLOCKED" else "₹18 ONLY",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isUnlocked) ForestGreen else MargNavyDark,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Chapter previews flow pills
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    resource.sampleChapters.take(3).forEach { ch ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PaperSurfaceWarm,
                            border = BorderStroke(1.dp, WarmClay.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "📖 $ch",
                                style = MaterialTheme.typography.labelSmall,
                                color = InkTextPrimary,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Expandable Chapter Outline Button
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = PaperSurfaceWarm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isExpanded = !isExpanded }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = MargNavy,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isExpanded) "Hide Full Index & Highlights" else "View Table of Contents & Key Chapters",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MargNavy,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = resource.detailedSummary,
                                style = MaterialTheme.typography.bodySmall,
                                color = InkTextPrimary,
                                lineHeight = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "CORE SECTIONS INCLUDED:",
                                style = MaterialTheme.typography.labelSmall,
                                color = ForestGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            resource.sampleChapters.forEach { chapter ->
                                Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.Top) {
                                    Text("• ", color = ForestGreen, fontWeight = FontWeight.Bold)
                                    Text(chapter, style = MaterialTheme.typography.bodySmall, fontSize = 11.sp, color = InkTextPrimary)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Actions: Download Real Asset or Unlock
                if (isUnlocked) {
                    Button(
                        onClick = {
                            MargdarshPdfDownloader.generateAndDownloadPdf(
                                context = context,
                                resource = resource,
                                studentName = studentName,
                                utr = "PAID_VERIFIED"
                            )
                        },
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("download_pdf_${resource.id}"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForestGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(17.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Download Real ${resource.fileType} (${resource.fileSizeMb})",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onPreviewClick,
                            shape = CircleShape,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Preview", fontSize = 12.sp, color = MargNavy, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onUnlockClick,
                            shape = CircleShape,
                            modifier = Modifier
                                .weight(2f)
                                .height(46.dp)
                                .testTag("unlock_pdf_${resource.id}"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RoyalBlue,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(Icons.Default.LockOpen, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Unlock for ₹18 Only",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
