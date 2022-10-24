package ru.leushinilya.loftmoney.screens.login

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.leushinilya.loftmoney.LoftApp

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val googleSignIntent = MutableLiveData<Intent>()
    val authorized = MutableLiveData(false)

    private val compositeDisposable = CompositeDisposable()
    private val app = getApplication<LoftApp>()

    fun onLoginClicked(activity: Activity) {
        val signOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val signClient = GoogleSignIn.getClient(activity, signOptions)
        googleSignIntent.postValue(signClient.signInIntent)
    }

    fun onIdReceived(userId: String) {
        compositeDisposable.add(
            app.itemsAPI.makeLogin(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        app.preferences.edit().putString(LoftApp.AUTH_KEY, it.authToken).apply()
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