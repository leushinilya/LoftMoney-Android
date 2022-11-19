package ru.leushinilya.loftmoney.screens.main

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.leushinilya.loftmoney.LoftApp
import ru.leushinilya.loftmoney.TransactionType
import ru.leushinilya.loftmoney.cells.Item

class MainViewModel(application: Application) : AndroidViewModel(application),
    LifecycleEventObserver {

    val expenses = mutableStateListOf<Item>()
    val incomes = mutableStateListOf<Item>()
    var selectedItems = mutableStateListOf<Item>()
    var isRefreshing by mutableStateOf(false)

    private val app = getApplication<LoftApp>()
    private var compositeDisposable = CompositeDisposable()
    private val authToken = app.preferences.getString(LoftApp.AUTH_KEY, "")

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
            app.itemsAPI.getItems(transactionType.value, authToken)
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
        app.itemsAPI.removeItem(item.id, authToken)
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