package com.wavez.trackerwater.feature.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentOnboading3Binding
import com.wavez.trackerwater.extension.gone
import com.wavez.trackerwater.extension.visible

class FragmentOnboarding3 :BaseFragment<FragmentOnboading3Binding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboading3Binding
        get() = FragmentOnboading3Binding::inflate

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        binding.ivMale.isSelected = true
        binding.tvMale.isSelected =true
        binding.ivFemale.isSelected = false
        binding.btnOther.isSelected = false
        binding.LLLayoutFemale.gone()
    }


    override fun initListener() {
        super.initListener()
        binding.ivMale.setOnClickListener {
            binding.ivMale.isSelected = true
            binding.tvMale.isSelected =true

            binding.tvFemale.isSelected =false
            binding.ivFemale.isSelected = false

            binding.btnOther.isSelected = false
            binding.LLLayoutFemale.gone()
        }

        binding.ivFemale.setOnClickListener {
            binding.ivFemale.isSelected = true
            binding.tvFemale.isSelected = true

            binding.ivMale.isSelected = false
            binding.tvMale.isSelected =false

            binding.btnOther.isSelected = false
            binding.LLLayoutFemale.gone()

        }

        binding.btnOther.setOnClickListener {
            binding.ivMale.isSelected = false
            binding.tvMale.isSelected =false

            binding.ivFemale.isSelected = false
            binding.tvFemale.isSelected =false

            binding.btnOther.isSelected = true
            binding.LLLayoutFemale.visible()


        }

        binding.tvPregnant.setOnClickListener {
            binding.tvPregnant.isSelected = true
            binding.tvBreastfeeding.isSelected = false
        }

        binding.tvBreastfeeding.setOnClickListener {
            binding.tvBreastfeeding.isSelected = true
            binding.tvPregnant.isSelected = false
        }

    }


}