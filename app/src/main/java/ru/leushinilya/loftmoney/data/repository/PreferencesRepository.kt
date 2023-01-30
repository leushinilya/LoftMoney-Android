package ru.leushinilya.loftmoney.data.repository

import kotlinx.coroutines.flow.Flow
import ru.leushinilya.loftmoney.ui.themes.UiSettings

interface PreferencesRepository {

    suspend fun getAuthToken(): String?
    suspend fun setAuthToken(value: String)

    suspend fun getUiSettings(): UiSettings
    suspend fun setUiSettings(value: UiSettings)
    val uiSettingsFlow: Flow<UiSettings>

    suspend fun clearAll()

}