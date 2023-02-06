package ru.leushinilya.loftmoney.ui.screens.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.leushinilya.loftmoney.TransactionType
import ru.leushinilya.loftmoney.cells.Item
import ru.leushinilya.loftmoney.data.remote.entity.Balance
import ru.leushinilya.loftmoney.data.repository.ItemsRepository
import ru.leushinilya.loftmoney.data.repository.PreferencesRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    var selectedItems = mutableStateListOf<Item>()
    var isRefreshing by mutableStateOf(false)
    val expenses = mutableStateListOf<Item>().apply {
        updateExpenses()
    }
    val incomes = mutableStateListOf<Item>().apply {
        updateIncomes()
    }
    val balance = MutableStateFlow<Balance?>(null).apply {
        getBalance()
    }

    private fun getItems(transactionType: TransactionType, onSuccess: (List<Item>) -> Unit) {
        viewModelScope.launch {
            try {
                val remoteItems = itemsRepository.getItems(transactionType.value)
                onSuccess(remoteItems.map { Item.getInstance(it) })
            } catch (e: Exception) {
                isRefreshing = false
            }
        }
    }

    fun updateIncomes() {
        isRefreshing = true
        getItems(TransactionType.INCOME) {
            incomes.clear()
            incomes.addAll(it)
            isRefreshing = false
        }
    }

    fun updateExpenses() {
        isRefreshing = true
        getItems(TransactionType.EXPENSE) {
            expenses.clear()
            expenses.addAll(it)
            isRefreshing = false
        }
    }

    fun onItemSelectionChanged(item: Item) {
        if (item in selectedItems) {
            selectedItems.remove(item)
        } else {
            selectedItems.add(item)
        }
    }

    fun onCancelClicked() {
        selectedItems.clear()
    }

    fun onRemoveClicked() {
        selectedItems.forEach {
            removeItem(it)
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            preferencesRepository.clearAll()
        }
    }

    private fun removeItem(item: Item) {
        viewModelScope.launch {
            try {
                itemsRepository.removeItem(item.id)
                selectedItems.remove(item)
                expenses.remove(item)
                incomes.remove(item)
            } catch (e: Exception) {
                selectedItems.clear()
            }
        }
    }

    private fun getBalance() {
        viewModelScope.launch {
            try {
                val balanceValue = itemsRepository.getBalance()
                balance.emit(balanceValue)
            } catch (e: Exception) {
//                TODO(show error)
            }
        }
    }

}