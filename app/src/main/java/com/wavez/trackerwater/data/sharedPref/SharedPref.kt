package com.wavez.trackerwater.data.sharedPref

import android.content.SharedPreferences

interface SharedPref {
    val sharedPref: SharedPreferences

    val editor: SharedPreferences.Editor

    var isPremium: Boolean

    var isNeedShowLanguage: Boolean

    fun getConfigLanguage(): String

    fun setConfigLanguage(languageCode: String)

    var isNeedGotoWalkthrough: Boolean



}