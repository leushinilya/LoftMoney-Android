package ru.leushinilya.loftmoney.screens.main

import android.app.Application
import androidx.compose.runtime.getValue
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
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.TransactionType
import ru.leushinilya.loftmoney.cells.Item

class MainViewModel(application: Application) : AndroidViewModel(application),
    LifecycleEventObserver {

    var expenses by mutableStateOf(listOf<Item>())
    var incomes by mutableStateOf(listOf<Item>())
    var isRefreshing by mutableStateOf(false)

    private val app = getApplication<LoftApp>()
    private val compositeDisposable = CompositeDisposable()

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                getItems(TransactionType.EXPENSE) {
                    expenses = it
                }
                getItems(TransactionType.INCOME) {
                    incomes = it
                }
            }
            Lifecycle.Event.ON_DESTROY -> {
                compositeDisposable.dispose()
            }
            else -> {}
        }
    }

    private fun getItems(transactionType: TransactionType, onSuccess: (List<Item>) -> Unit) {
        val authToken = app.getSharedPreferences(app.getString(R.string.app_name), 0)
            .getString(LoftApp.AUTH_KEY, "")
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
                        isRefreshing = false
                    },
                    {}
                )
        )
    }

}