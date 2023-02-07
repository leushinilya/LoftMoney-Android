package ru.leushinilya.loftmoney.ui.screens

import ru.leushinilya.loftmoney.R

enum class Screens(val titleRes: Int) {
    LOGIN(-1),
    MAIN(R.string.budget_accounting),
    LIST_EXPENSES(R.string.expences),
    LIST_INCOMES(R.string.incomes),
    DIAGRAM(R.string.balance),
    ADD_ITEM(-1)
}