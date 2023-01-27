package ru.leushinilya.loftmoney.data.repository

import ru.leushinilya.loftmoney.ui.themes.UiSettings

interface PreferencesRepository {

    suspend fun getAuthToken(): String?
    suspend fun setAuthToken(value: String)

    suspend fun getUiSettings(): UiSettings
    suspend fun setUiSettings(value: UiSettings)

    suspend fun clearAll()

}