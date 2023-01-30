package ru.leushinilya.loftmoney.data.repository

import kotlinx.coroutines.flow.*
import ru.leushinilya.loftmoney.data.local.PreferencesDataSource
import ru.leushinilya.loftmoney.ui.themes.*
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource
) : PreferencesRepository {

    override suspend fun getAuthToken(): String? =
        preferencesDataSource.getString("authToken")

    override suspend fun setAuthToken(value: String) =
        preferencesDataSource.saveString("authToken", value)

    override suspend fun getUiSettings(): UiSettings =
        preferencesDataSource.getObject("uiSettings", UiSettings::class.java)
            ?: UiSettings(LoftColors.BLUE, LoftTypography.NORMAL)

    override suspend fun setUiSettings(value: UiSettings) {
        preferencesDataSource.saveObject("uiSettings", value)
        uiSettingsStateFlow.emit(value)
    }

    private val uiSettingsStateFlow = MutableStateFlow(UiSettings())
    override val uiSettingsFlow: Flow<UiSettings> = uiSettingsStateFlow.map { 
        getUiSettings()
    }

    override suspend fun clearAll() =
        preferencesDataSource.clearAll()

}