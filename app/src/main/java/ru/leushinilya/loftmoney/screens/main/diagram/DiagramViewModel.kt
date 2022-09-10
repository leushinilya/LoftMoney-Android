package ru.leushinilya.loftmoney.screens.main.diagram

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class DiagramViewModel : ViewModel() {

    val incomes by mutableStateOf("0")
    val expenses by mutableStateOf("0")
    val available by mutableStateOf("0")

}