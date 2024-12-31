package com.wavez.trackerwater.ui.home.history

import android.view.LayoutInflater
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentMonthBinding

class MonthFragment : BaseFragment<FragmentMonthBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMonthBinding
        get() = FragmentMonthBinding::inflate

}