package ru.leushinilya.loftmoney

import android.app.Application
import android.content.SharedPreferences
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.leushinilya.loftmoney.data.remote.AuthAPI
import ru.leushinilya.loftmoney.data.remote.ItemsAPI

@HiltAndroidApp
class LoftApp : Application() {

    lateinit var itemsAPI: ItemsAPI
    lateinit var authAPI: AuthAPI
    lateinit var preferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        configureRetrofit()
        preferences = getSharedPreferences(getString(R.string.app_name), 0)
    }

    private fun configureRetrofit() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://loftschool.com/android-api/basic/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        itemsAPI = retrofit.create(ItemsAPI::class.java)
        authAPI = retrofit.create(AuthAPI::class.java)
    }

    companion object {
        var AUTH_KEY = "authKey"
    }
}