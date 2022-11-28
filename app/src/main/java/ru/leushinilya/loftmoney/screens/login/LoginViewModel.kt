package ru.leushinilya.loftmoney.screens.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
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

    private val compositeDisposable = CompositeDisposable()

    fun onLoginClicked(activity: Activity) {
        val signOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val signClient = GoogleSignIn.getClient(activity, signOptions)
        googleSignIntent.postValue(signClient.signInIntent)
    }

    fun onIdReceived(userId: String) {
        compositeDisposable.add(
            authRepository.makeLogin(userId)
                .flatMapCompletable {
                    preferencesRepository.setAuthToken(it.authToken)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        authorized.postValue(true)
                    },
                    {}
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}