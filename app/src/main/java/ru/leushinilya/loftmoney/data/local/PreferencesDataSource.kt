package ru.leushinilya.loftmoney.data.local

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import ru.leushinilya.loftmoney.BuildConfig
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val application: Application
) {

    private val preferences =
        application.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    fun getString(key: String): String? = preferences.getString(key, null)
    fun saveString(key: String, value: String?) = preferences.edit().putString(key, value).apply()

    fun <T> getObject(key: String, cls: Class<T>): T? {
        val serializedObject = preferences.getString(key, null)
        return Gson().fromJson(serializedObject, cls)
    }

    fun saveObject(key: String, value: Any?) {
        val serializedObject = Gson().toJson(value)
        preferences.edit().putString(key, serializedObject).apply()
    }

    fun clearAll() = preferences.edit().clear().apply()

}