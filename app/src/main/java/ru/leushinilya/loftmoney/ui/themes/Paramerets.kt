package ru.leushinilya.loftmoney.ui.themes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

data class ColorSet(
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

data class FontSet(
    val toolbar: TextStyle,
    val tabs: TextStyle,
    val contentSmall: TextStyle,
    val contentNormal: TextStyle,
    val contentLarge: TextStyle
)

object LoftTheme {
    val colors: ColorSet
        @Composable
        get() = LocalColors.current

    val typography: FontSet
        @Composable
        get() = LocalTypography.current
}

val LocalColors = staticCompositionLocalOf<ColorSet> {
    error("No colors provided")
}

val LocalTypography = staticCompositionLocalOf<FontSet> {
    error("No typography provided")
}

enum class LoftTypography(val fontSet: FontSet) {
    SMALL(smallTypography),
    NORMAL(normalTypography),
    LARGE(largeTypography)
}

enum class LoftColors(val colorSet: ColorSet) {
    BLUE(blueColorSet),
    RED(redColorSet),
    PURPLE(purpleColorSet)
}