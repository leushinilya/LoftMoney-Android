package ru.leushinilya.loftmoney.data.repository

import ru.leushinilya.loftmoney.data.local.PreferencesDataSource
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource
) : PreferencesRepository {

    override suspend fun getAuthToken(): String? = preferencesDataSource.getString("authToken")

    override suspend fun setAuthToken(value: String) =
        preferencesDataSource.saveString("authToken", value)

    override suspend fun clearAll() = preferencesDataSource.clearAll()

}