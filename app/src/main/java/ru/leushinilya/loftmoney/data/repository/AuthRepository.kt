package ru.leushinilya.loftmoney.data.repository

import io.reactivex.Single
import ru.leushinilya.loftmoney.data.remote.response.AuthResponse

interface AuthRepository {

    fun makeLogin(socialUserId: String): Single<AuthResponse>

}