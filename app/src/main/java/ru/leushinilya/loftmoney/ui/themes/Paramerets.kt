package ru.leushinilya.loftmoney.ui.themes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

data class LoftColors(
    val income: Color,
    val expense: Color,
    val primaryBackground: Color,
    val secondaryBackground: Color,
    val interactionBackground: Color,
    val contentBackground: Color,
    val interactionContentBackground: Color,
    val primaryText: Color,
    val hint: Color
)

data class LoftTypography(
    val toolbar: TextStyle,
    val tabs: TextStyle,
    val contentSmall: TextStyle,
    val contentNormal: TextStyle,
    val contentLarge: TextStyle
)

object LoftTheme {
    val colors: LoftColors
        @Composable
        get() = LocalColors.current

    val typography: LoftTypography
        @Composable
        get() = LocalTypography.current
}

val LocalColors = staticCompositionLocalOf<LoftColors> {
    error("No colors provided")
}

val LocalTypography = staticCompositionLocalOf<LoftTypography> {
    error("No typography provided")
}

enum class LoftFontStyle {
    SMALL, NORMAL, LARGE
}

enum class LoftColorStyle {
    BLUE, RED, PURPLE
}