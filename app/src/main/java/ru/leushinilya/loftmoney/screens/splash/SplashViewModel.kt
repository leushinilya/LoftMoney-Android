package ru.leushinilya.loftmoney.screens.splash

import android.app.Application
import androidx.lifecycle.*
import ru.leushinilya.loftmoney.LoftApp

class SplashViewModel(application: Application) : AndroidViewModel(application),
    LifecycleEventObserver {

    val state = MutableLiveData<SplashState>(SplashState.WaitingState)

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event != Lifecycle.Event.ON_RESUME) return
        val preferences = getApplication<LoftApp>().getSharedPreferences(LoftApp.AUTH_KEY, 0)
        val accessToken = preferences.getString(LoftApp.AUTH_KEY, "")
        if (accessToken.isNullOrEmpty()) {
            state.value = SplashState.NotAuthorizedState
        } else {
            state.value = SplashState.AuthorizedState
        }
    }

}

sealed class SplashState {
    object WaitingState : SplashState()
    object AuthorizedState : SplashState()
    object NotAuthorizedState : SplashState()
}