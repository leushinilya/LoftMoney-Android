package ru.leushinilya.loftmoney.ui.screens.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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

    val googleSignIntent = MutableLiveData<Intent>()
    val authorized = MutableLiveData(false)

    fun onLoginClicked(activity: Activity) {
        val signOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val signClient = GoogleSignIn.getClient(activity, signOptions)
        googleSignIntent.postValue(signClient.signInIntent)
    }

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