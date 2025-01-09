package com.wavez.trackerwater.extension

import android.content.Context
import com.wavez.trackerwater.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

const val timeFormat1 = "dd-MM-yyyy"
const val timeFormat2 = "HH:mm"
const val timeFormat3 = "dd-MM-yyyy HH:mm"
const val timeFormat4 = "dd-MM"
const val timeFormat5 = "yyyy"
const val timeFormat6 = "dd/MM/yyyy"
const val timeFormat7 = "dd-MM-yyyy-HH-mm-ss"
const val timeFormat8 = "HH:mm:ss dd-MM-yyyy"
const val timeFormat9 = "HH:mm - dd-MM-yyyy"

fun formatDateLocaleDMY(date: Long): String? {
    val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
    return dateFormat.format(date)
}

fun formatTimeLocale(date: Long): String? {
    return formatTimeLocaleFormat(date)
}

fun formatTimeLocaleFormat(date: Long): String? {
    val dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault())
    return dateFormat.format(date)
}

fun getDateTimeOfWeekFormat(
    milliSeconds: Long, timeFormat: String = "EE, dd-MM-yyyy, HH:mm aa"
): String {
    val formatter = SimpleDateFormat(timeFormat, Locale.getDefault())
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds
    return formatter.format(calendar.time)
}

fun getDateTimeOfWeek(milliSeconds: Long, timeFormat: String = "EE, dd-MM-yyyy, HH:mm aa"): String {
    return getDateTimeOfWeekFormat(milliSeconds, timeFormat)
}


fun getDateTimeFormat(milliSeconds: Long, timeFormat: String = "HH:mm dd-MM-yyyy"): String {
    val formatter = SimpleDateFormat(timeFormat, Locale.ENGLISH)
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds
    return formatter.format(calendar.time)
}

fun getDateTime(milliSeconds: Long, timeFormat: String = "HH:mm dd-MM-yyyy"): String {
    return getDateTimeFormat(milliSeconds, timeFormat)
}

fun formatDateLocale(date: Long): String? {
    val dateFormat =
        DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, Locale.getDefault())
    return dateFormat.format(date)
}


fun String.toMillisecond(
    format: String = timeFormat8,
    locale: Locale = Locale.getDefault()
): Long? =
    SimpleDateFormat(format, locale).parse(this)?.time

fun getStartOfDay(timestamp: Long = System.currentTimeMillis()): Long {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = timestamp
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}

fun getStartAndEndOfWeek(timestamp: Long = System.currentTimeMillis()): Pair<Long, Long> {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = timestamp
        set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    val startOfWeek = calendar.timeInMillis
    calendar.add(Calendar.DAY_OF_WEEK, 6)
    val endOfWeek = calendar.timeInMillis
    return Pair(startOfWeek, endOfWeek)
}

fun formatTimeMillis(timeMillis: Long, context: Context): String {
    val seconds = timeMillis / 1000
    val minutes = seconds / 60
    val hours = minutes / 60

    return when {
        hours > 0 -> {
            val remainingMinutes = (minutes % 60) / 60.0
            "%.1f ${context.getString(R.string.hours)}".format(hours + remainingMinutes)
        }
        minutes > 0 -> {
            "$minutes ${context.getString(R.string.minutes)}"
        }
        else -> {
            "$seconds ${context.getString(R.string.seconds)}"
        }
    }
}

fun Calendar.setToMorning(hour: Int = 9, minute: Int = 0): Calendar {
    return apply {
        set(Calendar.HOUR, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.AM_PM, Calendar.AM)
    }
}