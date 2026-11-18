package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.MembershipTier
import com.example.ui.components.BidarshoPageTopBar
import com.example.ui.components.MinimalistDrawableHeader
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MistAqua
import com.example.ui.theme.SageTeal
import com.example.ui.theme.WarmCream
import com.example.ui.theme.WarmGold

@Composable
fun HomeScreen(
    userName: String,
    currentTier: MembershipTier,
    onOpenClasses: () -> Unit,
    onOpenEpisodes: () -> Unit,
    onOpenMeditation: () -> Unit,
    onOpenLearnMore: () -> Unit,
    onOpenTierUpgrade: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 620.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BidarshoPageTopBar(
                title = "سلام $userName",
                currentTier = currentTier,
                onTierClick = onOpenTierUpgrade
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MinimalistDrawableHeader(
                    drawableRes = R.drawable.img_illust_home,
                    contentDescription = "تصویرسازی مینیمال خانه بیدارشو",
                    captionTitle = "بیداری درون و روابط آگاهانه",
                    captionSubtitle = "فضای امن برای رشد فردی، شناخت دلبستگی و ساخت رابطه‌ای آرام"
                )

                // 3-section access overview strip (Public, VIP, Class Member)
                MembershipThreeSectionsCard(
                    currentTier = currentTier,
                    onSelectUpgrade = onOpenTierUpgrade
                )

                // Center 4-option Menu Title
                Text(
                    text = "بخش‌های اصلی آکادمی",
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepTeal
                )

                // 2x2 Center 4-option grid menu
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HomeMenuTile(
                            title = "کلاس‌های آنلاین",
                            subtitle = "کارگاه‌ها و اسلایدها",
                            icon = Icons.Default.School,
                            containerColor = DeepTeal,
                            contentColor = WarmCream,
                            iconBgColor = WarmGold,
                            iconTint = DeepTeal,
                            onClick = onOpenClasses,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("home_menu_classes")
                        )
                        HomeMenuTile(
                            title = "اپیزودها",
                            subtitle = "پادکست‌های روابط",
                            icon = Icons.Default.Headphones,
                            containerColor = MistAqua.copy(alpha = 0.55f),
                            contentColor = DeepTeal,
                            iconBgColor = DeepTeal,
                            iconTint = WarmGold,
                            onClick = onOpenEpisodes,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("home_menu_episodes")
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HomeMenuTile(
                            title = "تلپاتی و مدیتیشن",
                            subtitle = "آرامش و تمرین ۴-۷-۸",
                            icon = Icons.Default.SelfImprovement,
                            containerColor = WarmGold.copy(alpha = 0.55f),
                            contentColor = DeepTeal,
                            iconBgColor = SageTeal,
                            iconTint = WarmCream,
                            onClick = onOpenMeditation,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("home_menu_meditation")
                        )
                        HomeMenuTile(
                            title = "بیشتر یاد بگیر",
                            subtitle = "مقالات و خودشناسی",
                            icon = Icons.Default.MenuBook,
                            containerColor = SageTeal.copy(alpha = 0.22f),
                            contentColor = DeepTeal,
                            iconBgColor = MistAqua,
                            iconTint = DeepTeal,
                            onClick = onOpenLearnMore,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("home_menu_learn_more")
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeMenuTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    iconBgColor: Color,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(144.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(1.dp, SageTeal.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun MembershipThreeSectionsCard(
    currentTier: MembershipTier,
    onSelectUpgrade: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MistAqua.copy(alpha = 0.24f)),
        border = BorderStroke(1.dp, MistAqua),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onSelectUpgrade)
            .testTag("home_three_sections_card")
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = DeepTeal,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "سطوح سه‌گانه عضویت",
                        style = MaterialTheme.typography.titleSmall,
                        color = DeepTeal
                    )
                }
                Text(
                    text = "تغییر / ارتقای اشتراک",
                    style = MaterialTheme.typography.labelSmall,
                    color = SageTeal
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MembershipTier.entries.forEach { tier ->
                    val isCurrent = currentTier == tier
                    val hasAccess = currentTier.hasAccessTo(tier)
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        color = when {
                            isCurrent -> WarmGold
                            hasAccess -> MistAqua.copy(alpha = 0.6f)
                            else -> WarmCream
                        },
                        border = BorderStroke(
                            1.dp,
                            if (isCurrent) DeepTeal else SageTeal.copy(alpha = 0.35f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = when (tier) {
                                    MembershipTier.PUBLIC -> "عمومی"
                                    MembershipTier.VIP -> "اعضای ویژه"
                                    MembershipTier.CLASS_MEMBER -> "اعضای کلاس"
                                },
                                style = MaterialTheme.typography.labelMedium,
                                color = DeepTeal,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = when {
                                    isCurrent -> "فعال"
                                    hasAccess -> "باز"
                                    else -> "اشتراکی"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = SageTeal
                            )
                        }
                    }
                }
            }
        }
    }
}
