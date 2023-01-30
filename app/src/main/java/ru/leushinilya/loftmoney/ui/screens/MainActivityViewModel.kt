package ru.leushinilya.loftmoney.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.leushinilya.loftmoney.data.repository.PreferencesRepository
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel(), LifecycleEventObserver {

    var authorized: Boolean? by mutableStateOf(null)
    val uiSettings = preferencesRepository.uiSettingsFlow

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModelScope.launch {
                    authorized = try {
                        preferencesRepository.getAuthToken() != null
                    } catch (e: Exception) {
                        false
                    }
                }
            }
            else -> {}
        }
    }

}