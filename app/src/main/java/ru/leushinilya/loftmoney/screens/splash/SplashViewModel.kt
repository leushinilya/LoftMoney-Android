package ru.leushinilya.loftmoney.screens.splash

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.leushinilya.loftmoney.LoftApp

class SplashViewModel(application: Application) : AndroidViewModel(application),
    LifecycleEventObserver {

    val state = MutableLiveData<SplashState>(SplashState.WaitingState)

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        val preferences = getApplication<LoftApp>().getSharedPreferences(LoftApp.AUTH_KEY, 0)
        val accessToken = preferences.getString(LoftApp.AUTH_KEY, "")
        viewModelScope.launch {
            state.value = SplashState.WaitingState
            delay(1000)
            if (accessToken.isNullOrEmpty()) {
                state.value = SplashState.NotAuthorizedState
            } else {
                state.value = SplashState.AuthorizedState
            }
        }
    }

}

sealed class SplashState {
    object WaitingState : SplashState()
    object AuthorizedState : SplashState()
    object NotAuthorizedState : SplashState()
}