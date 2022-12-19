package ru.leushinilya.loftmoney.data.repository

interface PreferencesRepository {

    suspend fun getAuthToken(): String?
    suspend fun setAuthToken(value: String)
    suspend fun clearAll()

}