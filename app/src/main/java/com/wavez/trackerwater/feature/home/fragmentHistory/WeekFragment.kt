package com.wavez.trackerwater.feature.home.fragmentHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentWeekBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeekFragment : BaseFragment<FragmentWeekBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentWeekBinding
        get() = FragmentWeekBinding::inflate

}