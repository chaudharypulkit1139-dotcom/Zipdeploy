package com.example.margdarsh.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.margdarsh.data.SkillItem
import com.example.margdarsh.data.TOP_10_SKILLS
import com.example.margdarsh.ui.theme.AmberGold
import com.example.margdarsh.ui.theme.CyanBg
import com.example.margdarsh.ui.theme.CyanVibrant
import com.example.margdarsh.ui.theme.EmeraldBg
import com.example.margdarsh.ui.theme.EmeraldVibrant
import com.example.margdarsh.ui.theme.ForestGreen
import com.example.margdarsh.ui.theme.IndigoBg
import com.example.margdarsh.ui.theme.IndigoVibrant
import com.example.margdarsh.ui.theme.InkTextPrimary
import com.example.margdarsh.ui.theme.InkTextSecondary
import com.example.margdarsh.ui.theme.MargNavy
import com.example.margdarsh.ui.theme.MargNavyDark
import com.example.margdarsh.ui.theme.PaperBackground
import com.example.margdarsh.ui.theme.PaperSurface
import com.example.margdarsh.ui.theme.PaperSurfaceWarm
import com.example.margdarsh.ui.theme.PurpleBg
import com.example.margdarsh.ui.theme.PurpleVibrant
import com.example.margdarsh.ui.theme.RoyalBlue
import com.example.margdarsh.ui.theme.WarmClay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillDevelopmentScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedSkill by remember { mutableStateOf<SkillItem?>(null) }
    var selectedCategoryFilter by remember { mutableStateOf("All") }

    val categories = listOf("All", "Tech & AI", "Finance & Business", "Leadership & Influence", "Analytics & Tech", "Software & Engineering")

    val filteredSkills = remember(selectedCategoryFilter) {
        if (selectedCategoryFilter == "All") {
            TOP_10_SKILLS
        } else {
            TOP_10_SKILLS.filter { it.category.contains(selectedCategoryFilter, ignoreCase = true) }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PaperBackground),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // High-Converting Header Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("skills_hub_header"),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = MargNavyDark),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(MargNavyDark, MargNavy, Color(0xFF065F46))
                            )
                        )
                        .padding(22.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AmberGold
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = MargNavyDark, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "2026–2030 FUTURE-PROOF SKILLS RADAR",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MargNavyDark,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Top 10 High-Income Career Accelerators",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 21.sp,
                            lineHeight = 26.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Degrees qualify candidates for entry-level interviews, but specific cognitive and technical skills create top 1% earning power. Master these 10 industry-benchmarked competencies with structured action blueprints.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.85f),
                            lineHeight = 19.sp,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

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
                                    Text("+45% to +80%", color = AmberGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Salary Premium", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
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
                                    Text("Free Curricula", color = AmberGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Top Global Courses", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategoryFilter == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategoryFilter = cat },
                        label = {
                            Text(
                                text = cat,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MargNavy else InkTextSecondary
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White,
                            selectedContainerColor = Color.White,
                            labelColor = InkTextSecondary,
                            selectedLabelColor = MargNavy
                        ),
                        border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, if (isSelected) MargNavy else Color.LightGray.copy(alpha = 0.6f)),
                        shape = CircleShape
                    )
                }
            }
        }

        // Skills List
        itemsIndexed(filteredSkills) { index, skill ->
            SkillItemCard(
                skill = skill,
                index = index + 1,
                onViewRoadmap = { selectedSkill = skill }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Modal Dialog: Detailed Skill Action Blueprint
    if (selectedSkill != null) {
        val sk = selectedSkill!!
        AlertDialog(
            onDismissRequest = { selectedSkill = null },
            confirmButton = {
                Button(
                    onClick = { selectedSkill = null },
                    colors = ButtonDefaults.buttonColors(containerColor = MargNavy, contentColor = Color.White)
                ) {
                    Text("Done", color = Color.White)
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = AmberGold, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = sk.title,
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
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("⏳ Timeline: ${sk.timeToLearn}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MargNavy)
                                Text("🎯 ${sk.highIncomeRole}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ForestGreen)
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Why This Skill Matters in 2026-2030:",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelMedium,
                            color = WarmClay
                        )
                        Text(sk.whyImportant, style = MaterialTheme.typography.bodySmall, lineHeight = 16.sp)
                    }

                    item {
                        Text(
                            text = "Step-by-Step Action Roadmap:",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelMedium,
                            color = MargNavy
                        )
                    }

                    items(sk.actionSteps) { step ->
                        Row(verticalAlignment = Alignment.Top) {
                            Text("✓ ", color = ForestGreen, fontWeight = FontWeight.Bold)
                            Text(step, style = MaterialTheme.typography.bodySmall, color = InkTextPrimary, lineHeight = 15.sp)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Recommended Industry Tools:",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelMedium,
                            color = RoyalBlue
                        )
                        Text(sk.recommendedTools.joinToString(" • "), style = MaterialTheme.typography.bodySmall, color = InkTextPrimary)
                    }

                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Top Free & Verified Learning Platforms:",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelMedium,
                            color = ForestGreen
                        )
                        Text(sk.freeResources.joinToString(" • "), style = MaterialTheme.typography.bodySmall, color = InkTextPrimary)
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SkillItemCard(
    skill: SkillItem,
    index: Int,
    onViewRoadmap: () -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("skill_item_${skill.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PaperSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MargNavy
                ) {
                    Text(
                        text = "SKILL $index",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = PaperSurfaceWarm
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = WarmClay, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = skill.timeToLearn,
                            style = MaterialTheme.typography.labelSmall,
                            color = WarmClay,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = skill.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MargNavy,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(3.dp))

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = EmeraldBg
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = EmeraldVibrant, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "High-Income Target: ${skill.highIncomeRole}",
                        style = MaterialTheme.typography.labelSmall,
                        color = EmeraldVibrant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = skill.summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 17.sp,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Expandable Action Roadmap
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
                                text = if (isExpanded) "Hide Action Steps" else "View Step-by-Step Action Roadmap",
                                style = MaterialTheme.typography.labelMedium,
                                color = MargNavy,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(8.dp))
                        skill.actionSteps.forEachIndexed { idx, step ->
                            Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.Top) {
                                Text("${idx + 1}. ", color = ForestGreen, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text(step, style = MaterialTheme.typography.bodySmall, fontSize = 11.sp, color = InkTextPrimary)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tools: ${skill.recommendedTools.joinToString(", ")}",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onViewRoadmap,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).height(40.dp)
                ) {
                    Text("Full Blueprint", fontSize = 12.sp, color = MargNavy, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        val shareText = "🚀 *MargDarsh Career Skill Blueprint: ${skill.title}*\n\nTarget Role: ${skill.highIncomeRole}\nTimeline: ${skill.timeToLearn}\n\nKey Steps:\n" +
                                skill.actionSteps.mapIndexed { idx, s -> "${idx + 1}. $s" }.joinToString("\n")
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Share Skill Roadmap"))
                    },
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MargNavy)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Share Plan", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
