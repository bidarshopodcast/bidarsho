package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.BidarshoCatalog
import com.example.model.EpisodeItem
import com.example.model.MembershipTier
import com.example.ui.components.BidarshoPageTopBar
import com.example.ui.components.MinimalistDrawableHeader
import com.example.ui.components.TierBadge
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MistAqua
import com.example.ui.theme.SageTeal
import com.example.ui.theme.WarmCream
import com.example.ui.theme.WarmGold
import kotlinx.coroutines.delay

@Composable
fun EpisodesScreen(
    currentTier: MembershipTier,
    onBack: () -> Unit,
    onRequestUpgrade: (MembershipTier) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf<MembershipTier?>(null) }
    var playingEpisodeId by remember { mutableStateOf<String?>(null) }
    var expandedEpisodeId by remember { mutableStateOf<String?>(null) }
    var playbackProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(playingEpisodeId) {
        if (playingEpisodeId != null) {
            playbackProgress = 0.05f
            while (playingEpisodeId != null && playbackProgress < 1f) {
                delay(400L)
                playbackProgress = (playbackProgress + 0.02f).coerceAtMost(1f)
            }
            if (playbackProgress >= 1f) {
                playingEpisodeId = null
            }
        }
    }

    val filteredEpisodes = remember(selectedFilter) {
        if (selectedFilter == null) BidarshoCatalog.episodes
        else BidarshoCatalog.episodes.filter { it.requiredTier == selectedFilter }
    }

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
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            BidarshoPageTopBar(
                title = "اپیزودهای صوتی و تحلیلی",
                currentTier = currentTier,
                onBackClick = onBack,
                onTierClick = { onRequestUpgrade(MembershipTier.VIP) }
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MinimalistDrawableHeader(
                    drawableRes = R.drawable.img_illust_episodes,
                    contentDescription = "تصویرسازی مینیمال اپیزودها",
                    captionTitle = "پادکست‌های تخصصی روانشناسی روابط",
                    captionSubtitle = "اپیزودهای رایگان برای بخش عمومی و تحلیل‌های عمیق ویژه اعضای VIP"
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    EpisodeFilterChip(
                        label = "همه اپیزودها",
                        selected = selectedFilter == null,
                        onClick = { selectedFilter = null }
                    )
                    EpisodeFilterChip(
                        label = "بخش عمومی",
                        selected = selectedFilter == MembershipTier.PUBLIC,
                        onClick = { selectedFilter = MembershipTier.PUBLIC }
                    )
                    EpisodeFilterChip(
                        label = "اعضای ویژه VIP",
                        selected = selectedFilter == MembershipTier.VIP,
                        onClick = { selectedFilter = MembershipTier.VIP }
                    )
                }

                filteredEpisodes.forEach { episode ->
                    val hasAccess = currentTier.hasAccessTo(episode.requiredTier)
                    val isPlaying = playingEpisodeId == episode.id
                    val isExpanded = expandedEpisodeId == episode.id

                    EpisodeCard(
                        episode = episode,
                        hasAccess = hasAccess,
                        isPlaying = isPlaying,
                        isExpanded = isExpanded,
                        progress = if (isPlaying) playbackProgress else 0f,
                        onTogglePlay = {
                            if (!hasAccess) {
                                onRequestUpgrade(episode.requiredTier)
                            } else {
                                playingEpisodeId = if (isPlaying) null else episode.id
                                expandedEpisodeId = episode.id
                            }
                        },
                        onToggleExpand = {
                            if (!hasAccess) {
                                onRequestUpgrade(episode.requiredTier)
                            } else {
                                expandedEpisodeId = if (isExpanded) null else episode.id
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EpisodeFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) WarmCream else DeepTeal
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = DeepTeal,
            containerColor = MistAqua.copy(alpha = 0.35f)
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = SageTeal.copy(alpha = 0.4f)
        )
    )
}

@Composable
private fun EpisodeCard(
    episode: EpisodeItem,
    hasAccess: Boolean,
    isPlaying: Boolean,
    isExpanded: Boolean,
    progress: Float,
    onTogglePlay: () -> Unit,
    onToggleExpand: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onToggleExpand)
            .testTag("episode_card_${episode.id}"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (hasAccess) MistAqua.copy(alpha = 0.28f) else WarmGold.copy(alpha = 0.2f)
        ),
        border = BorderStroke(
            1.dp,
            if (isPlaying) DeepTeal else SageTeal.copy(alpha = 0.35f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = WarmCream,
                        border = BorderStroke(1.dp, MistAqua)
                    ) {
                        Text(
                            text = "${episode.category} • ${episode.durationLabel}",
                            style = MaterialTheme.typography.labelSmall,
                            color = SageTeal,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                TierBadge(tier = episode.requiredTier)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (hasAccess) DeepTeal else WarmGold)
                        .clickable(onClick = onTogglePlay)
                        .testTag("episode_play_${episode.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when {
                            !hasAccess -> Icons.Default.Lock
                            isPlaying -> Icons.Default.Pause
                            else -> Icons.Default.PlayArrow
                        },
                        contentDescription = if (hasAccess) "پخش اپیزود" else "قفل VIP",
                        tint = if (hasAccess) WarmGold else DeepTeal
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = episode.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = DeepTeal
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = episode.summary,
                        style = MaterialTheme.typography.bodySmall,
                        color = SageTeal
                    )
                }
            }

            if (isPlaying) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(50)),
                    color = DeepTeal,
                    trackColor = MistAqua
                )
            }

            AnimatedVisibility(visible = isExpanded && hasAccess) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(WarmCream)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = null,
                            tint = SageTeal,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "خلاصه و نکات کلیدی اپیزود",
                            style = MaterialTheme.typography.labelLarge,
                            color = DeepTeal
                        )
                    }
                    Text(
                        text = episode.fullNotes,
                        style = MaterialTheme.typography.bodyMedium,
                        color = DeepTeal
                    )
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = WarmGold.copy(alpha = 0.4f)
                    ) {
                        Text(
                            text = "گام عملی: ${episode.actionStep}",
                            style = MaterialTheme.typography.bodySmall,
                            color = DeepTeal,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }

            if (!hasAccess) {
                Button(
                    onClick = onTogglePlay,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepTeal,
                        contentColor = WarmCream
                    )
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("باز کردن با اشتراک اعضای ویژه VIP")
                }
            }
        }
    }
}
