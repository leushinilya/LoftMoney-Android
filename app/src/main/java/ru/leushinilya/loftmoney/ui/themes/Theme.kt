package ru.leushinilya.loftmoney.ui.themes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun MainTheme(
    colorStyle: LoftColorStyle,
    fontStyle: LoftFontStyle,
    content: @Composable () -> Unit
) {

    val colors = when (colorStyle) {
        LoftColorStyle.BLUE -> bluePalette
        LoftColorStyle.PURPLE -> purplePalette
        LoftColorStyle.RED -> redPalette
    }

    val typography = when (fontStyle) {
        LoftFontStyle.SMALL -> smallTypography
        LoftFontStyle.NORMAL -> normalTypography
        LoftFontStyle.LARGE -> largeTypography
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography,
        content = content
    )
}