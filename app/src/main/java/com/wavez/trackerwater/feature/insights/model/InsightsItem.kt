package com.wavez.trackerwater.feature.insights.model

import java.io.Serializable

data class InsightsItem (
    val title: Int,
    val insightsChild : List<InsightsChildItem>
)
