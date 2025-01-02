package com.wavez.trackerwater.ui.home.fragmentHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentDayBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DayFragment : BaseFragment<FragmentDayBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDayBinding
        get() = FragmentDayBinding::inflate

}