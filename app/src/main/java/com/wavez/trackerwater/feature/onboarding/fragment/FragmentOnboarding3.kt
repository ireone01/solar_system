package com.wavez.trackerwater.feature.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentOnboading3Binding

class FragmentOnboarding3 :BaseFragment<FragmentOnboading3Binding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboading3Binding
        get() = FragmentOnboading3Binding::inflate

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        binding.ivMale.isSelected = true
        binding.tvMale.isSelected =true
        binding.ivFemale.isSelected = false
        binding.btnOther.isSelected = false
        binding.LLLayoutFemale.visibility = View.GONE
    }


    override fun initListener() {
        super.initListener()
        binding.ivMale.setOnClickListener {
            binding.ivMale.isSelected = true
            binding.tvMale.isSelected =true

            binding.tvFemale.isSelected =false
            binding.ivFemale.isSelected = false

            binding.btnOther.isSelected = false
            binding.LLLayoutFemale.visibility = View.GONE
        }

        binding.ivFemale.setOnClickListener {
            binding.ivFemale.isSelected = true
            binding.tvFemale.isSelected = true


            binding.ivMale.isSelected = false
            binding.tvMale.isSelected =false


            binding.btnOther.isSelected = false

            binding.LLLayoutFemale.visibility = View.VISIBLE
            binding.tvPregnant.setOnClickListener {
                binding.tvPregnant.isSelected = true
                binding.tvBreastfeeding.isSelected = false
            }
            binding.tvBreastfeeding.setOnClickListener {
                binding.tvBreastfeeding.isSelected = true
                binding.tvPregnant.isSelected = false
            }
        }

        binding.btnOther.setOnClickListener {
            binding.ivMale.isSelected = false
            binding.tvMale.isSelected =false

            binding.ivFemale.isSelected = false
            binding.tvFemale.isSelected =false

            binding.btnOther.isSelected = true

            binding.LLLayoutFemale.visibility = View.GONE
        }

    }


}