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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.WorkspacePremium
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.BidarshoCatalog
import com.example.data.UserProfileEntity
import com.example.model.MembershipTier
import com.example.ui.components.BidarshoLogoMark
import com.example.ui.components.BidarshoPageTopBar
import com.example.ui.components.CanvasHeaderTheme
import com.example.ui.components.MinimalistCanvasHeader
import com.example.ui.components.openExternalUrl
import com.example.ui.components.toPersianDigits
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MistAqua
import com.example.ui.theme.SageTeal
import com.example.ui.theme.WarmCream
import com.example.ui.theme.WarmGold

@Composable
fun ProfileScreen(
    userProfile: UserProfileEntity?,
    currentTier: MembershipTier,
    enrolledCount: Int,
    savedArticlesCount: Int,
    onSelectTier: (MembershipTier) -> Unit,
    onSignOut: () -> Unit,
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
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            BidarshoPageTopBar(
                title = "پروفایل من",
                currentTier = currentTier,
                onTierClick = onOpenTierUpgrade
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MinimalistCanvasHeader(
                    theme = CanvasHeaderTheme.PROFILE,
                    title = userProfile?.fullName ?: "عضو بیدارشو",
                    subtitle = "${userProfile?.phoneNumber?.toPersianDigits() ?: ""} • ${userProfile?.email ?: ""}"
                )

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProfileStatCard(
                        icon = Icons.Default.School,
                        value = enrolledCount.toPersianDigits(),
                        label = "کلاس‌های ثبت‌نامی",
                        modifier = Modifier.weight(1f)
                    )
                    ProfileStatCard(
                        icon = Icons.Default.Bookmark,
                        value = savedArticlesCount.toPersianDigits(),
                        label = "مطالب ذخیره‌شده",
                        modifier = Modifier.weight(1f)
                    )
                }

                // 3-Tier Subscription Management
                Text(
                    text = "مدیریت سطح عضویت و اشتراک",
                    style = MaterialTheme.typography.titleMedium,
                    color = DeepTeal
                )

                MembershipTier.entries.forEach { tier ->
                    val isCurrent = tier == currentTier
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onSelectTier(tier) }
                            .testTag("profile_tier_${tier.name}"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent) WarmGold.copy(alpha = 0.48f) else MistAqua.copy(alpha = 0.25f)
                        ),
                        border = BorderStroke(
                            width = if (isCurrent) 1.5.dp else 1.dp,
                            color = if (isCurrent) DeepTeal else SageTeal.copy(alpha = 0.35f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    if (tier != MembershipTier.PUBLIC) {
                                        Icon(
                                            imageVector = Icons.Default.WorkspacePremium,
                                            contentDescription = null,
                                            tint = DeepTeal,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Text(
                                        text = tier.titleFa,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = DeepTeal,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = tier.descriptionFa,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SageTeal
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = if (isCurrent) DeepTeal else WarmCream,
                                border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = if (isCurrent) "فعال" else tier.monthlyLabelFa,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isCurrent) WarmGold else DeepTeal,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                OutlinedButton(
                    onClick = onSignOut,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("sign_out_button"),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, DeepTeal),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepTeal)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("خروج از حساب کاربری", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
private fun ProfileStatCard(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MistAqua.copy(alpha = 0.3f)),
        border = BorderStroke(1.dp, MistAqua)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = null, tint = DeepTeal)
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = DeepTeal,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = SageTeal
            )
        }
    }
}

/**
 * Counseling Screen ("نوبت مشاوره")
 * Strictly contains ONLY the Telegram support link button and WhatsApp support link button,
 * clearly stating online appointment booking is not available and coordination with support is mandatory.
 */
@Composable
fun CounselingScreen(
    currentTier: MembershipTier,
    onOpenTierUpgrade: () -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
                title = "نوبت مشاوره",
                currentTier = currentTier,
                onTierClick = onOpenTierUpgrade
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MinimalistCanvasHeader(
                    theme = CanvasHeaderTheme.COUNSELING,
                    title = "رزرو نوبت مشاوره تخصصی روابط",
                    subtitle = "هماهنگی مستقیم با تیم پشتیبانی موسسه بیدارشو"
                )

                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MistAqua.copy(alpha = 0.28f)),
                    border = BorderStroke(1.dp, MistAqua),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = WarmGold.copy(alpha = 0.45f),
                            border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.35f))
                        ) {
                            Text(
                                text = "توجه: در حال حاضر شرایط گرفتن نوبت مشاوره به‌صورت آنلاین در اپلیکیشن وجود ندارد و برای تعیین زمان جلسه حتماً نیاز به هماهنگی با پشتیبانی از طریق تلگرام یا واتساپ است.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = DeepTeal,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(14.dp)
                            )
                        }

                        // ONLY Telegram & WhatsApp support buttons
                        Button(
                            onClick = {
                                openExternalUrl(
                                    context,
                                    BidarshoCatalog.TELEGRAM_SUPPORT_URL,
                                    onShowMessage
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("counseling_telegram_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepTeal,
                                contentColor = WarmCream
                            )
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "لینک تلگرام پشتیبانی (هماهنگی نوبت)",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                        Button(
                            onClick = {
                                openExternalUrl(
                                    context,
                                    BidarshoCatalog.WHATSAPP_SUPPORT_URL,
                                    onShowMessage
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("counseling_whatsapp_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SageTeal,
                                contentColor = WarmCream
                            )
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "لینک واتساپ پشتیبانی (هماهنگی نوبت)",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContactScreen(
    currentTier: MembershipTier,
    onOpenTierUpgrade: () -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
                title = "تماس با ما",
                currentTier = currentTier,
                onTierClick = onOpenTierUpgrade
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MinimalistCanvasHeader(
                    theme = CanvasHeaderTheme.CONTACT,
                    title = "راه‌های ارتباطی با موسسه بیدارشو",
                    subtitle = "پاسخگویی پشتیبانی: شنبه تا پنج‌شنبه از ساعت ۱۰ تا ۱۹"
                )

                ContactChannelCard(
                    icon = Icons.AutoMirrored.Filled.Send,
                    title = "پشتیبانی تلگرام",
                    subtitle = "@bidarsho_support",
                    onClick = { openExternalUrl(context, BidarshoCatalog.TELEGRAM_SUPPORT_URL, onShowMessage) }
                )
                ContactChannelCard(
                    icon = Icons.Default.Chat,
                    title = "پشتیبانی واتساپ",
                    subtitle = "پیام مستقیم به تیم هماهنگی بیدارشو",
                    onClick = { openExternalUrl(context, BidarshoCatalog.WHATSAPP_SUPPORT_URL, onShowMessage) }
                )
                ContactChannelCard(
                    icon = Icons.Default.PhotoCamera,
                    title = "صفحه اینستاگرام بیدارشو",
                    subtitle = "@bidarsho.academy",
                    onClick = { openExternalUrl(context, BidarshoCatalog.INSTAGRAM_URL, onShowMessage) }
                )
                ContactChannelCard(
                    icon = Icons.Default.Email,
                    title = "ایمیل رسمی",
                    subtitle = BidarshoCatalog.SUPPORT_EMAIL,
                    onClick = {
                        openExternalUrl(context, "mailto:${BidarshoCatalog.SUPPORT_EMAIL}", onShowMessage)
                    }
                )
                ContactChannelCard(
                    icon = Icons.Default.Phone,
                    title = "تلفن دفتر",
                    subtitle = BidarshoCatalog.SUPPORT_PHONE,
                    onClick = {
                        openExternalUrl(context, "tel:02188004512", onShowMessage)
                    }
                )
            }
        }
    }
}

@Composable
private fun ContactChannelCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MistAqua.copy(alpha = 0.28f)),
        border = BorderStroke(1.dp, MistAqua)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(DeepTeal),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = WarmGold)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleSmall, color = DeepTeal)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = SageTeal)
            }
        }
    }
}

@Composable
fun AboutScreen(
    currentTier: MembershipTier,
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
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            BidarshoPageTopBar(
                title = "درباره ما",
                currentTier = currentTier,
                onTierClick = onOpenTierUpgrade
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MinimalistCanvasHeader(
                    theme = CanvasHeaderTheme.ABOUT,
                    title = "داستان و رسالت موسسه بیدارشو",
                    subtitle = "همراهی علمی و انسانی در مسیر خودآگاهی و روابط سالم"
                )

                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MistAqua.copy(alpha = 0.28f)),
                    border = BorderStroke(1.dp, MistAqua)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            BidarshoLogoMark(size = 50.dp)
                            Column {
                                Text(
                                    text = "موسسه روانشناسی و کوچینگ بیدارشو",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = DeepTeal
                                )
                                Text(
                                    text = "روانشناسی دلبستگی • کوچینگ روابط",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SageTeal
                                )
                            }
                        }

                        Text(
                            text = "موسسه «بیدارشو» با هدف آگاهی‌بخشی عمیق درباره الگوهای ناخودآگاه روابط عاطفی، سبک‌های دلبستگی (ایمن، اضطرابی و اجتنابی)، رهایی از روابط آسیب‌زا و بازیابی آرامش درونی شکل گرفته است.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = DeepTeal
                        )
                        Text(
                            text = "ما باور داریم کیفیت زندگی ما در گرو کیفیت رابطه‌ای است که ابتدا با خودمان و سپس با شریک عاطفی‌مان می‌سازیم. در بیدارشو آموزش‌های کاربردی، کارگاه‌های آنلاین با اسلایدهای مروری و تمرین‌های آرام‌سازی ذهن در کنار هم قرار گرفته‌اند.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = DeepTeal
                        )

                        listOf(
                            "آموزش‌های مبتنی بر علم دلبستگی و روان‌درمانی مدرن",
                            "ارائه اسلایدها و تمرین‌های عملی پس از هر کارگاه آنلاین",
                            "تمرین‌های تنظیم سیستم عصبی مثل تکنیک تنفسی ۴-۷-۸ و مدیتیشن"
                        ).forEach { pillar ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = DeepTeal,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = pillar,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = DeepTeal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
