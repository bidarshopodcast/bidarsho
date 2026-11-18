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
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.BidarshoCatalog
import com.example.model.BreathingState
import com.example.model.MeditationSession
import com.example.model.MembershipTier
import com.example.ui.components.BidarshoPageTopBar
import com.example.ui.components.Breathing478Card
import com.example.ui.components.TierBadge
import com.example.ui.components.toPersianDigits
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MistAqua
import com.example.ui.theme.SageTeal
import com.example.ui.theme.WarmCream
import com.example.ui.theme.WarmGold

@Composable
fun MeditationScreen(
    currentTier: MembershipTier,
    breathingState: BreathingState,
    onToggleBreathing: () -> Unit,
    onBack: () -> Unit,
    onRequestUpgrade: (MembershipTier) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedSessionId by remember { mutableStateOf<String?>("med_1") }

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BidarshoPageTopBar(
                title = "تلپاتی و مدیتیشن",
                currentTier = currentTier,
                onBackClick = onBack,
                onTierClick = { onRequestUpgrade(MembershipTier.VIP) }
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Interactive 4-7-8 Breathing Character ("آدمک") at top of screen
                Breathing478Card(
                    breathingState = breathingState,
                    onToggleBreathing = onToggleBreathing
                )

                Text(
                    text = "جلسات هدایت‌شده تلپاتی و آرام‌سازی",
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepTeal
                )

                BidarshoCatalog.meditations.forEach { session ->
                    val hasAccess = currentTier.hasAccessTo(session.requiredTier)
                    val isExpanded = expandedSessionId == session.id
                    MeditationSessionCard(
                        session = session,
                        hasAccess = hasAccess,
                        isExpanded = isExpanded,
                        onToggle = {
                            if (hasAccess) {
                                expandedSessionId = if (isExpanded) null else session.id
                            } else {
                                onRequestUpgrade(session.requiredTier)
                            }
                        },
                        onUnlockClick = { onRequestUpgrade(session.requiredTier) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MeditationSessionCard(
    session: MeditationSession,
    hasAccess: Boolean,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onUnlockClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onToggle)
            .testTag("meditation_card_${session.id}"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (hasAccess) MistAqua.copy(alpha = 0.28f) else WarmGold.copy(alpha = 0.22f)
        ),
        border = BorderStroke(1.dp, SageTeal.copy(alpha = 0.35f))
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
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = WarmCream,
                    border = BorderStroke(1.dp, MistAqua)
                ) {
                    Text(
                        text = "${session.category} • ${session.durationMinutes.toPersianDigits()} دقیقه",
                        style = MaterialTheme.typography.labelSmall,
                        color = SageTeal,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                TierBadge(tier = session.requiredTier)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (hasAccess) DeepTeal else WarmGold),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (hasAccess) Icons.Default.SelfImprovement else Icons.Default.Lock,
                        contentDescription = null,
                        tint = if (hasAccess) WarmGold else DeepTeal
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = session.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = DeepTeal
                    )
                    Text(
                        text = session.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = SageTeal
                    )
                }

                if (hasAccess) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = DeepTeal
                    )
                }
            }

            AnimatedVisibility(visible = hasAccess && isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(WarmCream)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "مراحل اجرای جلسه:",
                        style = MaterialTheme.typography.labelLarge,
                        color = DeepTeal
                    )
                    session.guidanceSteps.forEachIndexed { idx, step ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MistAqua,
                                modifier = Modifier.size(22.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = (idx + 1).toPersianDigits(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = DeepTeal,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodyMedium,
                                color = DeepTeal
                            )
                        }
                    }
                }
            }

            if (!hasAccess) {
                Button(
                    onClick = onUnlockClick,
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
                    Text("دسترسی با اشتراک اعضای ویژه VIP")
                }
            }
        }
    }
}
