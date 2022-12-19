package ru.leushinilya.loftmoney.data.remote.service

import retrofit2.http.GET
import retrofit2.http.Query
import ru.leushinilya.loftmoney.data.remote.response.AuthResponse

interface AuthService {

    @GET("./auth")
    suspend fun makeLogin(@Query("social_user_id") socialUserId: String): AuthResponse

}