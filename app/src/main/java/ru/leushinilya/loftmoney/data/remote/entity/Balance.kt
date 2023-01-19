package ru.leushinilya.loftmoney.data.remote.entity

import com.google.gson.annotations.SerializedName

data class Balance(
    val status: String,
    val balance: Double,
    @SerializedName("total_income")
    val totalIncomes: Double,
    @SerializedName("total_expenses")
    val totalExpenses: Double
)
