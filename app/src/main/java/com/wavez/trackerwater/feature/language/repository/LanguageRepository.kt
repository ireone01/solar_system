package com.wavez.trackerwater.feature.language.repository

import com.wavez.trackerwater.feature.language.model.Language

interface LanguageRepository {

    fun getLanguages(): List<Language>

    fun saveLanguage(language: Language)

    fun getConfigLanguageCode(): String

}