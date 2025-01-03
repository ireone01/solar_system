package com.wavez.trackerwater.feature.onboarding.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentOnboading2Binding

class FragmentOnboarding2 : BaseFragment<FragmentOnboading2Binding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboading2Binding
        get() = FragmentOnboading2Binding::inflate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("FragmentOnboarding2","oncreate")
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
