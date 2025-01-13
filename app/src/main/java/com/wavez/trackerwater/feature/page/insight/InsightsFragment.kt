package com.wavez.trackerwater.feature.page.insight

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentInsightsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InsightsFragment : BaseFragment<FragmentInsightsBinding>() {


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentInsightsBinding
        get() = FragmentInsightsBinding::inflate

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {
        super.initListener()
    }


}