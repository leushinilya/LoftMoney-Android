package ru.leushinilya.loftmoney.ui.themes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun MainTheme(
    uiSettings: UiSettings,
    content: @Composable () -> Unit
) {

    val colors = when (uiSettings.colors) {
        LoftColors.BLUE -> blueColorSet
        LoftColors.PURPLE -> purpleColorSet
        LoftColors.RED -> redColorSet
    }

    val typography = when (uiSettings.typography) {
        LoftTypography.SMALL -> smallTypography
        LoftTypography.NORMAL -> normalTypography
        LoftTypography.LARGE -> largeTypography
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography,
        content = content
    )
}

data class UiSettings(
    val colors: LoftColors = LoftColors.BLUE,
    val typography: LoftTypography = LoftTypography.NORMAL
)