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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.BidarshoCatalog
import com.example.model.LearnArticle
import com.example.model.MembershipTier
import com.example.ui.components.BidarshoPageTopBar
import com.example.ui.components.MinimalistDrawableHeader
import com.example.ui.components.TierBadge
import com.example.ui.components.toPersianDigits
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MistAqua
import com.example.ui.theme.SageTeal
import com.example.ui.theme.WarmCream
import com.example.ui.theme.WarmGold

@Composable
fun LearnMoreScreen(
    currentTier: MembershipTier,
    savedArticleIds: Set<String>,
    onToggleSaveArticle: (String) -> Unit,
    onBack: () -> Unit,
    onRequestUpgrade: (MembershipTier) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedArticleId by remember { mutableStateOf<String?>("art_1") }

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
                title = "بیشتر یاد بگیر",
                currentTier = currentTier,
                onBackClick = onBack,
                onTierClick = { onRequestUpgrade(MembershipTier.VIP) }
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                MinimalistDrawableHeader(
                    drawableRes = R.drawable.img_illust_learn,
                    contentDescription = "تصویرسازی مینیمال بیشتر یاد بگیر",
                    captionTitle = "کتابخانه خودشناسی و مهارت‌های رابطه",
                    captionSubtitle = "مطالب آموزشی سطح‌بندی‌شده برای بخش عمومی، اعضای ویژه و اعضای کلاس"
                )

                BidarshoCatalog.learnArticles.forEach { article ->
                    val hasAccess = currentTier.hasAccessTo(article.requiredTier)
                    val isSaved = savedArticleIds.contains(article.id)
                    val isExpanded = expandedArticleId == article.id

                    LearnArticleCard(
                        article = article,
                        hasAccess = hasAccess,
                        isSaved = isSaved,
                        isExpanded = isExpanded,
                        onToggleExpand = {
                            if (hasAccess) {
                                expandedArticleId = if (isExpanded) null else article.id
                            } else {
                                onRequestUpgrade(article.requiredTier)
                            }
                        },
                        onToggleSave = { onToggleSaveArticle(article.id) },
                        onUnlock = { onRequestUpgrade(article.requiredTier) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LearnArticleCard(
    article: LearnArticle,
    hasAccess: Boolean,
    isSaved: Boolean,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onToggleSave: () -> Unit,
    onUnlock: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onToggleExpand)
            .testTag("article_card_${article.id}"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (hasAccess) MistAqua.copy(alpha = 0.28f) else WarmGold.copy(alpha = 0.2f)
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
                        text = "${article.topicTag} • ${article.readMinutes.toPersianDigits()} دقیقه مطالعه",
                        style = MaterialTheme.typography.labelSmall,
                        color = SageTeal,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TierBadge(tier = article.requiredTier)
                    if (hasAccess) {
                        IconButton(
                            onClick = onToggleSave,
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("save_article_${article.id}")
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = if (isSaved) "حذف از نشان‌شده‌ها" else "ذخیره مقاله",
                                tint = DeepTeal
                            )
                        }
                    }
                }
            }

            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                color = DeepTeal
            )
            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodySmall,
                color = SageTeal
            )

            AnimatedVisibility(visible = hasAccess && isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(WarmCream)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    article.paragraphs.forEach { paragraph ->
                        Text(
                            text = paragraph,
                            style = MaterialTheme.typography.bodyMedium,
                            color = DeepTeal
                        )
                    }
                }
            }

            if (!hasAccess) {
                Button(
                    onClick = onUnlock,
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
                    Text("باز کردن با ${article.requiredTier.titleFa}")
                }
            }
        }
    }
}
