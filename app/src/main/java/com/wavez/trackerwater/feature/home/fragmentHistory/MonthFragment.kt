package com.wavez.trackerwater.feature.home.fragmentHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentMonthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonthFragment : BaseFragment<FragmentMonthBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMonthBinding
        get() = FragmentMonthBinding::inflate

}