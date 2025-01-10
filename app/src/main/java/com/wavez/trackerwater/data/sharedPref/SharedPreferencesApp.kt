package com.wavez.trackerwater.data.sharedPref

import android.content.Context
import android.content.SharedPreferences
import com.wavez.trackerwater.util.ConstantUser


class SharedPreferencesApp constructor(private val context: Context) : SharedPref {
    companion object {

        private const val PREFERENCE_FILE_KEY = "shared_preferences_app"
        private const val IS_PREMIUM = "is_premium"
        private const val IS_NEED_SHOW_LANGUAGE = "is_need_show_language"
        private const val LANGUAGE_CONFIG = "language_config"
        private const val IS_NEED_GOTO_WALKTHROUGH = "is_need_goto_walkthrough"
        private const val GENDER_USER = "gender_user"
        private const val TIME_USUALLY_WAKE_UP = "time_usually_wake_up"

    }

    override val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)

    override val editor: SharedPreferences.Editor
        get() = sharedPref.edit()

    override var isPremium: Boolean
        get() = sharedPref.getBoolean(IS_PREMIUM, false)
        set(value) {
            editor.putBoolean(IS_PREMIUM, value).apply()
        }

    override var isNeedShowLanguage: Boolean
        get() = sharedPref.getBoolean(IS_NEED_SHOW_LANGUAGE, true)
        set(value) {
            editor.putBoolean(IS_NEED_SHOW_LANGUAGE, value).apply()
        }

    override fun getConfigLanguage(): String {
        return sharedPref.getString(LANGUAGE_CONFIG, "") ?: ""
    }

    override fun setConfigLanguage(languageCode: String) {
        sharedPref.edit().putString(LANGUAGE_CONFIG, languageCode).apply()
    }

    override var isNeedGotoWalkthrough: Boolean
        get() = sharedPref.getBoolean(IS_NEED_GOTO_WALKTHROUGH, true)
        set(value) {
            editor.putBoolean(IS_NEED_GOTO_WALKTHROUGH, value).apply()
        }

    override var genderUser: Int
        get() = sharedPref.getInt(GENDER_USER, ConstantUser.GENDER_MALE)
        set(value) {
            editor.putInt(GENDER_USER, value).apply()
        }

    override var timeUsuallyWakeUp: Long
        get() = sharedPref.getLong(TIME_USUALLY_WAKE_UP, 0L)
        set(value) {
            editor.putLong(TIME_USUALLY_WAKE_UP, value).apply()
        }
}
