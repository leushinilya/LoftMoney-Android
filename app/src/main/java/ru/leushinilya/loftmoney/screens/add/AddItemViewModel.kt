package ru.leushinilya.loftmoney.screens.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.data.repository.ItemsRepository
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    var state = MutableLiveData<AddItemViewState>(AddItemViewState.Edit)

    private val compositeDispose = CompositeDisposable()

    fun onAddClicked(price: Float, name: String, type: String) {
        if (price == 0F || name.isBlank()) {
            state.value = AddItemViewState.Error(res = R.string.fill_all_data)
            return
        }
        compositeDispose.add(
            itemsRepository.postItem(price, name, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        state.value = AddItemViewState.Success
                    },
                    {
                        state.value = AddItemViewState.Error(message = it.localizedMessage)
                    }
                )
        )
    }
}

sealed class AddItemViewState {
    object Edit : AddItemViewState()
    object Success : AddItemViewState()
    data class Error(val message: String? = null, val res: Int? = null) : AddItemViewState()
}