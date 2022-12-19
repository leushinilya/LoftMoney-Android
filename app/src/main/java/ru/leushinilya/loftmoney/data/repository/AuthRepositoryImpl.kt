package ru.leushinilya.loftmoney.data.repository

import ru.leushinilya.loftmoney.data.remote.response.AuthResponse
import ru.leushinilya.loftmoney.data.remote.service.AuthService
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {

    override suspend fun makeLogin(socialUserId: String): AuthResponse =
        authService.makeLogin(socialUserId)

}