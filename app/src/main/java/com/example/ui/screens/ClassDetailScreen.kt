package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Slideshow
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.MembershipTier
import com.example.model.OnlineClassItem
import com.example.ui.components.BidarshoPageTopBar
import com.example.ui.components.CanvasHeaderTheme
import com.example.ui.components.MinimalistCanvasHeader
import com.example.ui.components.toPersianDigits
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MistAqua
import com.example.ui.theme.SageTeal
import com.example.ui.theme.WarmCream
import com.example.ui.theme.WarmGold
import kotlinx.coroutines.launch

@Composable
fun ClassDetailScreen(
    classItem: OnlineClassItem,
    currentTier: MembershipTier,
    isEnrolled: Boolean,
    lastSlideIndex: Int,
    onBack: () -> Unit,
    onEnrollClick: () -> Unit,
    onSlideChanged: (Int) -> Unit,
    onOpenTierUpgrade: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    val hasClassMemberAccess = currentTier == MembershipTier.CLASS_MEMBER
    val pagerState = rememberPagerState(
        initialPage = lastSlideIndex.coerceIn(0, (classItem.slides.size - 1).coerceAtLeast(0)),
        pageCount = { classItem.slides.size }
    )
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        if (hasClassMemberAccess) {
            onSlideChanged(pagerState.currentPage)
        }
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
                .padding(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            BidarshoPageTopBar(
                title = classItem.title,
                currentTier = currentTier,
                onBackClick = onBack,
                onTierClick = onOpenTierUpgrade
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MinimalistCanvasHeader(
                    theme = CanvasHeaderTheme.CLASS_DETAIL,
                    title = classItem.instructor,
                    subtitle = classItem.durationText.toPersianDigits()
                )

                // Overview & Syllabus Card
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MistAqua.copy(alpha = 0.28f)),
                    border = BorderStroke(1.dp, MistAqua)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "درباره این کارگاه",
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepTeal
                        )
                        Text(
                            text = classItem.summary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = DeepTeal
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "سرفصل‌های آموزشی",
                            style = MaterialTheme.typography.titleSmall,
                            color = DeepTeal
                        )
                        classItem.syllabus.forEachIndexed { idx, topic ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = WarmGold,
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
                                    text = topic,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = DeepTeal
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = onEnrollClick,
                            enabled = !isEnrolled,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("enroll_class_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepTeal,
                                contentColor = WarmCream,
                                disabledContainerColor = WarmGold,
                                disabledContentColor = DeepTeal
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isEnrolled) "ثبت‌نام شما تایید شده است" else "ثبت‌نام در این کلاس (مخصوص اعضای کلاس)",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }

                // Class Slides Section (Class Members only)
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
                            imageVector = Icons.Default.Slideshow,
                            contentDescription = null,
                            tint = DeepTeal
                        )
                        Text(
                            text = "اسلایدهای مرور پس از کلاس",
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepTeal
                        )
                    }

                    if (hasClassMemberAccess && classItem.slides.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = WarmGold
                        ) {
                            Text(
                                text = "اسلاید ${(pagerState.currentPage + 1).toPersianDigits()} از ${classItem.slides.size.toPersianDigits()}",
                                style = MaterialTheme.typography.labelSmall,
                                color = DeepTeal,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                if (hasClassMemberAccess) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        val slide = classItem.slides[page]
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = DeepTeal),
                            border = BorderStroke(1.5.dp, WarmGold.copy(alpha = 0.6f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = WarmGold
                                ) {
                                    Text(
                                        text = "اسلاید ${slide.slideNumber.toPersianDigits()}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = DeepTeal,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                    )
                                }
                                Text(
                                    text = slide.title,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = WarmCream
                                )
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = SageTeal.copy(alpha = 0.4f)
                                ) {
                                    Text(
                                        text = slide.keyPoint,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = WarmGold,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                                Text(
                                    text = slide.explanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MistAqua
                                )
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = WarmCream.copy(alpha = 0.12f),
                                    border = BorderStroke(1.dp, MistAqua.copy(alpha = 0.35f))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "تمرین بازتابی:",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = WarmGold
                                        )
                                        Text(
                                            text = slide.reflectionPrompt,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = WarmCream
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val prev = (pagerState.currentPage - 1).coerceAtLeast(0)
                                coroutineScope.launch { pagerState.animateScrollToPage(prev) }
                            },
                            enabled = pagerState.currentPage > 0,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("prev_slide_button"),
                            border = BorderStroke(1.dp, SageTeal),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = DeepTeal)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("اسلاید قبلی", color = DeepTeal)
                        }

                        Button(
                            onClick = {
                                val next = (pagerState.currentPage + 1).coerceAtMost(classItem.slides.lastIndex)
                                coroutineScope.launch { pagerState.animateScrollToPage(next) }
                            },
                            enabled = pagerState.currentPage < classItem.slides.lastIndex,
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("next_slide_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SageTeal,
                                contentColor = WarmCream
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("اسلاید بعدی")
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                        }
                    }
                } else {
                    // Locked slides prompt for non-class-members
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = WarmGold.copy(alpha = 0.28f)),
                        border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.4f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(DeepTeal),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "مخصوص اعضای کلاس",
                                    tint = WarmGold
                                )
                            }
                            Text(
                                text = "اسلایدهای آموزشی مخصوص «بخش اعضای کلاس» است",
                                style = MaterialTheme.typography.titleSmall,
                                color = DeepTeal,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "با تهیه اشتراک اعضای کلاس، به تمام اسلایدهای مرور پس از کلاس و ثبت‌نام کارگاه‌ها دسترسی کامل خواهید داشت.",
                                style = MaterialTheme.typography.bodySmall,
                                color = SageTeal
                            )
                            Button(
                                onClick = onOpenTierUpgrade,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("unlock_slides_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DeepTeal,
                                    contentColor = WarmCream
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("فعال‌سازی اشتراک اعضای کلاس")
                            }
                        }
                    }
                }
            }
        }
    }
}
