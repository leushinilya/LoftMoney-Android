package ru.leushinilya.loftmoney.screens.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.leushinilya.loftmoney.TransactionType
import ru.leushinilya.loftmoney.cells.Item
import ru.leushinilya.loftmoney.data.repository.ItemsRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository
) : ViewModel(), LifecycleEventObserver {

    val expenses = mutableStateListOf<Item>()
    val incomes = mutableStateListOf<Item>()
    var selectedItems = mutableStateListOf<Item>()
    var isRefreshing by mutableStateOf(false)

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_RESUME) {
            updateExpenses()
            updateIncomes()
        }
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

}