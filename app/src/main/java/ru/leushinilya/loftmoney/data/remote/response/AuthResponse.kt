package ru.leushinilya.loftmoney.data.remote.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val status: String,
    val id: String,
    @SerializedName("auth_token")
    val authToken: String
)