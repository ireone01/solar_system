package com.example.solar_system_scope_app.model

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LanguageViewModel(application: Application) : AndroidViewModel(application){
    private val _language = MutableLiveData<String>()
    val language : LiveData<String> get() = _language

    init {
        _language.value = PlanetDataProvider.getLanguage(application.applicationContext)
    }

    fun setLanguage(languageCode: String , context: Context){
        PlanetDataProvider.setLanguage(context,languageCode)
        _language.value = languageCode
    }
}