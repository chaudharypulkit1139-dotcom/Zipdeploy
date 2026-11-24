package com.example.margdarsh.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.margdarsh.R
import com.example.margdarsh.ui.theme.AmberGold
import com.example.margdarsh.ui.theme.MargNavy
import com.example.margdarsh.ui.theme.PaperSurface
import com.example.margdarsh.ui.theme.RoyalBlue

/**
 * Compact MargDarsh Brand Logo for Top Bars, Dialog headers, and compact headers.
 */
@Composable
fun MargdarshLogoCompact(
    modifier: Modifier = Modifier,
    iconSize: Dp = 32.dp,
    textSize: Int = 20,
    onDarkBackground: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .then(
                if (onClick != null) Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClick() }
                    .padding(4.dp)
                else Modifier
            )
            .testTag("margdarsh_compact_logo")
    ) {
        // Official Logo Icon
        Box(
            modifier = Modifier
                .size(iconSize)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, RoyalBlue.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.margdarsh_app_icon_1789370771118),
                contentDescription = "MargDarsh Logo Emblem",
                modifier = Modifier.size(iconSize * 0.9f),
                contentScale = ContentScale.Fit
            )
        }

        // Two-Tone Wordmark: "Marg" + "Darsh"
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = if (onDarkBackground) Color.White else MargNavy,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = textSize.sp
                    )
                ) {
                    append("Marg")
                }
                withStyle(
                    SpanStyle(
                        color = if (onDarkBackground) AmberGold else RoyalBlue,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = textSize.sp
                    )
                ) {
                    append("Darsh")
                }
            },
            maxLines = 1,
            softWrap = false,
            fontFamily = FontFamily.Default
        )
    }
}

/**
 * Full MargDarsh Hero Brand Banner featuring the logo emblem, stylized wordmark,
 * amber separator bar, and the official tagline: "STREAM. ROADMAP. CAMPUS. SCHOLARSHIP."
 */
@Composable
fun MargdarshHeroBrandBanner(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("margdarsh_hero_brand_banner"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Emblem with subtle glow
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                RoyalBlue.copy(alpha = 0.08f),
                                Color.White
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.margdarsh_app_icon_1789370771118),
                    contentDescription = "MargDarsh Logo",
                    modifier = Modifier.size(86.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Stylized Wordmark: MargDarsh
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Marg",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = MargNavy,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "Darsh",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = RoyalBlue,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = AmberGold,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Signature Golden Amber Line
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(2.5.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                AmberGold.copy(alpha = 0.3f),
                                AmberGold,
                                AmberGold.copy(alpha = 0.3f)
                            )
                        )
                    )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Official Tagline
            Text(
                text = "STREAM • ROADMAP • CAMPUS • SCHOLARSHIP",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MargNavy,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
