package ru.leushinilya.loftmoney.screens.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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

    private var compositeDisposable = CompositeDisposable()

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                compositeDisposable = CompositeDisposable()
                updateExpenses()
                updateIncomes()
            }
            Lifecycle.Event.ON_DESTROY -> {
                compositeDisposable.dispose()
            }
            else -> {}
        }
    }

    private fun getItems(transactionType: TransactionType, onSuccess: (List<Item>) -> Unit) {
        compositeDisposable.add(
            itemsRepository.getItems(transactionType.value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        onSuccess(
                            it.map { remoteItem ->
                                Item.getInstance(remoteItem)
                            }
                        )
                    },
                    {
                        isRefreshing = false
                    }
                )
        )
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

    private fun removeItem(item: Item) = compositeDisposable.add(
        itemsRepository.removeItem(item.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    selectedItems.remove(item)
                    expenses.remove(item)
                    incomes.remove(item)
                },
                {}
            )
    )

}