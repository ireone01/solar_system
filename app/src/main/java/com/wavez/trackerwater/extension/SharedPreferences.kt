package com.wavez.trackerwater.extension

import android.content.SharedPreferences

fun SharedPreferences.getNumericString(key: String, defaultValue: Int): Int =
    getString(key, "")?.toIntOrNull() ?: defaultValue

fun SharedPreferences.getNumericString(key: String, defaultValue: Double): Double =
    getString(key, "")?.toDoubleOrNull() ?: defaultValue