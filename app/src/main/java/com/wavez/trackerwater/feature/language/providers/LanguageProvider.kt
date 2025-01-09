package com.wavez.trackerwater.feature.language.providers

import android.content.Context
import com.wavez.trackerwater.R
import com.wavez.trackerwater.data.sharedPref.SharedPref
import com.wavez.trackerwater.feature.language.model.Language
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LanguageProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPref: SharedPref
) {
    companion object {

        const val LANGUAGE_DEFAULT_OF_DEVICE = "zzz"

    }

    private val defaultLang = Language(LANGUAGE_DEFAULT_OF_DEVICE, "Device default", R.drawable.ic_flag_default)
    private val enLang = Language("en", "English", R.drawable.flag_lan_gb)
    private val esLang = Language("es", "Spanish", R.drawable.flag_lan_es)
    private val deLang = Language("de", "German", R.drawable.flag_lan_be)
    private val ptLang = Language("pt", "Portuguese", R.drawable.flag_lan_pt)
    private val hiLang = Language("hi", "Hindi", R.drawable.flag_lan_in)
    private val viLang = Language("vi", "Vietnamese", R.drawable.flag_lan_vn)
    private val arLang = Language("ar", "Arabic", R.drawable.flag_lan_ae)
    private val frLang = Language("fr", "French", R.drawable.flag_lan_fr)
    private val inLang = Language("in", "Indonesia", R.drawable.flag_lan_id)
    private val filLang = Language("fil", "Filipino", R.drawable.flag_lan_ph)
    private val thLang = Language("th", "Thai", R.drawable.flag_lan_th)
    private val zhCNLang = Language("zh-CN", "Chinese (Simplified)", R.drawable.flag_lan_cn)
    private val javLang = Language("ja", "Japanese", R.drawable.flag_lan_jp)
    private val koLang = Language("ko", "Korean", R.drawable.flag_lan_kr)
    private val itLang = Language("it", "Italian", R.drawable.flag_lan_it)
    private val nlLang = Language("nl", "Dutch", R.drawable.flag_lan_nl)
    private val trLang = Language("tr", "Turkish", R.drawable.flag_lan_tr)

    private val availableLanguages = arrayListOf(
        enLang, esLang, deLang, ptLang, hiLang, viLang, arLang, frLang, inLang,
        filLang, thLang, zhCNLang, javLang, koLang, itLang, nlLang, trLang
    )

    fun getLanguages(deviceLanguage: String): List<Language> {
        val deviceLanguageInList = availableLanguages.find { it.code == deviceLanguage }
        val sortedList = availableLanguages.toMutableList()
        if (deviceLanguageInList != null) {
            sortedList.remove(deviceLanguageInList)
            sortedList.add(0, deviceLanguageInList)
        } else {
            sortedList.add(0, defaultLang)
        }
        return sortedList
    }
}
