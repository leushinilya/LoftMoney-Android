package ru.leushinilya.loftmoney.ui.screens.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.leushinilya.loftmoney.data.repository.AuthRepository
import ru.leushinilya.loftmoney.data.repository.PreferencesRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    val authorized = MutableLiveData(false)

    fun onIdReceived(userId: String) {
        viewModelScope.launch {
            try {
                val authResponse = authRepository.makeLogin(userId)
                preferencesRepository.setAuthToken(authResponse.authToken)
                authorized.postValue(true)
            } catch (e: Exception) {
                authorized.postValue(false)
            }
        }
    }

}