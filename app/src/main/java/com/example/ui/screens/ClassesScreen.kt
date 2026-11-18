package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.BidarshoCatalog
import com.example.data.ClassEnrollmentEntity
import com.example.model.MembershipTier
import com.example.model.OnlineClassItem
import com.example.ui.components.BidarshoPageTopBar
import com.example.ui.components.MinimalistDrawableHeader
import com.example.ui.components.toPersianDigits
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MistAqua
import com.example.ui.theme.SageTeal
import com.example.ui.theme.WarmCream
import com.example.ui.theme.WarmGold

@Composable
fun ClassesScreen(
    currentTier: MembershipTier,
    enrollments: Map<String, ClassEnrollmentEntity>,
    onBack: () -> Unit,
    onOpenClassDetail: (OnlineClassItem) -> Unit,
    onOpenTierUpgrade: () -> Unit,
    onLockedClassTapped: (OnlineClassItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val classes = BidarshoCatalog.onlineClasses
    val nowMillis = System.currentTimeMillis()

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
                title = "کلاس‌های آنلاین و کارگاه‌ها",
                currentTier = currentTier,
                onBackClick = onBack,
                onTierClick = onOpenTierUpgrade
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MinimalistDrawableHeader(
                    drawableRes = R.drawable.img_illust_classes,
                    contentDescription = "تصویرسازی مینیمال کلاس‌های آنلاین",
                    captionTitle = "کارگاه‌های تخصصی روابط + اسلایدهای کلاس",
                    captionSubtitle = "کلاس‌های آینده تا زمان برگزاری قفل هستند و در تاریخ مقرر با امکان ثبت‌نام روشن و فعال می‌شوند."
                )

                classes.forEachIndexed { index, classItem ->
                    val unlocked = classItem.isUnlocked(nowMillis)
                    val isEnrolled = enrollments.containsKey(classItem.id)
                    OnlineClassCard(
                        classItem = classItem,
                        cardIndex = index,
                        isUnlocked = unlocked,
                        isEnrolled = isEnrolled,
                        onClick = {
                            if (unlocked) {
                                onOpenClassDetail(classItem)
                            } else {
                                onLockedClassTapped(classItem)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun OnlineClassCard(
    classItem: OnlineClassItem,
    cardIndex: Int,
    isUnlocked: Boolean,
    isEnrolled: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .testTag("class_card_${classItem.id}"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) MistAqua.copy(alpha = 0.34f) else MistAqua.copy(alpha = 0.16f)
        ),
        border = BorderStroke(
            width = if (isUnlocked) 1.5.dp else 1.dp,
            color = if (isUnlocked) DeepTeal.copy(alpha = 0.45f) else SageTeal.copy(alpha = 0.35f)
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Class visual cover with Lock overlay when future-dated
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(104.dp)
                    .background(if (isUnlocked) DeepTeal else DeepTeal.copy(alpha = 0.55f))
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(if (isUnlocked) 1f else 0.35f)
                ) {
                    val w = size.width
                    val h = size.height
                    when (cardIndex % 4) {
                        0 -> {
                            drawCircle(SageTeal, radius = h * 0.65f, center = Offset(w * 0.18f, h * 0.5f))
                            drawCircle(WarmGold.copy(alpha = 0.85f), radius = h * 0.25f, center = Offset(w * 0.82f, h * 0.35f))
                            drawCircle(MistAqua, radius = h * 0.4f, center = Offset(w * 0.52f, h * 0.85f), style = Stroke(6f))
                        }
                        1 -> {
                            drawCircle(WarmGold.copy(alpha = 0.85f), radius = h * 0.38f, center = Offset(w * 0.25f, h * 0.4f))
                            drawCircle(MistAqua.copy(alpha = 0.5f), radius = h * 0.52f, center = Offset(w * 0.78f, h * 0.6f))
                        }
                        2 -> {
                            drawLine(
                                color = MistAqua,
                                start = Offset(0f, h * 0.7f),
                                end = Offset(w, h * 0.3f),
                                strokeWidth = 10f,
                                cap = StrokeCap.Round
                            )
                            drawCircle(WarmGold, radius = h * 0.28f, center = Offset(w * 0.5f, h * 0.48f))
                        }
                        else -> {
                            drawCircle(SageTeal, radius = h * 0.4f, center = Offset(w * 0.4f, h * 0.5f))
                            drawCircle(WarmGold, radius = h * 0.4f, center = Offset(w * 0.6f, h * 0.5f), style = Stroke(8f))
                        }
                    }
                }

                // Bright vs Locked overlay badge
                if (!isUnlocked) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DeepTeal.copy(alpha = 0.62f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(WarmCream),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "کلاس قفل است",
                                    tint = DeepTeal
                                )
                            }
                            Text(
                                text = "برگزاری در آینده • قفل تا زمان مقرر",
                                style = MaterialTheme.typography.labelMedium,
                                color = WarmGold
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = WarmGold,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.EventAvailable,
                                contentDescription = null,
                                tint = DeepTeal,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (isEnrolled) "ثبت‌نام شده" else "باز و آماده ثبت‌نام",
                                style = MaterialTheme.typography.labelSmall,
                                color = DeepTeal,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = classItem.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = if (isUnlocked) DeepTeal else DeepTeal.copy(alpha = 0.7f)
                )
                Text(
                    text = classItem.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = SageTeal
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = classItem.scheduledDateFa.toPersianDigits(),
                        style = MaterialTheme.typography.labelSmall,
                        color = DeepTeal
                    )
                    Text(
                        text = "${classItem.slides.size.toPersianDigits()} اسلاید آموزشی",
                        style = MaterialTheme.typography.labelSmall,
                        color = SageTeal
                    )
                }

                Button(
                    onClick = onClick,
                    enabled = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("class_action_button_${classItem.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isUnlocked) DeepTeal else SageTeal.copy(alpha = 0.45f),
                        contentColor = if (isUnlocked) WarmCream else DeepTeal
                    )
                ) {
                    Icon(
                        imageVector = when {
                            !isUnlocked -> Icons.Default.Lock
                            isEnrolled -> Icons.Default.Slideshow
                            else -> Icons.Default.CheckCircle
                        },
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when {
                            !isUnlocked -> "قفل تا تاریخ برگزاری"
                            isEnrolled -> "ورود به کلاس و مشاهده اسلایدها"
                            else -> "مشاهده و ثبت‌نام در کلاس"
                        },
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}
