package ru.leushinilya.loftmoney.data.repository

import io.reactivex.Single
import ru.leushinilya.loftmoney.data.remote.response.AuthResponse
import ru.leushinilya.loftmoney.data.remote.service.AuthService
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {

    override fun makeLogin(socialUserId: String): Single<AuthResponse> =
        authService.makeLogin(socialUserId)

}