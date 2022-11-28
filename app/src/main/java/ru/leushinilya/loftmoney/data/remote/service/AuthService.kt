package ru.leushinilya.loftmoney.data.remote.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.leushinilya.loftmoney.data.remote.response.AuthResponse

interface AuthService {

    @GET("./auth")
    fun makeLogin(@Query("social_user_id") socialUserId: String): Single<AuthResponse>

}