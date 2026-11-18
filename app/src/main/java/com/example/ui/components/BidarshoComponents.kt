package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.BidarshoCatalog
import com.example.model.MembershipTier
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MistAqua
import com.example.ui.theme.SageTeal
import com.example.ui.theme.WarmCream
import com.example.ui.theme.WarmGold

fun String.toPersianDigits(): String {
    val persian = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
    val sb = StringBuilder(this.length)
    for (ch in this) {
        if (ch in '0'..'9') {
            sb.append(persian[ch - '0'])
        } else {
            sb.append(ch)
        }
    }
    return sb.toString()
}

fun Int.toPersianDigits(): String = this.toString().toPersianDigits()

fun openExternalUrl(context: Context, url: String, onError: (String) -> Unit) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (_: Exception) {
        onError("برنامه مناسب برای باز کردن لینک یافت نشد.")
    }
}

/**
 * Custom minimalist emblem for "بیدارشو" using strictly the 5 brand colors.
 */
@Composable
fun BidarshoLogoMark(
    modifier: Modifier = Modifier,
    size: Dp = 44.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(DeepTeal)
            .border(1.5.dp, WarmGold.copy(alpha = 0.7f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(6.dp)) {
            val w = this.size.width
            val h = this.size.height
            val center = Offset(w / 2f, h / 2f)

            // Outer calm arc
            drawArc(
                color = MistAqua,
                startAngle = 195f,
                sweepAngle = 150f,
                useCenter = false,
                topLeft = Offset(w * 0.1f, h * 0.15f),
                size = Size(w * 0.8f, h * 0.7f),
                style = Stroke(width = w * 0.08f, cap = StrokeCap.Round)
            )

            // Rising golden sun
            drawCircle(
                color = WarmGold,
                radius = w * 0.19f,
                center = Offset(center.x, h * 0.48f)
            )

            // Gentle sage horizon wave
            val wave = Path().apply {
                moveTo(w * 0.14f, h * 0.68f)
                quadraticTo(w * 0.5f, h * 0.54f, w * 0.86f, h * 0.68f)
            }
            drawPath(
                path = wave,
                color = SageTeal,
                style = Stroke(width = w * 0.09f, cap = StrokeCap.Round)
            )

            // Warm cream awakening spark dot
            drawCircle(
                color = WarmCream,
                radius = w * 0.055f,
                center = Offset(center.x, h * 0.16f)
            )
        }
    }
}

@Composable
fun TierBadge(
    tier: MembershipTier,
    modifier: Modifier = Modifier
) {
    val bgColor: Color
    val contentColor: Color
    val borderColor: Color
    when (tier) {
        MembershipTier.PUBLIC -> {
            bgColor = MistAqua.copy(alpha = 0.55f)
            contentColor = DeepTeal
            borderColor = SageTeal.copy(alpha = 0.4f)
        }
        MembershipTier.VIP -> {
            bgColor = WarmGold
            contentColor = DeepTeal
            borderColor = DeepTeal.copy(alpha = 0.25f)
        }
        MembershipTier.CLASS_MEMBER -> {
            bgColor = DeepTeal
            contentColor = WarmGold
            borderColor = WarmGold.copy(alpha = 0.6f)
        }
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (tier != MembershipTier.PUBLIC) {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(14.dp)
                )
            }
            Text(
                text = tier.badgeFa,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BidarshoPageTopBar(
    title: String,
    currentTier: MembershipTier,
    onBackClick: (() -> Unit)? = null,
    onTierClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (onBackClick != null) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MistAqua.copy(alpha = 0.35f))
                        .testTag("top_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "بازگشت",
                        tint = DeepTeal
                    )
                }
            } else {
                BidarshoLogoMark(size = 42.dp)
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = DeepTeal
                )
                Text(
                    text = "موسسه روانشناسی بیدارشو",
                    style = MaterialTheme.typography.bodySmall,
                    color = SageTeal
                )
            }
        }

        TierBadge(
            tier = currentTier,
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .clickable(onClick = onTierClick)
                .testTag("header_tier_badge")
        )
    }
}

@Composable
fun MinimalistDrawableHeader(
    @DrawableRes drawableRes: Int,
    contentDescription: String,
    captionTitle: String,
    captionSubtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MistAqua.copy(alpha = 0.28f)),
        border = BorderStroke(1.dp, MistAqua)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(168.dp)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            ) {
                Image(
                    painter = painterResource(id = drawableRes),
                    contentDescription = contentDescription,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WarmCream.copy(alpha = 0.92f))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = captionTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepTeal
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = captionSubtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = SageTeal
                )
            }
        }
    }
}

enum class CanvasHeaderTheme {
    AUTH,
    PROFILE,
    COUNSELING,
    CONTACT,
    ABOUT,
    CLASS_DETAIL
}

/**
 * Custom minimalist geometric & botanical Canvas illustrations tailored to each section
 * using strictly the 5 Bidarsho colors.
 */
@Composable
fun MinimalistCanvasHeader(
    theme: CanvasHeaderTheme,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MistAqua.copy(alpha = 0.28f)),
        border = BorderStroke(1.dp, MistAqua)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(WarmCream)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Soft background organic shape
                    drawCircle(
                        color = MistAqua.copy(alpha = 0.45f),
                        radius = h * 0.65f,
                        center = Offset(w * 0.22f, h * 0.45f)
                    )
                    drawCircle(
                        color = WarmGold.copy(alpha = 0.45f),
                        radius = h * 0.42f,
                        center = Offset(w * 0.78f, h * 0.35f)
                    )

                    when (theme) {
                        CanvasHeaderTheme.AUTH -> {
                            // Rising golden sun behind calm hills & doorway arch
                            drawArc(
                                color = DeepTeal,
                                startAngle = 180f,
                                sweepAngle = 180f,
                                useCenter = false,
                                topLeft = Offset(w * 0.35f, h * 0.22f),
                                size = Size(w * 0.30f, h * 0.9f),
                                style = Stroke(width = 6f, cap = StrokeCap.Round)
                            )
                            drawCircle(
                                color = WarmGold,
                                radius = h * 0.16f,
                                center = Offset(w * 0.5f, h * 0.46f)
                            )
                            val hill = Path().apply {
                                moveTo(0f, h * 0.85f)
                                quadraticTo(w * 0.5f, h * 0.65f, w, h * 0.85f)
                                lineTo(w, h)
                                lineTo(0f, h)
                                close()
                            }
                            drawPath(hill, color = SageTeal.copy(alpha = 0.45f))
                        }

                        CanvasHeaderTheme.COUNSELING -> {
                            // Two harmonious circles + connecting golden bridge
                            drawCircle(
                                color = DeepTeal,
                                radius = h * 0.22f,
                                center = Offset(w * 0.36f, h * 0.52f),
                                style = Stroke(width = 7f)
                            )
                            drawCircle(
                                color = SageTeal,
                                radius = h * 0.22f,
                                center = Offset(w * 0.64f, h * 0.52f),
                                style = Stroke(width = 7f)
                            )
                            val bridge = Path().apply {
                                moveTo(w * 0.36f, h * 0.52f)
                                quadraticTo(w * 0.5f, h * 0.24f, w * 0.64f, h * 0.52f)
                            }
                            drawPath(
                                bridge,
                                color = WarmGold,
                                style = Stroke(width = 8f, cap = StrokeCap.Round)
                            )
                            drawCircle(color = DeepTeal, radius = 10f, center = Offset(w * 0.36f, h * 0.52f))
                            drawCircle(color = SageTeal, radius = 10f, center = Offset(w * 0.64f, h * 0.52f))
                        }

                        CanvasHeaderTheme.PROFILE -> {
                            // Calm mandala halo & minimalist silhouette
                            drawCircle(
                                color = SageTeal.copy(alpha = 0.35f),
                                radius = h * 0.36f,
                                center = Offset(w * 0.5f, h * 0.5f),
                                style = Stroke(width = 5f)
                            )
                            drawCircle(
                                color = DeepTeal,
                                radius = h * 0.14f,
                                center = Offset(w * 0.5f, h * 0.36f)
                            )
                            drawArc(
                                color = DeepTeal,
                                startAngle = 180f,
                                sweepAngle = 180f,
                                useCenter = true,
                                topLeft = Offset(w * 0.38f, h * 0.54f),
                                size = Size(w * 0.24f, h * 0.38f)
                            )
                            drawCircle(
                                color = WarmGold,
                                radius = 11f,
                                center = Offset(w * 0.5f, h * 0.14f)
                            )
                        }

                        CanvasHeaderTheme.CONTACT -> {
                            // Minimalist sound ripples & message arc
                            for (i in 1..3) {
                                drawArc(
                                    color = if (i == 2) DeepTeal else SageTeal.copy(alpha = 0.6f),
                                    startAngle = -55f,
                                    sweepAngle = 110f,
                                    useCenter = false,
                                    topLeft = Offset(w * 0.45f - i * 26f, h * 0.5f - i * 26f),
                                    size = Size(i * 52f, i * 52f),
                                    style = Stroke(width = 6f, cap = StrokeCap.Round)
                                )
                            }
                            drawCircle(color = WarmGold, radius = 16f, center = Offset(w * 0.45f, h * 0.5f))
                        }

                        CanvasHeaderTheme.ABOUT -> {
                            // Growing botanical leaf & golden sun
                            val stem = Path().apply {
                                moveTo(w * 0.5f, h * 0.85f)
                                quadraticTo(w * 0.48f, h * 0.52f, w * 0.5f, h * 0.24f)
                            }
                            drawPath(stem, color = DeepTeal, style = Stroke(width = 7f, cap = StrokeCap.Round))
                            val leftLeaf = Path().apply {
                                moveTo(w * 0.5f, h * 0.62f)
                                quadraticTo(w * 0.35f, h * 0.52f, w * 0.38f, h * 0.38f)
                                quadraticTo(w * 0.48f, h * 0.45f, w * 0.5f, h * 0.62f)
                            }
                            drawPath(leftLeaf, color = SageTeal)
                            val rightLeaf = Path().apply {
                                moveTo(w * 0.5f, h * 0.48f)
                                quadraticTo(w * 0.65f, h * 0.38f, w * 0.62f, h * 0.24f)
                                quadraticTo(w * 0.52f, h * 0.32f, w * 0.5f, h * 0.48f)
                            }
                            drawPath(rightLeaf, color = MistAqua)
                            drawCircle(color = WarmGold, radius = 18f, center = Offset(w * 0.5f, h * 0.20f))
                        }

                        CanvasHeaderTheme.CLASS_DETAIL -> {
                            // Overlapping workshop cards / knowledge circles
                            drawCircle(
                                color = SageTeal.copy(alpha = 0.5f),
                                radius = h * 0.28f,
                                center = Offset(w * 0.42f, h * 0.52f)
                            )
                            drawCircle(
                                color = DeepTeal.copy(alpha = 0.75f),
                                radius = h * 0.28f,
                                center = Offset(w * 0.58f, h * 0.52f)
                            )
                            drawCircle(
                                color = WarmGold,
                                radius = h * 0.11f,
                                center = Offset(w * 0.5f, h * 0.52f)
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MistAqua.copy(alpha = 0.26f))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepTeal
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = SageTeal
                )
            }
        }
    }
}

@Composable
fun SubscriptionUpgradeDialog(
    requiredTier: MembershipTier,
    currentTier: MembershipTier,
    context: Context,
    onActivateTier: (MembershipTier) -> Unit,
    onDismiss: () -> Unit,
    onError: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = WarmCream),
            border = BorderStroke(1.5.dp, SageTeal.copy(alpha = 0.45f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(WarmGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = DeepTeal
                        )
                    }
                    Column {
                        Text(
                            text = "نیاز به اشتراک ${requiredTier.badgeFa}",
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepTeal
                        )
                        Text(
                            text = "سطح فعلی شما: ${currentTier.badgeFa}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SageTeal
                        )
                    }
                }

                Text(
                    text = requiredTier.descriptionFa,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DeepTeal
                )

                // Plan options
                listOf(MembershipTier.VIP, MembershipTier.CLASS_MEMBER).forEach { plan ->
                    val isSelected = plan == requiredTier
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (isSelected) WarmGold.copy(alpha = 0.35f) else MistAqua.copy(alpha = 0.28f),
                        border = BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) DeepTeal else SageTeal.copy(alpha = 0.35f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { onActivateTier(plan) }
                            .testTag("activate_tier_${plan.name}")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = plan.titleFa,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = DeepTeal,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = plan.descriptionFa,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SageTeal
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "انتخاب پلن",
                                tint = DeepTeal
                            )
                        }
                    }
                }

                Button(
                    onClick = { onActivateTier(requiredTier) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_activate_tier_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepTeal,
                        contentColor = WarmCream
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "فعال‌سازی آزمایشی ${requiredTier.badgeFa}",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                OutlinedButton(
                    onClick = {
                        openExternalUrl(context, BidarshoCatalog.TELEGRAM_SUPPORT_URL, onError)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    border = BorderStroke(1.dp, SageTeal),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepTeal),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "هماهنگی و خرید از طریق پشتیبانی تلگرام",
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "بستن",
                        color = SageTeal,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
