package com.wavez.trackerwater.feature.language.repository

import com.wavez.trackerwater.data.sharedPref.SharedPref
import com.wavez.trackerwater.feature.language.model.Language
import com.wavez.trackerwater.feature.language.providers.LanguageProvider
import com.wavez.trackerwater.feature.language.repository.LanguageRepository

class LanguageRepositoryImpl(
    private val sharedPref: SharedPref,
    private val providerLanguage: LanguageProvider
) : LanguageRepository {

    override fun getLanguages(): List<Language> {
        return providerLanguage.getLanguages(sharedPref.getConfigLanguage())
    }

    override fun saveLanguage(language: Language) {
        sharedPref.setConfigLanguage(language.code)
    }


    override fun getConfigLanguageCode() =  sharedPref.getConfigLanguage()

}