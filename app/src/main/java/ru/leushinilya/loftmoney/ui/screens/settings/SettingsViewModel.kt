package ru.leushinilya.loftmoney.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.leushinilya.loftmoney.data.repository.PreferencesRepository
import ru.leushinilya.loftmoney.ui.themes.LoftColors
import ru.leushinilya.loftmoney.ui.themes.LoftTypography
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    fun onColorSchemeSelected(colors: LoftColors) {
        viewModelScope.launch {
            preferencesRepository.setColors(colors)
        }
    }

    fun onTypographySelected(typography: LoftTypography) {
        viewModelScope.launch {
            preferencesRepository.setTypography(typography)
        }
    }

}