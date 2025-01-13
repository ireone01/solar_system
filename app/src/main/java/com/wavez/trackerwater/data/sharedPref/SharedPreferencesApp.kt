package com.wavez.trackerwater.data.sharedPref

import android.content.Context
import android.content.SharedPreferences
import com.wavez.trackerwater.util.ConstantUser
import com.wavez.trackerwater.util.TextUtils


class SharedPreferencesApp constructor(private val context: Context) : SharedPref {
    companion object {

        private const val PREFERENCE_FILE_KEY = "shared_preferences_app"
        private const val IS_PREMIUM = "is_premium"
        private const val IS_NEED_SHOW_LANGUAGE = "is_need_show_language"
        private const val LANGUAGE_CONFIG = "language_config"
        private const val IS_NEED_GOTO_WALKTHROUGH = "is_need_goto_walkthrough"
        private const val IS_REMINDER = "is_reminder"
        private const val IS_TYPE_DRINK_CUP = "is_type_drink_cup"
        private const val GENDER_USER = "gender_user"
        private const val TIME_USUALLY_WAKE_UP = "time_usually_wake_up"
        private const val IS_SKIP = "IS_SKIP"
        private const val IS_STOP = "IS_STOP"
        private const val REMINDER_MODE = "REMINDER_MODE"

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

    override var isStop: Boolean
        get() = sharedPref.getBoolean(IS_STOP, false)
        set(value) {
            editor.putBoolean(IS_STOP, value).apply()
        }

    override var isSkip: Boolean
        get() = sharedPref.getBoolean(IS_SKIP, false)
        set(value) {
            editor.putBoolean(IS_SKIP, value).apply()
        }

    override var reminderMode: Int
        get() =sharedPref.getInt(REMINDER_MODE, TextUtils.STANDARD)
        set(value) {
            editor.putInt(REMINDER_MODE, value).apply()
        }

    override var isTypeDrinkCup: Boolean
        get() = sharedPref.getBoolean(IS_TYPE_DRINK_CUP, true)
        set(value) {
            editor.putBoolean(IS_TYPE_DRINK_CUP, value).apply()
        }
    override var isReminder: Boolean
        get() = sharedPref.getBoolean(IS_REMINDER, false)
        set(value) {
            editor.putBoolean(IS_REMINDER, value).apply()
        }
}
