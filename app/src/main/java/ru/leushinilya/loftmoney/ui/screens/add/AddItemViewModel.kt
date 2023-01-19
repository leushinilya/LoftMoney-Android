package ru.leushinilya.loftmoney.ui.screens.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.leushinilya.loftmoney.R
import ru.leushinilya.loftmoney.data.repository.ItemsRepository
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    var state = MutableLiveData<AddItemViewState>(AddItemViewState.Edit)

    fun onAddClicked(price: Float, name: String, type: String) {
        if (price == 0F || name.isBlank()) {
            state.value = AddItemViewState.Error(res = R.string.fill_all_data)
            return
        }
        viewModelScope.launch {
            try {
                itemsRepository.postItem(price, name, type)
                state.value = AddItemViewState.Success
            } catch (e: Exception) {
                state.value = AddItemViewState.Error(message = e.localizedMessage)
            }
        }
    }
}

sealed class AddItemViewState {
    object Edit : AddItemViewState()
    object Success : AddItemViewState()
    data class Error(val message: String? = null, val res: Int? = null) : AddItemViewState()
}