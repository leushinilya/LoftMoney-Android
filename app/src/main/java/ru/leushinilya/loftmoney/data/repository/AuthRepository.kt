package ru.leushinilya.loftmoney.data.repository

import ru.leushinilya.loftmoney.data.remote.response.AuthResponse

interface AuthRepository {

    suspend fun makeLogin(socialUserId: String): AuthResponse

}