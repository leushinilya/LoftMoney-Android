package ru.leushinilya.loftmoney.screens.main.budget

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import androidx.lifecycle.MutableLiveData
import ru.leushinilya.loftmoney.remote.ItemsAPI
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ru.leushinilya.loftmoney.LoftApp
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.leushinilya.loftmoney.cells.Item
import ru.leushinilya.loftmoney.remote.RemoteItem
import java.util.*

class BudgetViewModel : ViewModel() {
    var compositeDisposable = CompositeDisposable()
    var items by mutableStateOf(listOf<Item>())
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
        val authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "")
        var type = "income"
        if (currentPosition == 0) type = "expense"
        val disposable = itemsAPI.getItems(type, authToken)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ remoteItems: ArrayList<RemoteItem> ->
//                        sorting remoteItems list by date
                val comparator =
                    Comparator { o1: RemoteItem, o2: RemoteItem -> o1.date.compareTo(o2.date) }
                Collections.sort(remoteItems, comparator)
                val itemList = ArrayList<Item>()
                var sum = 0f
                for (remoteItem in remoteItems) {
                    itemList.add(Item.getInstance(remoteItem))
                    sum += remoteItem.price
                }
                items = itemList
                if (currentPosition == 0) expensesSum.postValue(sum) else if (currentPosition == 1) incomesSum.postValue(
                    sum
                )
            }) { throwable: Throwable -> messageString.postValue(throwable.localizedMessage) }
        compositeDisposable.add(disposable)
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