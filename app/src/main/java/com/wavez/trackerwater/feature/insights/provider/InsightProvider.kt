package com.wavez.trackerwater.feature.insights.provider

import com.wavez.trackerwater.R
import com.wavez.trackerwater.feature.insights.model.InsightsChildItem
import com.wavez.trackerwater.feature.insights.model.InsightsItem

object InsightProvider {
    private val INSIGHT_WATER_1 = InsightsChildItem(
        R.string.water_drinking_title_1,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_wt_start
    )
    private val INSIGHT_WATER_2 = InsightsChildItem(
        R.string.water_drinking_title_2,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_wt_start
    )
    private val INSIGHT_WATER_3 = InsightsChildItem(
        R.string.water_drinking_title_3,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_wt_start
    )
    private val INSIGHT_WATER_4 = InsightsChildItem(
        R.string.water_drinking_title_4,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_wt_start
    )
    private val INSIGHT_WATER_5 = InsightsChildItem(
        R.string.water_drinking_title_5,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_wt_start
    )
    private val INSIGHT_WATER_6 = InsightsChildItem(
        R.string.water_drinking_title_6,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_wt_start
    )
    private val INSIGHT_WATER_7 = InsightsChildItem(
        R.string.water_drinking_title_7,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_wt_start
    )
    private val INSIGHT_WATER_8 = InsightsChildItem(
        R.string.water_drinking_title_8,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_wt_start
    )
    private val INSIGHT_WATER_9 = InsightsChildItem(
        R.string.water_drinking_title_9,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_wt_start
    )
    private val WATER_DRINKING = InsightsItem(
        R.string.water_drinking, listOf(
            INSIGHT_WATER_1,
            INSIGHT_WATER_2,
            INSIGHT_WATER_3,
            INSIGHT_WATER_4,
            INSIGHT_WATER_5,
            INSIGHT_WATER_6,
            INSIGHT_WATER_7,
            INSIGHT_WATER_8,
            INSIGHT_WATER_9
        )
    )
    private val BEAUTY_SKINCARE_1 = InsightsChildItem(
        R.string.beauty_skincare_title_1,
        R.color.danger_200,
        R.color.danger_50,
        R.drawable.ic_wt_start
    )
    private val BEAUTY_SKINCARE_2 = InsightsChildItem(
        R.string.beauty_skincare_title_2,
        R.color.danger_200,
        R.color.danger_50,
        R.drawable.ic_wt_start
    )
    private val BEAUTY_SKINCARE_3 = InsightsChildItem(
        R.string.beauty_skincare_title_3,
        R.color.danger_200,
        R.color.danger_50,
        R.drawable.ic_wt_start
    )
    private val BEAUTY_SKINCARE_4 = InsightsChildItem(
        R.string.beauty_skincare_title_4,
        R.color.danger_200,
        R.color.danger_50,
        R.drawable.ic_wt_start
    )

    private val BEAUTY_SKINCARE = InsightsItem(
        R.string.beauty_skincare, listOf(
            BEAUTY_SKINCARE_1,
            BEAUTY_SKINCARE_2,
            BEAUTY_SKINCARE_3,
            BEAUTY_SKINCARE_4
        )
    )


    private val SELF_CARE_1 = InsightsChildItem(
        R.string.self_care_title_1,
        R.color.warning_300,
        R.color.warning_100,
        R.drawable.ic_wt_start
    )
    private val SELF_CARE_2 = InsightsChildItem(
        R.string.self_care_title_2,
        R.color.warning_300,
        R.color.warning_100,
        R.drawable.ic_wt_start
    )
    private val SELF_CARE_3 = InsightsChildItem(
        R.string.self_care_title_3,
        R.color.warning_300,
        R.color.warning_100,
        R.drawable.ic_wt_start
    )
    private val SELF_CARE_4 = InsightsChildItem(
        R.string.self_care_title_4,
        R.color.warning_300,
        R.color.warning_100,
        R.drawable.ic_wt_start
    )
    private val SELF_CARE_5 = InsightsChildItem(
        R.string.self_care_title_5,
        R.color.warning_300,
        R.color.warning_100,
        R.drawable.ic_wt_start
    )
    private val SELF_CARE_6 = InsightsChildItem(
        R.string.self_care_title_6,
        R.color.warning_300,
        R.color.warning_100,
        R.drawable.ic_wt_start
    )

    private val SELF_CARE = InsightsItem(
        R.string.self_care, listOf(
            SELF_CARE_1,
            SELF_CARE_2,
            SELF_CARE_3,
            SELF_CARE_4,
            SELF_CARE_5,
            SELF_CARE_6
        )
    )



    private val HEALTH_LIFE_STYLE_1 = InsightsChildItem(
        R.string.health_life_style_title_1,
        R.color.secondary_200,
        R.color.secondary_100,
        R.drawable.ic_wt_start
    )
    private val HEALTH_LIFE_STYLE_2 = InsightsChildItem(
        R.string.health_life_style_title_2,
        R.color.secondary_200,
        R.color.secondary_100,
        R.drawable.ic_wt_start
    )
    private val HEALTH_LIFE_STYLE_3 = InsightsChildItem(
        R.string.health_life_style_title_3,
        R.color.secondary_200,
        R.color.secondary_100,
        R.drawable.ic_wt_start
    )
    private val HEALTH_LIFE_STYLE_4 = InsightsChildItem(
        R.string.health_life_style_title_4,
        R.color.secondary_200,
        R.color.secondary_100,
        R.drawable.ic_wt_start
    )
    private val HEALTH_LIFE_STYLE_5 = InsightsChildItem(
        R.string.health_life_style_title_5,
        R.color.secondary_200,
        R.color.secondary_100,
        R.drawable.ic_wt_start
    )
    private val HEALTH_LIFE_STYLE_6 = InsightsChildItem(
        R.string.health_life_style_title_6,
        R.color.secondary_200,
        R.color.secondary_100,
        R.drawable.ic_wt_start
    )

    private val HEALTH_LIFE_STYLE = InsightsItem(
        R.string.health_life_style, listOf(
            HEALTH_LIFE_STYLE_1,
            HEALTH_LIFE_STYLE_2,
            HEALTH_LIFE_STYLE_3,
            HEALTH_LIFE_STYLE_4,
            HEALTH_LIFE_STYLE_5,
            HEALTH_LIFE_STYLE_6
        )
    )


    val ALL_DATA_INSIGHT = listOf(WATER_DRINKING , BEAUTY_SKINCARE, SELF_CARE, HEALTH_LIFE_STYLE)
}