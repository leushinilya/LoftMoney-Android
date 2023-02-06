package ru.leushinilya.loftmoney.ui.screens

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.leushinilya.loftmoney.data.repository.PreferencesRepository
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    preferencesRepository: PreferencesRepository
) : ViewModel() {

    val authorized = preferencesRepository.authorizedFlow
    val uiSettings = preferencesRepository.uiSettingsFlow

}