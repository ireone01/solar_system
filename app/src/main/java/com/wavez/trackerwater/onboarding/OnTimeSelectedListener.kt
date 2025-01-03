package com.wavez.trackerwater.onboarding

interface OnTimeSelectedListener {
    fun onTimeSelected(position: Int , timeString:String)
}
enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER
}
