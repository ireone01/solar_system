package com.wavez.trackerwater.util

import android.content.Context
import android.content.SharedPreferences

object SPUtils {

    private const val PREFERENCE = "PREFERENCE"
    private const val SHARED_PREFS_NAME = "SHARED_PREFS_NAME"
    public const val REMINDER = "REMINDER"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE)
    }

    fun getPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setString(context: Context, key: String, value: String) {
        getPreferences(context).edit().apply {
            putString(key, value)
            apply()
        }
    }

    fun getString(context: Context, key: String, defaultValue: String): String {
        return getPreferences(context).getString(key, defaultValue) ?: defaultValue
    }

    fun setLong(context: Context, key: String, value: Long) {
        getPreferences(context).edit().apply {
            putLong(key, value)
            apply()
        }
    }

    fun getLong(context: Context, key: String, defaultValue: Long): Long {
        return getPreferences(context).getLong(key, defaultValue)
    }

    fun setInt(context: Context, key: String, value: Int) {
        getPreferences(context).edit().apply {
            putInt(key, value)
            apply()
        }
    }

    fun getInt(context: Context, key: String, defaultValue: Int): Int {
        return getPreferences(context).getInt(key, defaultValue)
    }

    fun setBoolean(context: Context, key: String, value: Boolean) {
        getPreferences(context).edit().apply {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBoolean(context: Context, key: String, defaultValue: Boolean): Boolean {
        return getPreferences(context).getBoolean(key, defaultValue)
    }
}
