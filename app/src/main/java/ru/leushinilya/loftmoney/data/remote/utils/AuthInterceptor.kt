package ru.leushinilya.loftmoney.data.remote.utils

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.leushinilya.loftmoney.data.repository.PreferencesRepository
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        runBlocking {
            preferencesRepository.getAuthToken()?.let {
                val url = request.url.newBuilder().addQueryParameter("auth-token", it).build()
                request = request.newBuilder().url(url).build()
            }
        }
        return chain.proceed(request)
    }

}