package ru.leushinilya.loftmoney.screens.add

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.leushinilya.loftmoney.LoftApp
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.TransactionType

class AddItemViewModel(application: Application) : AndroidViewModel(application) {

    var state by mutableStateOf<AddItemViewState>(AddItemViewState.Edit)

    private val compositeDispose = CompositeDisposable()
    private val app = getApplication<LoftApp>()

    fun onAddClicked(price: Float, name: String, type: TransactionType) {
        if (price == 0F || name.isBlank()) {
            state = AddItemViewState.Error(app.getString(R.string.fill_all_data))
            return
        }
        val authToken = app.getSharedPreferences(app.getString(R.string.app_name), 0)
            .getString(LoftApp.AUTH_KEY, "")
        compositeDispose.add(
            app.itemsAPI.postItems(price, name, type.value, authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        state = AddItemViewState.Success
                    },
                    {
                        state = AddItemViewState.Error(it.localizedMessage)
                    }
                )
        )
    }
}

sealed class AddItemViewState {
    object Edit : AddItemViewState()
    object Success : AddItemViewState()
    data class Error(val message: String?) : AddItemViewState()
}