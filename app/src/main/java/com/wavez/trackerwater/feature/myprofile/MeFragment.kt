package com.wavez.trackerwater.feature.myprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentMeBinding
import com.wavez.trackerwater.feature.myprofile.adapter.FragmentDialogSoundVibration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeFragment: BaseFragment<FragmentMeBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMeBinding
        get() = FragmentMeBinding::inflate

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {
        super.initListener()

        binding.llayout2.setOnClickListener {
            val dialog = FragmentDialogDailyGoal()
            dialog.show(parentFragmentManager,"DailyGoalOn")
        }
        binding.llayout3.setOnClickListener {
            val dialog = FragmentDialogSoundVibration()
            dialog.show(parentFragmentManager,"SoundVibration_On")
        }

        binding.llayout4.setOnClickListener {
            val dialog = FragmentDialogUnits()
            dialog.show(parentFragmentManager,"UnitOn")
        }

        binding.llayout21.setOnClickListener {
            val dialog = FragmentDIalogGenderWeight()
            dialog.show(parentFragmentManager,"GenderWeightOn")
        }
        binding.llayout23.setOnClickListener {
            val dialog = FragmentDialogFirstDofW()
            dialog.show(parentFragmentManager,"FirstDoW_On")
        }

        binding.llayout24.setOnClickListener {
            val dialog = FragmentDialogAdayStartAt()
            dialog.show(parentFragmentManager,"AdayStartAt_On")
        }

        binding.llayout25.setOnClickListener {
            val dialog = FragmentDialogTimeFormat()
            dialog.show(parentFragmentManager,"TimeFormat_On")
        }


    }
}