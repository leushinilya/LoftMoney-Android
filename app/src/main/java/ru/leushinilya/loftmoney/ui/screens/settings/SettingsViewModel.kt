package ru.leushinilya.loftmoney.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.leushinilya.loftmoney.data.repository.PreferencesRepository
import ru.leushinilya.loftmoney.ui.themes.*
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiSettings = MutableStateFlow(UiSettings(LoftColors.BLUE, LoftTypography.NORMAL)).apply {
        viewModelScope.launch {
            val settings = preferencesRepository.getUiSettings()
            emit(settings)
        }
    }
    val uiSettings = _uiSettings.asStateFlow()

    fun onColorSchemeSelected(colors: LoftColors) {
        viewModelScope.launch {
            val settings = _uiSettings.value.copy(colors = colors)
            _uiSettings.emit(settings)
        }
    }

    fun onTypographySelected(typography: LoftTypography) {
        viewModelScope.launch {
            val settings = _uiSettings.value.copy(typography = typography)
            _uiSettings.emit(settings)
        }
    }

    fun onSaveButtonClicked() {
        viewModelScope.launch {
            preferencesRepository.setUiSettings(uiSettings.value)
        }
    }
}