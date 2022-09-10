package ru.leushinilya.loftmoney.screens.old.main.budget

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.leushinilya.loftmoney.LoftApp
import ru.leushinilya.loftmoney.cells.Item
import ru.leushinilya.loftmoney.remote.ItemsAPI
import ru.leushinilya.loftmoney.remote.RemoteItem

class BudgetViewModel : ViewModel() {
    var compositeDisposable = CompositeDisposable()
    var items by mutableStateOf(listOf<Item>())
    var isRefreshing by mutableStateOf(false)
    var messageString = MutableLiveData<String>()
    var messageInt = MutableLiveData<Int>()
    var isEditMode = MutableLiveData(false)
    @JvmField
    var incomesSum = MutableLiveData(0f)
    @JvmField
    var expensesSum = MutableLiveData(0f)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun updateListFromInternet(
        itemsAPI: ItemsAPI,
        currentPosition: Int,
        sharedPreferences: SharedPreferences
    ) {
        isRefreshing = true
        val authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "")
        var type = "income"
        if (currentPosition == 0) type = "expense"
        compositeDisposable.add(
            itemsAPI.getItems(type, authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { remoteItems: ArrayList<RemoteItem> ->
                        isRefreshing = false
                        remoteItems.sortByDescending { it.date }
                        val itemList = ArrayList<Item>()
                        var sum = 0f
                        for (remoteItem in remoteItems) {
                            itemList.add(Item.getInstance(remoteItem))
                            sum += remoteItem.price
                        }
                        items = itemList
                        when (currentPosition) {
                            0 -> expensesSum.postValue(sum)
                            1 -> incomesSum.postValue(sum)
                        }
                    },
                    { throwable: Throwable ->
                        isRefreshing = false
                        messageString.postValue(throwable.localizedMessage)
                    }
                )
        )
    }

    fun removeItem(item: Item, itemsAPI: ItemsAPI, sharedPreferences: SharedPreferences) {
        val authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "")
        val disposable = itemsAPI
            .removeItem(item.id, authToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}) { throwable: Throwable -> messageString.postValue(throwable.localizedMessage) }
        compositeDisposable.add(disposable)
    }

}