package ru.leushinilya.loftmoney.screens.main.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.leushinilya.loftmoney.cells.Item

class ListViewModel : ViewModel() {
    var items by mutableStateOf(listOf<Item>())
    var isRefreshing by mutableStateOf(false)
}