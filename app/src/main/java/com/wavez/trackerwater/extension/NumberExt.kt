package com.wavez.trackerwater.extension

import java.text.DateFormat
import java.util.Locale
import kotlin.math.pow
import kotlin.math.roundToInt

fun convertTimeToHourMinuteSecond(time: Long): String {
    val secondTime = time / 1000
    val seconds = secondTime % 60
    val minutes = ((secondTime - seconds) / 60) % 60
    val hours = (secondTime - seconds - minutes * 60) / (60 * 60)
    return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
}

fun convertTimeToDate(strTime: String): String {
    try {
        val time = strTime.toLong()
        return DateFormat.getDateTimeInstance(
            DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault()
        ).format(time)
    } catch (_: Exception) {
        return ""
    }
}
fun Double.roundTo(decimals: Int): Double {
    val factor = 10.0.pow(decimals)
    return (this * factor).roundToInt() / factor
}