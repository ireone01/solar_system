package com.wavez.trackerwater.feature.insights.model

data class InsightsItem (
    val title: Int,
    val textColor : Int ,
    val insightsChild : List<InsightsChildItem>
)
