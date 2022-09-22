package ru.leushinilya.loftmoney

const val EXPENSE = 0
const val INCOME = 1

enum class TransactionType (val value: String) {
    EXPENSE("expense"),
    INCOME("income")
}