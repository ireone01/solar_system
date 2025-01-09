package com.wavez.trackerwater.feature.language

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lingvo.base_common.model.DataResult
import com.wavez.trackerwater.feature.language.model.Language
import com.wavez.trackerwater.feature.language.repository.LanguageRepository
import com.wavez.trackerwater.data.sharedPref.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val languageRepository: LanguageRepository,
    private val sharedPref: SharedPref
) : ViewModel() {

    private val _selectedUiLanguage = MutableLiveData<Language>()
    val selectedUiLanguage: LiveData<Language> = _selectedUiLanguage

    private val _uiLanguageState = MutableLiveData<DataResult<List<Language>>>()
    val uiLanguageState: LiveData<DataResult<List<Language>>> = _uiLanguageState

    fun setLanguageDefault(languages: List<Language>) {
        val configLanguage = languageRepository.getConfigLanguageCode()
        languages.find { it.code == configLanguage }?.let { onLanguageChanged(it) }
    }

    fun getUiLanguages() {
        viewModelScope.launch {
            _uiLanguageState.value = DataResult.Loading
            _uiLanguageState.value = DataResult.Success(languageRepository.getLanguages())
        }
    }

    fun onLanguageChanged(uiLanguage: Language) {
        _selectedUiLanguage.value = uiLanguage
    }

    fun getConfig(): String {
        return languageRepository.getConfigLanguageCode()
    }

    fun saveConfig() {
        viewModelScope.launch {
            _selectedUiLanguage.value?.let { languageRepository.saveLanguage(it) }
        }
    }

    fun saveNeedLanguage() {
        sharedPref.isNeedShowLanguage = false
        Log.d("hehehe", "initConfig: "+sharedPref.isNeedShowLanguage + sharedPref.isNeedShowLanguage)

    }
}