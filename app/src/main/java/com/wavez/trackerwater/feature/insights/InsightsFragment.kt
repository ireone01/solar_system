package com.wavez.trackerwater.feature.insights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentInsightsBinding
import com.wavez.trackerwater.feature.insights.adapter.InsightAdapter
import com.wavez.trackerwater.feature.insights.model.InsightsChildItem
import com.wavez.trackerwater.feature.insights.provider.InsightProvider


class InsightsFragment : BaseFragment<FragmentInsightsBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInsightsBinding
        get() = FragmentInsightsBinding::inflate

    private lateinit var adapter: InsightAdapter

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        adapter = InsightAdapter { childItem, textColor ->
            openChildDetail(childItem, textColor)
        }
        binding.rvInsight.adapter = adapter
        adapter.setData(InsightProvider.ALL_DATA_INSIGHT)

    }

    private fun openChildDetail(childItem: InsightsChildItem, textColor: Int) {
        startActivity(
            InsightDetailActivity.newIntent(
                childItem.parts, childItem.colorPrimary, textColor, requireContext()
            )
        )
    }

}