package ru.leushinilya.loftmoney.data.repository

import kotlinx.coroutines.flow.Flow
import ru.leushinilya.loftmoney.ui.themes.UiSettings

interface PreferencesRepository {

    suspend fun getAuthToken(): String?
    suspend fun setAuthToken(value: String)

    val uiSettingsFlow: Flow<UiSettings>
    suspend fun setUiSettings(value: UiSettings)

    suspend fun clearAll()

}