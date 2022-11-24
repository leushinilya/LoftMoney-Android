package ru.leushinilya.loftmoney.data.local

import android.app.Application
import android.content.Context
import ru.leushinilya.loftmoney.BuildConfig
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val application: Application
) {

    private val preferences = application.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    fun getString(key: String): String? = preferences.getString(key, null)
    fun saveString(key: String, value: String) = preferences.edit().putString(key, value).apply()

    fun clearAll() = preferences.edit().clear().apply()

}