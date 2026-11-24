package com.example.margdarsh.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.margdarsh.ui.theme.ForestGreen
import com.example.margdarsh.ui.theme.InkTextSecondary
import com.example.margdarsh.ui.theme.MargNavy
import com.example.margdarsh.ui.theme.PaperSurface
import com.example.margdarsh.ui.theme.RoyalBlue

enum class MargdarshTab(val title: String, val testTag: String) {
    HOME("Home", "tab_home"),
    TESTS("Tests", "tab_tests"),
    RESOURCES("Resources", "tab_resources"),
    SKILLS("Skills", "tab_skills"),
    RESULTS("Dossier", "tab_results"),
    STUDENT("Student Test", "tab_student"),
    PARENT("Parent Survey", "tab_parent")
}

@Composable
fun MargdarshBottomBar(
    currentTab: MargdarshTab,
    onTabSelected: (MargdarshTab) -> Unit,
    modifier: Modifier = Modifier
) {
    // Only display primary tabs on bottom bar
    val visibleTabs = listOf(
        MargdarshTab.HOME,
        MargdarshTab.TESTS,
        MargdarshTab.RESOURCES,
        MargdarshTab.SKILLS,
        MargdarshTab.RESULTS
    )

    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        containerColor = PaperSurface,
        tonalElevation = 4.dp
    ) {
        visibleTabs.forEach { tab ->
            val isSelected = currentTab == tab
            val icon = when (tab) {
                MargdarshTab.HOME -> if (isSelected) Icons.Filled.Home else Icons.Outlined.Home
                MargdarshTab.TESTS -> if (isSelected) Icons.Filled.School else Icons.Outlined.School
                MargdarshTab.RESOURCES -> if (isSelected) Icons.Filled.AutoStories else Icons.Outlined.AutoStories
                MargdarshTab.SKILLS -> if (isSelected) Icons.Filled.Lightbulb else Icons.Outlined.Lightbulb
                MargdarshTab.RESULTS -> if (isSelected) Icons.Filled.Assessment else Icons.Outlined.Assessment
                else -> Icons.Filled.Home
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                modifier = Modifier.testTag(tab.testTag),
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = tab.title
                    )
                },
                label = { Text(tab.title, style = MaterialTheme.typography.labelSmall) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = RoyalBlue,
                    selectedTextColor = MargNavy,
                    unselectedIconColor = InkTextSecondary,
                    unselectedTextColor = InkTextSecondary,
                    indicatorColor = RoyalBlue.copy(alpha = 0.12f)
                )
            )
        }
    }
}
