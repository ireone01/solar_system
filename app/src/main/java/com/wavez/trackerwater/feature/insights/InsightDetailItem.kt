package com.wavez.trackerwater.feature.insights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentInsightDetailBinding
import com.wavez.trackerwater.feature.insights.adapter.InsightDetailPagerAdapter
import com.wavez.trackerwater.feature.insights.model.InsightsParts

class InsightDetailItem : BaseFragment<FragmentInsightDetailBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInsightDetailBinding
        get() = FragmentInsightDetailBinding::inflate

    companion object {
        fun newInstance(parts: List<InsightsParts>): InsightDetailItem {
            val fragment = InsightDetailItem()
            val bundle = Bundle()
            bundle.putSerializable("ARG_PARTS", ArrayList(parts))
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        val parts = arguments?.getSerializable("ARG_PARTS") as? ArrayList<InsightsParts> ?: arrayListOf()
        binding.vPagerInsight.adapter = InsightDetailPagerAdapter(parts.map { it.contentDescrip })
        binding.vPagerInsight.setPageTransformer(FadePageTransformer())
    }
}