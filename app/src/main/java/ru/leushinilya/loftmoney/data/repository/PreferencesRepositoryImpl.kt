package ru.leushinilya.loftmoney.data.repository

import ru.leushinilya.loftmoney.data.local.PreferencesDataSource
import ru.leushinilya.loftmoney.ui.themes.LoftColors
import ru.leushinilya.loftmoney.ui.themes.LoftTypography
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource
) : PreferencesRepository {

    override suspend fun getAuthToken(): String? =
        preferencesDataSource.getString("authToken")

    override suspend fun setAuthToken(value: String) =
        preferencesDataSource.saveString("authToken", value)

    override suspend fun getColors(): LoftColors? =
        preferencesDataSource.getObject("colors", LoftColors::class.java)

    override suspend fun setColors(value: LoftColors) =
        preferencesDataSource.saveObject("typography", value)

    override suspend fun getTypography(): LoftTypography? =
        preferencesDataSource.getObject("typography", LoftTypography::class.java)

    override suspend fun setTypography(value: LoftTypography) =
        preferencesDataSource.saveObject("typography", value)

    override suspend fun clearAll() =
        preferencesDataSource.clearAll()

}