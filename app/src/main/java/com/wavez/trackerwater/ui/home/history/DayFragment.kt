package com.wavez.trackerwater.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentDayBinding

class DayFragment : BaseFragment<FragmentDayBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDayBinding
        get() = FragmentDayBinding::inflate

}