package ru.leushinilya.loftmoney.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.leushinilya.loftmoney.data.repository.PreferencesRepository
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _authorized = MutableStateFlow<Boolean?>(null).apply {
        viewModelScope.launch {
            emit(preferencesRepository.getAuthToken() != null)
        }
    }
    val authorized = _authorized.asStateFlow()
    val uiSettings = preferencesRepository.uiSettingsFlow

}