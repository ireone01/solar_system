package com.wavez.trackerwater.feature.fragment.fragmentHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentWeekBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeekFragment : BaseFragment<FragmentWeekBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentWeekBinding
        get() = FragmentWeekBinding::inflate

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
    }

    override fun initListener() {
        super.initListener()
    }

    override fun initObserver() {
        super.initObserver()
    }
}