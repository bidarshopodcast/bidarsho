package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

// ColorScheme built exclusively from the 5 specified Bidarsho colors and their translucent tones
private val BidarshoColorScheme = lightColorScheme(
    primary = DeepTeal,
    onPrimary = WarmCream,
    primaryContainer = MistAqua,
    onPrimaryContainer = DeepTeal,
    secondary = SageTeal,
    onSecondary = WarmCream,
    secondaryContainer = MistAqua.copy(alpha = 0.55f),
    onSecondaryContainer = DeepTeal,
    tertiary = WarmGold,
    onTertiary = DeepTeal,
    tertiaryContainer = WarmGold.copy(alpha = 0.45f),
    onTertiaryContainer = DeepTeal,
    background = WarmCream,
    onBackground = DeepTeal,
    surface = WarmCream,
    onSurface = DeepTeal,
    surfaceVariant = MistAqua.copy(alpha = 0.35f),
    onSurfaceVariant = SageTeal,
    surfaceContainerLowest = WarmCream,
    surfaceContainerLow = MistAqua.copy(alpha = 0.22f),
    surfaceContainer = MistAqua.copy(alpha = 0.32f),
    surfaceContainerHigh = MistAqua.copy(alpha = 0.48f),
    surfaceContainerHighest = MistAqua.copy(alpha = 0.65f),
    outline = SageTeal.copy(alpha = 0.55f),
    outlineVariant = MistAqua,
    error = DeepTeal,
    onError = WarmGold
)

val BidarshoShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun BidarshoTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme(
            colorScheme = BidarshoColorScheme,
            typography = BidarshoTypography,
            shapes = BidarshoShapes,
            content = content
        )
    }
}
