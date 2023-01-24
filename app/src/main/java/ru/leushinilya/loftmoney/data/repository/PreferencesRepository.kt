package ru.leushinilya.loftmoney.data.repository

import ru.leushinilya.loftmoney.ui.themes.LoftColors
import ru.leushinilya.loftmoney.ui.themes.LoftTypography

interface PreferencesRepository {

    suspend fun getAuthToken(): String?
    suspend fun setAuthToken(value: String)

    suspend fun getColors(): LoftColors?
    suspend fun setColors(value: LoftColors)

    suspend fun getTypography(): LoftTypography?
    suspend fun setTypography(value: LoftTypography)

    suspend fun clearAll()

}