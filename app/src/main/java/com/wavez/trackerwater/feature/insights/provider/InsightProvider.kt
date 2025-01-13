package com.wavez.trackerwater.feature.insights.provider

import com.wavez.trackerwater.R
import com.wavez.trackerwater.feature.insights.model.InsightsChildItem
import com.wavez.trackerwater.feature.insights.model.InsightsItem
import com.wavez.trackerwater.feature.insights.model.InsightsParts

object InsightProvider {
    private val INSIGHT_WATER_1 = InsightsChildItem(
        R.string.water_drinking_title_1,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.water_drinking_title_1, R.string.Wd_Item1_Opening_des),
            InsightsParts(R.string.Wd_Item1_Screen_1_title, R.string.Wd_Item1_Screen_1_des),
            InsightsParts(R.string.Wd_Item1_Screen_2_title, R.string.Wd_Item1_Screen_2_des),
            InsightsParts(R.string.Wd_Item1_Screen_3_title, R.string.Wd_Item1_Screen_3_des),
            InsightsParts(R.string.Wd_Item1_Screen_4_title, R.string.Wd_Item1_Screen_4_des),
            InsightsParts(R.string.Wd_Item1_Screen_5_title, R.string.Wd_Item1_Screen_5_des),
            InsightsParts(R.string.Wd_Item1_Screen_6_title, R.string.Wd_Item1_Screen_6_des)
        )
    )
    private val INSIGHT_WATER_2 = InsightsChildItem(
        R.string.water_drinking_title_2,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.water_drinking_title_2, R.string.Wd_Item2_Opening_des),
            InsightsParts(R.string.Wd_Item2_Screen_1_title, R.string.Wd_Item2_Screen_1_des),
            InsightsParts(R.string.Wd_Item2_Screen_2_title, R.string.Wd_Item2_Screen_2_des),
            InsightsParts(R.string.Wd_Item2_Screen_3_title, R.string.Wd_Item2_Screen_3_des),
            InsightsParts(R.string.Wd_Item2_Screen_4_title, R.string.Wd_Item2_Screen_4_des),
            InsightsParts(R.string.Wd_Item2_Screen_5_title, R.string.Wd_Item2_Screen_5_des)
        )
    )
    private val INSIGHT_WATER_3 = InsightsChildItem(
        R.string.water_drinking_title_3,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.water_drinking_title_3, R.string.Wd_Item3_Opening_des),
            InsightsParts(R.string.Wd_Item3_Screen_1_title, R.string.Wd_Item3_Screen_1_des),
            InsightsParts(R.string.Wd_Item3_Screen_2_title, R.string.Wd_Item3_Screen_2_des),
            InsightsParts(R.string.Wd_Item3_Screen_3_title, R.string.Wd_Item3_Screen_3_des),
            InsightsParts(R.string.Wd_Item3_Screen_4_title, R.string.Wd_Item3_Screen_4_des),
            InsightsParts(R.string.Wd_Item3_Screen_5_title, R.string.Wd_Item3_Screen_5_des),
            InsightsParts(R.string.Wd_Item3_Screen_6_title, R.string.Wd_Item3_Screen_6_des),
            InsightsParts(R.string.Wd_Item3_Screen_7_title, R.string.Wd_Item3_Screen_7_des)
        )
    )
    private val INSIGHT_WATER_4 = InsightsChildItem(
        R.string.water_drinking_title_4,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.water_drinking_title_4, R.string.Wd_Item4_Opening_des),
            InsightsParts(R.string.Wd_Item4_Screen_1_title, R.string.Wd_Item4_Screen_1_des),
            InsightsParts(R.string.Wd_Item4_Screen_2_title, R.string.Wd_Item4_Screen_2_des),
            InsightsParts(R.string.Wd_Item4_Screen_3_title, R.string.Wd_Item4_Screen_3_des),
            InsightsParts(R.string.Wd_Item4_Screen_4_title, R.string.Wd_Item4_Screen_4_des),

            )
    )
    private val INSIGHT_WATER_5 = InsightsChildItem(
        R.string.water_drinking_title_5,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.water_drinking_title_5, R.string.Wd_Item5_Opening_des),
            InsightsParts(R.string.Wd_Item5_Screen_1_title, R.string.Wd_Item5_Screen_1_des),
            InsightsParts(R.string.Wd_Item5_Screen_2_title, R.string.Wd_Item5_Screen_2_des),
            InsightsParts(R.string.Wd_Item5_Screen_3_title, R.string.Wd_Item5_Screen_3_des),
            InsightsParts(R.string.Wd_Item5_Screen_4_title, R.string.Wd_Item5_Screen_4_des),
            InsightsParts(R.string.Wd_Item5_Screen_5_title, R.string.Wd_Item5_Screen_5_des),
            InsightsParts(R.string.Wd_Item5_Screen_6_title, R.string.Wd_Item5_Screen_6_des),
            InsightsParts(R.string.Wd_Item5_Screen_7_title, R.string.Wd_Item5_Screen_7_des),
            InsightsParts(R.string.Wd_Item5_Screen_8_title, R.string.Wd_Item5_Screen_8_des),
            InsightsParts(R.string.Wd_Item5_Screen_9_title, R.string.Wd_Item5_Screen_9_des)


        )
    )
    private val INSIGHT_WATER_6 = InsightsChildItem(
        R.string.water_drinking_title_6,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.water_drinking_title_6, R.string.Wd_Item6_Opening_des),
            InsightsParts(R.string.Wd_Item6_Screen_1_title, R.string.Wd_Item6_Screen_1_des),
            InsightsParts(R.string.Wd_Item6_Screen_2_title, R.string.Wd_Item6_Screen_2_des),
            InsightsParts(R.string.Wd_Item6_Screen_3_title, R.string.Wd_Item6_Screen_3_des),
            InsightsParts(R.string.Wd_Item6_Screen_4_title, R.string.Wd_Item6_Screen_4_des),
            InsightsParts(R.string.Wd_Item6_Screen_5_title, R.string.Wd_Item6_Screen_5_des),
            InsightsParts(R.string.Wd_Item6_Screen_6_title, R.string.Wd_Item6_Screen_6_des),
            InsightsParts(R.string.Wd_Item6_Screen_7_title, R.string.Wd_Item6_Screen_7_des)


        )
    )
    private val INSIGHT_WATER_7 = InsightsChildItem(
        R.string.water_drinking_title_7,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.water_drinking_title_7, R.string.Wd_Item7_Opening_des),
            InsightsParts(R.string.Wd_Item7_Screen_1_title, R.string.Wd_Item7_Screen_1_des),
            InsightsParts(R.string.Wd_Item7_Screen_2_title, R.string.Wd_Item7_Screen_2_des),
            InsightsParts(R.string.Wd_Item7_Screen_3_title, R.string.Wd_Item7_Screen_3_des),
            InsightsParts(R.string.Wd_Item7_Screen_4_title, R.string.Wd_Item7_Screen_4_des),
            InsightsParts(R.string.Wd_Item7_Screen_5_title, R.string.Wd_Item7_Screen_5_des),
            InsightsParts(R.string.Wd_Item7_Screen_6_title, R.string.Wd_Item7_Screen_6_des),
            InsightsParts(R.string.Wd_Item7_Screen_7_title, R.string.Wd_Item7_Screen_7_des),
            InsightsParts(R.string.Wd_Item7_Screen_8_title, R.string.Wd_Item7_Screen_8_des)
        )
    )
    private val INSIGHT_WATER_8 = InsightsChildItem(
        R.string.water_drinking_title_8,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.water_drinking_title_8, R.string.Wd_Item8_Opening_des),
            InsightsParts(R.string.Wd_Item8_Screen_1_title, R.string.Wd_Item8_Screen_1_des),
            InsightsParts(R.string.Wd_Item8_Screen_2_title, R.string.Wd_Item8_Screen_2_des),
            InsightsParts(R.string.Wd_Item8_Screen_3_title, R.string.Wd_Item8_Screen_3_des),
            InsightsParts(R.string.Wd_Item8_Screen_4_title, R.string.Wd_Item8_Screen_4_des),
            InsightsParts(R.string.Wd_Item8_Screen_5_title, R.string.Wd_Item8_Screen_5_des),
            InsightsParts(R.string.Wd_Item8_Screen_6_title, R.string.Wd_Item8_Screen_6_des),
            InsightsParts(R.string.Wd_Item8_Screen_7_title, R.string.Wd_Item8_Screen_7_des),
        )
    )
    private val INSIGHT_WATER_9 = InsightsChildItem(
        R.string.water_drinking_title_9,
        R.color.success_100,
        R.color.success_200,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.water_drinking_title_9, R.string.Wd_Item9_Opening_des),
            InsightsParts(R.string.Wd_Item9_Screen_1_title, R.string.Wd_Item9_Screen_1_des),
            InsightsParts(R.string.Wd_Item9_Screen_2_title, R.string.Wd_Item9_Screen_2_des),
            InsightsParts(R.string.Wd_Item9_Screen_3_title, R.string.Wd_Item9_Screen_3_des),
            InsightsParts(R.string.Wd_Item9_Screen_4_title, R.string.Wd_Item9_Screen_4_des),
            InsightsParts(R.string.Wd_Item9_Screen_5_title, R.string.Wd_Item9_Screen_5_des),
            InsightsParts(R.string.Wd_Item9_Screen_6_title, R.string.Wd_Item9_Screen_6_des),
        )
    )
    private val WATER_DRINKING = InsightsItem(
        R.string.water_drinking, R.color.success_800,listOf(
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
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.beauty_skincare_title_1, R.string.Bs_Item1_Opening_des),
            InsightsParts(R.string.Bs_Item1_Screen_1_title, R.string.Bs_Item1_Screen_1_des),
            InsightsParts(R.string.Bs_Item1_Screen_2_title, R.string.Bs_Item1_Screen_2_des),
            InsightsParts(R.string.Bs_Item1_Screen_3_title, R.string.Bs_Item1_Screen_3_des),
            InsightsParts(R.string.Bs_Item1_Screen_4_title, R.string.Bs_Item1_Screen_4_des),
            InsightsParts(R.string.Bs_Item1_Screen_5_title, R.string.Bs_Item1_Screen_5_des),
            InsightsParts(R.string.Bs_Item1_Screen_6_title, R.string.Bs_Item1_Screen_6_des),
        )

    )
    private val BEAUTY_SKINCARE_2 = InsightsChildItem(
        R.string.beauty_skincare_title_2,
        R.color.danger_200,
        R.color.danger_50,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.beauty_skincare_title_2, R.string.Bs_Item2_Opening_des),
            InsightsParts(R.string.Bs_Item2_Screen_1_title, R.string.Bs_Item2_Screen_1_des),
            InsightsParts(R.string.Bs_Item2_Screen_2_title, R.string.Bs_Item2_Screen_2_des),
            InsightsParts(R.string.Bs_Item2_Screen_3_title, R.string.Bs_Item2_Screen_3_des),
            InsightsParts(R.string.Bs_Item2_Screen_4_title, R.string.Bs_Item2_Screen_4_des),
            InsightsParts(R.string.Bs_Item2_Screen_5_title, R.string.Bs_Item2_Screen_5_des),
            InsightsParts(R.string.Bs_Item2_Screen_6_title, R.string.Bs_Item2_Screen_6_des),
            InsightsParts(R.string.Bs_Item2_Screen_7_title, R.string.Bs_Item2_Screen_7_des),
            InsightsParts(R.string.Bs_Item2_Screen_8_title, R.string.Bs_Item2_Screen_8_des),
        )
    )
    private val BEAUTY_SKINCARE_3 = InsightsChildItem(
        R.string.beauty_skincare_title_3,
        R.color.danger_200,
        R.color.danger_50,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.beauty_skincare_title_3, R.string.Bs_Item3_Opening_des),
            InsightsParts(R.string.Bs_Item3_Screen_1_title, R.string.Bs_Item3_Screen_1_des),
            InsightsParts(R.string.Bs_Item3_Screen_2_title, R.string.Bs_Item3_Screen_2_des),
            InsightsParts(R.string.Bs_Item3_Screen_3_title, R.string.Bs_Item3_Screen_3_des),
            InsightsParts(R.string.Bs_Item3_Screen_4_title, R.string.Bs_Item3_Screen_4_des),
            InsightsParts(R.string.Bs_Item3_Screen_5_title, R.string.Bs_Item3_Screen_5_des),
            InsightsParts(R.string.Bs_Item3_Screen_6_title, R.string.Bs_Item3_Screen_6_des),

            )
    )
    private val BEAUTY_SKINCARE_4 = InsightsChildItem(
        R.string.beauty_skincare_title_4,
        R.color.danger_200,
        R.color.danger_50,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.beauty_skincare_title_4, R.string.Bs_Item4_Opening_des),
            InsightsParts(R.string.Bs_Item4_Screen_1_title, R.string.Bs_Item4_Screen_1_des),
            InsightsParts(R.string.Bs_Item4_Screen_2_title, R.string.Bs_Item4_Screen_2_des),
            InsightsParts(R.string.Bs_Item4_Screen_3_title, R.string.Bs_Item4_Screen_3_des),
            InsightsParts(R.string.Bs_Item4_Screen_4_title, R.string.Bs_Item4_Screen_4_des),
            InsightsParts(R.string.Bs_Item4_Screen_5_title, R.string.Bs_Item4_Screen_5_des),
            InsightsParts(R.string.Bs_Item4_Screen_6_title, R.string.Bs_Item4_Screen_6_des),
        )
    )

    private val BEAUTY_SKINCARE = InsightsItem(
        R.string.beauty_skincare,R.color.danger_800 ,listOf(
            BEAUTY_SKINCARE_1, BEAUTY_SKINCARE_2, BEAUTY_SKINCARE_3, BEAUTY_SKINCARE_4
        )
    )


    private val SELF_CARE_1 = InsightsChildItem(
        R.string.self_care_title_1,
        R.color.warning_300,
        R.color.warning_100,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.self_care_title_1, R.string.Sc_Item1_Opening_des),
            InsightsParts(R.string.Sc_Item1_Screen_1_title, R.string.Sc_Item1_Screen_1_des),
            InsightsParts(R.string.Sc_Item1_Screen_2_title, R.string.Sc_Item1_Screen_2_des),
            InsightsParts(R.string.Sc_Item1_Screen_3_title, R.string.Sc_Item1_Screen_3_des),
            InsightsParts(R.string.Sc_Item1_Screen_4_title, R.string.Sc_Item1_Screen_4_des),
            InsightsParts(R.string.Sc_Item1_Screen_5_title, R.string.Sc_Item1_Screen_5_des),
            InsightsParts(R.string.Sc_Item1_Screen_6_title, R.string.Sc_Item1_Screen_6_des),
            InsightsParts(R.string.Sc_Item1_Screen_7_title, R.string.Sc_Item1_Screen_7_des),
        )
    )
    private val SELF_CARE_2 = InsightsChildItem(
        R.string.self_care_title_2,
        R.color.warning_300,
        R.color.warning_100,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.self_care_title_2, R.string.Sc_Item2_Opening_des),
            InsightsParts(R.string.Sc_Item2_Screen_1_title, R.string.Sc_Item2_Screen_1_des),
            InsightsParts(R.string.Sc_Item2_Screen_2_title, R.string.Sc_Item2_Screen_2_des),
            InsightsParts(R.string.Sc_Item2_Screen_3_title, R.string.Sc_Item2_Screen_3_des),
            InsightsParts(R.string.Sc_Item2_Screen_4_title, R.string.Sc_Item2_Screen_4_des),
            InsightsParts(R.string.Sc_Item2_Screen_5_title, R.string.Sc_Item2_Screen_5_des),
            InsightsParts(R.string.Sc_Item2_Screen_6_title, R.string.Sc_Item2_Screen_6_des),
        )
    )
    private val SELF_CARE_3 = InsightsChildItem(
        R.string.self_care_title_3,
        R.color.warning_300,
        R.color.warning_100,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.self_care_title_3, R.string.Sc_Item3_Opening_des),
            InsightsParts(R.string.Sc_Item3_Screen_1_title, R.string.Sc_Item3_Screen_1_des),
            InsightsParts(R.string.Sc_Item3_Screen_2_title, R.string.Sc_Item3_Screen_2_des),
            InsightsParts(R.string.Sc_Item3_Screen_3_title, R.string.Sc_Item3_Screen_3_des),
            InsightsParts(R.string.Sc_Item3_Screen_4_title, R.string.Sc_Item3_Screen_4_des),
            InsightsParts(R.string.Sc_Item3_Screen_5_title, R.string.Sc_Item3_Screen_5_des),
            InsightsParts(R.string.Sc_Item3_Screen_6_title, R.string.Sc_Item3_Screen_6_des),
            InsightsParts(R.string.Sc_Item3_Screen_7_title, R.string.Sc_Item3_Screen_7_des),
        )
    )
    private val SELF_CARE_4 = InsightsChildItem(
        R.string.self_care_title_4,
        R.color.warning_300,
        R.color.warning_100,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.self_care_title_4, R.string.Sc_Item4_Opening_des),
            InsightsParts(R.string.Sc_Item4_Screen_1_title, R.string.Sc_Item4_Screen_1_des),
            InsightsParts(R.string.Sc_Item4_Screen_2_title, R.string.Sc_Item4_Screen_2_des),
            InsightsParts(R.string.Sc_Item4_Screen_3_title, R.string.Sc_Item4_Screen_3_des),
            InsightsParts(R.string.Sc_Item4_Screen_4_title, R.string.Sc_Item4_Screen_4_des),
            InsightsParts(R.string.Sc_Item4_Screen_5_title, R.string.Sc_Item4_Screen_5_des),
        )
    )
    private val SELF_CARE_5 = InsightsChildItem(
        R.string.self_care_title_5,
        R.color.warning_300,
        R.color.warning_100,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.self_care_title_5, R.string.Sc_Item5_Opening_des),
            InsightsParts(R.string.Sc_Item5_Screen_1_title, R.string.Sc_Item5_Screen_1_des),
            InsightsParts(R.string.Sc_Item5_Screen_2_title, R.string.Sc_Item5_Screen_2_des),
            InsightsParts(R.string.Sc_Item5_Screen_3_title, R.string.Sc_Item5_Screen_3_des),
            InsightsParts(R.string.Sc_Item5_Screen_4_title, R.string.Sc_Item5_Screen_4_des),
            InsightsParts(R.string.Sc_Item5_Screen_5_title, R.string.Sc_Item5_Screen_5_des),
        )
    )


    private val SELF_CARE = InsightsItem(
        R.string.self_care,R.color.warning_900 ,listOf(
            SELF_CARE_1,
            SELF_CARE_2,
            SELF_CARE_3,
            SELF_CARE_4,
            SELF_CARE_5,

            )
    )


    private val HEALTH_LIFE_STYLE_1 = InsightsChildItem(
        R.string.health_life_style_title_1,
        R.color.secondary_200,
        R.color.secondary_100,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.health_life_style_title_1, R.string.Hl_Item1_Opening_des),
            InsightsParts(R.string.Hl_Item1_Screen_1_title, R.string.Hl_Item1_Screen_1_des),
            InsightsParts(R.string.Hl_Item1_Screen_2_title, R.string.Hl_Item1_Screen_2_des),
            InsightsParts(R.string.Hl_Item1_Screen_3_title, R.string.Hl_Item1_Screen_3_des),
            InsightsParts(R.string.Hl_Item1_Screen_4_title, R.string.Hl_Item1_Screen_4_des),
            InsightsParts(R.string.Hl_Item1_Screen_5_title, R.string.Hl_Item1_Screen_5_des),
            InsightsParts(R.string.Hl_Item1_Screen_6_title, R.string.Hl_Item1_Screen_6_des),
            InsightsParts(R.string.Hl_Item1_Screen_7_title, R.string.Hl_Item1_Screen_7_des),
            InsightsParts(R.string.Hl_Item1_Screen_8_title, R.string.Hl_Item1_Screen_8_des),
        )
    )
    private val HEALTH_LIFE_STYLE_2 = InsightsChildItem(
        R.string.health_life_style_title_2,
        R.color.secondary_200,
        R.color.secondary_100,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.health_life_style_title_1, R.string.Hl_Item2_Opening_des),
            InsightsParts(R.string.Hl_Item2_Screen_1_title, R.string.Hl_Item2_Screen_1_des),
            InsightsParts(R.string.Hl_Item2_Screen_2_title, R.string.Hl_Item2_Screen_2_des),
            InsightsParts(R.string.Hl_Item2_Screen_3_title, R.string.Hl_Item2_Screen_3_des),
            InsightsParts(R.string.Hl_Item2_Screen_4_title, R.string.Hl_Item2_Screen_4_des),
            InsightsParts(R.string.Hl_Item2_Screen_5_title, R.string.Hl_Item2_Screen_5_des),

            )
    )
    private val HEALTH_LIFE_STYLE_3 = InsightsChildItem(
        R.string.health_life_style_title_3,
        R.color.secondary_200,
        R.color.secondary_100,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.health_life_style_title_1, R.string.Hl_Item3_Opening_des),
            InsightsParts(R.string.Hl_Item3_Screen_1_title, R.string.Hl_Item3_Screen_1_des),
            InsightsParts(R.string.Hl_Item3_Screen_2_title, R.string.Hl_Item3_Screen_2_des),
            InsightsParts(R.string.Hl_Item3_Screen_3_title, R.string.Hl_Item3_Screen_3_des),
            InsightsParts(R.string.Hl_Item3_Screen_4_title, R.string.Hl_Item3_Screen_4_des),
            InsightsParts(R.string.Hl_Item3_Screen_5_title, R.string.Hl_Item3_Screen_5_des),
            InsightsParts(R.string.Hl_Item3_Screen_6_title, R.string.Hl_Item3_Screen_6_des),
            InsightsParts(R.string.Hl_Item3_Screen_7_title, R.string.Hl_Item3_Screen_7_des),
            InsightsParts(R.string.Hl_Item3_Screen_8_title, R.string.Hl_Item3_Screen_8_des),
        )
    )
    private val HEALTH_LIFE_STYLE_4 = InsightsChildItem(
        R.string.health_life_style_title_4,
        R.color.secondary_200,
        R.color.secondary_100,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.health_life_style_title_4, R.string.Hl_Item4_Opening_des),
            InsightsParts(R.string.Hl_Item4_Screen_1_title, R.string.Hl_Item4_Screen_1_des),
            InsightsParts(R.string.Hl_Item4_Screen_2_title, R.string.Hl_Item4_Screen_2_des),
            InsightsParts(R.string.Hl_Item4_Screen_3_title, R.string.Hl_Item4_Screen_3_des),
            InsightsParts(R.string.Hl_Item4_Screen_4_title, R.string.Hl_Item4_Screen_4_des),
            InsightsParts(R.string.Hl_Item4_Screen_5_title, R.string.Hl_Item4_Screen_5_des),
        )
    )
    private val HEALTH_LIFE_STYLE_5 = InsightsChildItem(
        R.string.health_life_style_title_5,
        R.color.secondary_200,
        R.color.secondary_100,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.health_life_style_title_5, R.string.Hl_Item5_Opening_des),
            InsightsParts(R.string.Hl_Item5_Screen_1_title, R.string.Hl_Item5_Screen_1_des),
            InsightsParts(R.string.Hl_Item5_Screen_2_title, R.string.Hl_Item5_Screen_2_des),
            InsightsParts(R.string.Hl_Item5_Screen_3_title, R.string.Hl_Item5_Screen_3_des),
            InsightsParts(R.string.Hl_Item5_Screen_4_title, R.string.Hl_Item5_Screen_4_des),
            InsightsParts(R.string.Hl_Item5_Screen_5_title, R.string.Hl_Item5_Screen_5_des),
            InsightsParts(R.string.Hl_Item5_Screen_6_title, R.string.Hl_Item5_Screen_6_des),
            InsightsParts(R.string.Hl_Item5_Screen_7_title, R.string.Hl_Item5_Screen_7_des),
            InsightsParts(R.string.Hl_Item5_Screen_8_title, R.string.Hl_Item5_Screen_8_des),
        )
    )
    private val HEALTH_LIFE_STYLE_6 = InsightsChildItem(
        R.string.health_life_style_title_6,
        R.color.secondary_200,
        R.color.secondary_100,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.health_life_style_title_6, R.string.Hl_Item6_Opening_des),
            InsightsParts(R.string.Hl_Item6_Screen_1_title, R.string.Hl_Item6_Screen_1_des),
            InsightsParts(R.string.Hl_Item6_Screen_2_title, R.string.Hl_Item6_Screen_2_des),
            InsightsParts(R.string.Hl_Item6_Screen_3_title, R.string.Hl_Item6_Screen_3_des),
            InsightsParts(R.string.Hl_Item6_Screen_4_title, R.string.Hl_Item6_Screen_4_des),
            InsightsParts(R.string.Hl_Item6_Screen_5_title, R.string.Hl_Item6_Screen_5_des),
            InsightsParts(R.string.Hl_Item6_Screen_6_title, R.string.Hl_Item6_Screen_6_des),
        )
    )

    private val HEALTH_LIFE_STYLE = InsightsItem(
        R.string.health_life_style, R.color.secondary_800,listOf(
            HEALTH_LIFE_STYLE_1,
            HEALTH_LIFE_STYLE_2,
            HEALTH_LIFE_STYLE_3,
            HEALTH_LIFE_STYLE_4,
            HEALTH_LIFE_STYLE_5,
            HEALTH_LIFE_STYLE_6
        )
    )

    private val CARDIO_VASCULAR_HEALTH_1 = InsightsChildItem(
        R.string.cardio_vascular_health_title_1,
        R.color.info_300,
        R.color.info_200,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.cardio_vascular_health_title_1, R.string.Cv_Item1_Opening_des),
            InsightsParts(R.string.Cv_Item1_Screen_1_title, R.string.Cv_Item1_Screen_1_des),
            InsightsParts(R.string.Cv_Item1_Screen_2_title, R.string.Cv_Item1_Screen_2_des),
            InsightsParts(R.string.Cv_Item1_Screen_3_title, R.string.Cv_Item1_Screen_3_des),
            InsightsParts(R.string.Cv_Item1_Screen_4_title, R.string.Cv_Item1_Screen_4_des),
            InsightsParts(R.string.Cv_Item1_Screen_5_title, R.string.Cv_Item1_Screen_5_des),
            InsightsParts(R.string.Cv_Item1_Screen_6_title, R.string.Cv_Item1_Screen_6_des),
        )
    )
    private val CARDIO_VASCULAR_HEALTH_2 = InsightsChildItem(
        R.string.cardio_vascular_health_title_2,
        R.color.info_300,
        R.color.info_200,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.cardio_vascular_health_title_2, R.string.Cv_Item2_Opening_des),
            InsightsParts(R.string.Cv_Item2_Screen_1_title, R.string.Cv_Item2_Screen_1_des),
            InsightsParts(R.string.Cv_Item2_Screen_2_title, R.string.Cv_Item2_Screen_2_des),
            InsightsParts(R.string.Cv_Item2_Screen_3_title, R.string.Cv_Item2_Screen_3_des),
            InsightsParts(R.string.Cv_Item2_Screen_4_title, R.string.Cv_Item2_Screen_4_des),
            InsightsParts(R.string.Cv_Item2_Screen_5_title, R.string.Cv_Item2_Screen_5_des),
            InsightsParts(R.string.Cv_Item2_Screen_6_title, R.string.Cv_Item2_Screen_6_des),
            InsightsParts(R.string.Cv_Item2_Screen_7_title, R.string.Cv_Item2_Screen_7_des),
            InsightsParts(R.string.Cv_Item2_Screen_8_title, R.string.Cv_Item2_Screen_8_des),
        )
    )
    private val CARDIO_VASCULAR_HEALTH_3 = InsightsChildItem(
        R.string.cardio_vascular_health_title_3,
        R.color.info_300,
        R.color.info_200,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.cardio_vascular_health_title_3, R.string.Cv_Item3_Opening_des),
            InsightsParts(R.string.Cv_Item3_Screen_1_title, R.string.Cv_Item3_Screen_1_des),
            InsightsParts(R.string.Cv_Item3_Screen_2_title, R.string.Cv_Item3_Screen_2_des),
            InsightsParts(R.string.Cv_Item3_Screen_3_title, R.string.Cv_Item3_Screen_3_des),
            InsightsParts(R.string.Cv_Item3_Screen_4_title, R.string.Cv_Item3_Screen_4_des),
            InsightsParts(R.string.Cv_Item3_Screen_5_title, R.string.Cv_Item3_Screen_5_des),
            InsightsParts(R.string.Cv_Item3_Screen_6_title, R.string.Cv_Item3_Screen_6_des),
            InsightsParts(R.string.Cv_Item3_Screen_7_title, R.string.Cv_Item3_Screen_7_des),
        )
    )
    private val CARDIO_VASCULAR_HEALTH_4 = InsightsChildItem(
        R.string.cardio_vascular_health_title_4,
        R.color.info_300,
        R.color.info_200,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.cardio_vascular_health_title_4, R.string.Cv_Item4_Opening_des),
            InsightsParts(R.string.Cv_Item4_Screen_1_title, R.string.Cv_Item4_Screen_1_des),
            InsightsParts(R.string.Cv_Item4_Screen_2_title, R.string.Cv_Item4_Screen_2_des),
            InsightsParts(R.string.Cv_Item4_Screen_3_title, R.string.Cv_Item4_Screen_3_des),
            InsightsParts(R.string.Cv_Item4_Screen_4_title, R.string.Cv_Item4_Screen_4_des),
        )
    )
    private val CARDIO_VASCULAR_HEALTH_5 = InsightsChildItem(
        R.string.cardio_vascular_health_title_5,
        R.color.info_300,
        R.color.info_200,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.cardio_vascular_health_title_5, R.string.Cv_Item5_Opening_des),
            InsightsParts(R.string.Cv_Item5_Screen_1_title, R.string.Cv_Item5_Screen_1_des),
            InsightsParts(R.string.Cv_Item5_Screen_2_title, R.string.Cv_Item5_Screen_2_des),
            InsightsParts(R.string.Cv_Item5_Screen_3_title, R.string.Cv_Item5_Screen_3_des),
            InsightsParts(R.string.Cv_Item5_Screen_4_title, R.string.Cv_Item5_Screen_4_des),
            InsightsParts(R.string.Cv_Item5_Screen_5_title, R.string.Cv_Item5_Screen_5_des),
            InsightsParts(R.string.Cv_Item5_Screen_6_title, R.string.Cv_Item5_Screen_6_des),
        )
    )
    private val CARDIO_VASCULAR_HEALTH_6 = InsightsChildItem(
        R.string.cardio_vascular_health_title_6,
        R.color.info_300,
        R.color.info_200,
        R.drawable.ic_gender_wt,
        listOf(
            InsightsParts(R.string.cardio_vascular_health_title_6, R.string.Cv_Item6_Opening_des),
            InsightsParts(R.string.Cv_Item6_Screen_1_title, R.string.Cv_Item6_Screen_1_des),
            InsightsParts(R.string.Cv_Item6_Screen_2_title, R.string.Cv_Item6_Screen_2_des),
            InsightsParts(R.string.Cv_Item6_Screen_3_title, R.string.Cv_Item6_Screen_3_des),
            InsightsParts(R.string.Cv_Item6_Screen_4_title, R.string.Cv_Item6_Screen_4_des),
            InsightsParts(R.string.Cv_Item6_Screen_5_title, R.string.Cv_Item6_Screen_5_des),
        )
    )
    private val CARDIO_VASCULAR_HEALTH = InsightsItem(
        R.string.cardio_vascular_health, R.color.info_800,listOf(
            CARDIO_VASCULAR_HEALTH_1,
            CARDIO_VASCULAR_HEALTH_2,
            CARDIO_VASCULAR_HEALTH_3,
            CARDIO_VASCULAR_HEALTH_4,
            CARDIO_VASCULAR_HEALTH_5,
            CARDIO_VASCULAR_HEALTH_6
        )
    )

    val ALL_DATA_INSIGHT = listOf(
        WATER_DRINKING, BEAUTY_SKINCARE, SELF_CARE, HEALTH_LIFE_STYLE,
        CARDIO_VASCULAR_HEALTH
    )
}