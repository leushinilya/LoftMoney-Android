package ru.leushinilya.loftmoney.ui.themes

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val smallTypography = FontSet(
    toolbar = TextStyle(fontWeight = FontWeight(700), fontSize = 18.sp),
    tabs = TextStyle(fontWeight = FontWeight(700), fontSize = 14.sp),
    contentSmall = TextStyle(fontWeight = FontWeight(500), fontSize = 10.sp),
    contentNormal = TextStyle(fontWeight = FontWeight(500), fontSize = 18.sp),
    contentLarge = TextStyle(fontWeight = FontWeight(500), fontSize = 36.sp),
)

val normalTypography = FontSet(
    toolbar = TextStyle(fontWeight = FontWeight(700), fontSize = 20.sp),
    tabs = TextStyle(fontWeight = FontWeight(700), fontSize = 14.sp),
    contentSmall = TextStyle(fontWeight = FontWeight(500), fontSize = 12.sp),
    contentNormal = TextStyle(fontWeight = FontWeight(500), fontSize = 20.sp),
    contentLarge = TextStyle(fontWeight = FontWeight(500), fontSize = 48.sp),
)

val largeTypography = FontSet(
    toolbar = TextStyle(fontWeight = FontWeight(700), fontSize = 24.sp),
    tabs = TextStyle(fontWeight = FontWeight(700), fontSize = 18.sp),
    contentSmall = TextStyle(fontWeight = FontWeight(500), fontSize = 14.sp),
    contentNormal = TextStyle(fontWeight = FontWeight(500), fontSize = 24.sp),
    contentLarge = TextStyle(fontWeight = FontWeight(500), fontSize = 56.sp),
)