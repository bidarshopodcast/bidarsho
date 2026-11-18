package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.model.BreathingPhase
import com.example.model.BreathingState
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MistAqua
import com.example.ui.theme.SageTeal
import com.example.ui.theme.WarmCream
import com.example.ui.theme.WarmGold

/**
 * Interactive minimalist character ("آدمک") for the 4-7-8 breathing exercise
 * at the top of the Meditation & Telepathy screen.
 */
@Composable
fun Breathing478Card(
    breathingState: BreathingState,
    onToggleBreathing: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedScale by animateFloatAsState(
        targetValue = when (breathingState.phase) {
            BreathingPhase.READY, BreathingPhase.COMPLETED -> 0.25f
            else -> 0.25f + 0.75f * breathingState.phaseProgress
        },
        animationSpec = tween(durationMillis = 120, easing = FastOutSlowInEasing),
        label = "breathAuraScale"
    )

    val auraColor by animateColorAsState(
        targetValue = when (breathingState.phase) {
            BreathingPhase.INHALE -> MistAqua
            BreathingPhase.HOLD -> WarmGold
            BreathingPhase.EXHALE -> SageTeal
            BreathingPhase.COMPLETED -> WarmGold
            BreathingPhase.READY -> MistAqua.copy(alpha = 0.65f)
        },
        animationSpec = tween(durationMillis = 350),
        label = "breathAuraColor"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MistAqua.copy(alpha = 0.26f)),
        border = BorderStroke(1.5.dp, SageTeal.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                    Icon(
                        imageVector = Icons.Default.Air,
                        contentDescription = null,
                        tint = DeepTeal
                    )
                    Text(
                        text = "تکنیک تنفسی ۴-۷-۸ (آرامبخش سریع)",
                        style = MaterialTheme.typography.titleMedium,
                        color = DeepTeal
                    )
                }

                if (breathingState.isRunning || breathingState.phase == BreathingPhase.COMPLETED) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = WarmGold
                    ) {
                        Text(
                            text = "چرخه ${breathingState.currentCycle.toPersianDigits()} از ${breathingState.totalCycles.toPersianDigits()}",
                            style = MaterialTheme.typography.labelSmall,
                            color = DeepTeal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Clickable minimalist meditating character ("آدمک")
            Box(
                modifier = Modifier
                    .size(210.dp)
                    .clip(CircleShape)
                    .background(WarmCream)
                    .border(2.dp, auraColor.copy(alpha = 0.85f), CircleShape)
                    .clickable(onClick = onToggleBreathing)
                    .testTag("breathing_character_button"),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val center = Offset(w / 2f, h / 2f)

                    // Outer breathing aura circle
                    val maxRadius = w * 0.45f
                    val currentRadius = w * 0.16f + (maxRadius - w * 0.16f) * animatedScale
                    drawCircle(
                        color = auraColor.copy(alpha = 0.30f),
                        radius = currentRadius,
                        center = center
                    )
                    drawCircle(
                        color = auraColor.copy(alpha = 0.55f),
                        radius = currentRadius * 0.72f,
                        center = center
                    )

                    // Subtle vertical lift of character during inhale/hold
                    val breathLift = animatedScale * (h * 0.03f)

                    // Minimalist seated meditation character ("آدمک")
                    // Crossed legs / base
                    val basePath = Path().apply {
                        moveTo(w * 0.30f, h * 0.74f)
                        quadraticTo(w * 0.50f, h * 0.65f, w * 0.70f, h * 0.74f)
                        quadraticTo(w * 0.50f, h * 0.82f, w * 0.30f, h * 0.74f)
                        close()
                    }
                    drawPath(basePath, color = DeepTeal)

                    // Torso / chest (expands slightly with breath)
                    val torsoWidth = w * (0.20f + 0.05f * animatedScale)
                    drawArc(
                        color = DeepTeal,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = true,
                        topLeft = Offset(center.x - torsoWidth / 2f, h * 0.45f - breathLift),
                        size = Size(torsoWidth, h * 0.44f)
                    )

                    // Glowing lungs / heart center
                    drawCircle(
                        color = if (breathingState.phase == BreathingPhase.HOLD) WarmGold else WarmCream,
                        radius = w * (0.045f + 0.025f * animatedScale),
                        center = Offset(center.x, h * 0.56f - breathLift)
                    )

                    // Arms resting calmly on knees
                    val leftArm = Path().apply {
                        moveTo(center.x - torsoWidth * 0.45f, h * 0.52f - breathLift)
                        quadraticTo(w * 0.28f, h * 0.62f, w * 0.34f, h * 0.72f)
                    }
                    val rightArm = Path().apply {
                        moveTo(center.x + torsoWidth * 0.45f, h * 0.52f - breathLift)
                        quadraticTo(w * 0.72f, h * 0.62f, w * 0.66f, h * 0.72f)
                    }
                    drawPath(leftArm, color = SageTeal, style = Stroke(width = 7f, cap = StrokeCap.Round))
                    drawPath(rightArm, color = SageTeal, style = Stroke(width = 7f, cap = StrokeCap.Round))

                    // Head
                    drawCircle(
                        color = DeepTeal,
                        radius = w * 0.095f,
                        center = Offset(center.x, h * 0.35f - breathLift)
                    )

                    // Serene closed eyes / mindful halo dot
                    drawArc(
                        color = WarmCream,
                        startAngle = 15f,
                        sweepAngle = 150f,
                        useCenter = false,
                        topLeft = Offset(center.x - w * 0.055f, h * 0.35f - breathLift),
                        size = Size(w * 0.04f, h * 0.025f),
                        style = Stroke(width = 3f, cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = WarmCream,
                        startAngle = 15f,
                        sweepAngle = 150f,
                        useCenter = false,
                        topLeft = Offset(center.x + w * 0.015f, h * 0.35f - breathLift),
                        size = Size(w * 0.04f, h * 0.025f),
                        style = Stroke(width = 3f, cap = StrokeCap.Round)
                    )

                    // Exhale calm wave lines when exhaling
                    if (breathingState.phase == BreathingPhase.EXHALE) {
                        val exhaleWave = Path().apply {
                            moveTo(center.x - w * 0.14f, h * 0.20f)
                            quadraticTo(center.x, h * 0.14f, center.x + w * 0.14f, h * 0.20f)
                        }
                        drawPath(exhaleWave, color = SageTeal, style = Stroke(width = 4f, cap = StrokeCap.Round))
                    }
                }

                // Live seconds counter badge at bottom of circle
                if (breathingState.isRunning) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = DeepTeal,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 10.dp)
                    ) {
                        Text(
                            text = "${breathingState.secondsRemaining.toPersianDigits()} ثانیه",
                            style = MaterialTheme.typography.labelLarge,
                            color = WarmGold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Phase title & instruction
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = breathingState.phase.titleFa,
                    style = MaterialTheme.typography.titleLarge,
                    color = DeepTeal
                )
                Text(
                    text = breathingState.phase.instructionFa,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SageTeal,
                    textAlign = TextAlign.Center
                )
            }

            // Cycle step dots (1..4)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 1..4) {
                    val active = i <= breathingState.currentCycle
                    Box(
                        modifier = Modifier
                            .size(if (i == breathingState.currentCycle && breathingState.isRunning) 14.dp else 10.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    active -> WarmGold
                                    else -> MistAqua
                                }
                            )
                            .border(1.dp, DeepTeal.copy(alpha = 0.4f), CircleShape)
                    )
                }
            }

            // Protocol summary pills (4 - 7 - 8 - 4x)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ProtocolChip("دم ۴ث", breathingState.phase == BreathingPhase.INHALE, Modifier.weight(1f))
                ProtocolChip("حبس ۷ث", breathingState.phase == BreathingPhase.HOLD, Modifier.weight(1f))
                ProtocolChip("بازدم ۸ث", breathingState.phase == BreathingPhase.EXHALE, Modifier.weight(1f))
                ProtocolChip("۴ بار تکرار", breathingState.phase == BreathingPhase.COMPLETED, Modifier.weight(1f))
            }

            Button(
                onClick = onToggleBreathing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("breathing_action_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (breathingState.isRunning) SageTeal else DeepTeal,
                    contentColor = WarmCream
                )
            ) {
                Icon(
                    imageVector = when {
                        breathingState.isRunning -> Icons.Default.Stop
                        breathingState.phase == BreathingPhase.COMPLETED -> Icons.Default.Refresh
                        else -> Icons.Default.PlayArrow
                    },
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = when {
                        breathingState.isRunning -> "توقف تمرین"
                        breathingState.phase == BreathingPhase.COMPLETED -> "تکرار مجدد تمرین ۴-۷-۸"
                        else -> "شروع تمرین تنفس ۴-۷-۸"
                    },
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun ProtocolChip(
    text: String,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (isActive) WarmGold else WarmCream,
        border = BorderStroke(1.dp, if (isActive) DeepTeal else MistAqua)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = DeepTeal,
            textAlign = TextAlign.Center,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
        )
    }
}
