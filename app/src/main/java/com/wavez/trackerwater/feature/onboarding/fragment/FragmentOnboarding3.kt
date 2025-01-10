package com.wavez.trackerwater.feature.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.data.sharedPref.SharedPref
import com.wavez.trackerwater.databinding.FragmentOnboading3Binding
import com.wavez.trackerwater.extension.gone
import com.wavez.trackerwater.extension.visible
import com.wavez.trackerwater.util.ConstantUser
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentOnboarding3 : BaseFragment<FragmentOnboading3Binding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboading3Binding
        get() = FragmentOnboading3Binding::inflate

    @Inject
    lateinit var sharedPref: SharedPref

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        syncUiGender()
    }

    private fun syncUiGender() {
        when (sharedPref.genderUser) {
            ConstantUser.GENDER_MALE -> {
                sharedPref.genderUser = ConstantUser.GENDER_MALE
                binding.ivMale.isSelected = true
                binding.tvMale.isSelected = true

                binding.tvFemale.isSelected = false
                binding.ivFemale.isSelected = false

                binding.btnOther.isSelected = false
                binding.LLLayoutFemale.gone()
            }

            ConstantUser.GENDER_FEMALE -> {
                sharedPref.genderUser = ConstantUser.GENDER_FEMALE

                binding.ivFemale.isSelected = true
                binding.tvFemale.isSelected = true

                binding.ivMale.isSelected = false
                binding.tvMale.isSelected = false

                binding.btnOther.isSelected = false
                binding.LLLayoutFemale.gone()
            }

            else -> {
                sharedPref.genderUser = ConstantUser.GENDER_OTHER
                binding.ivMale.isSelected = false
                binding.tvMale.isSelected = false

                binding.ivFemale.isSelected = false
                binding.tvFemale.isSelected = false

                binding.btnOther.isSelected = true
                binding.LLLayoutFemale.visible()

            }
        }
    }


    override fun initListener() {
        super.initListener()
        binding.ivMale.setOnClickListener {
            sharedPref.genderUser = ConstantUser.GENDER_MALE
            binding.ivMale.isSelected = true
            binding.tvMale.isSelected = true

            binding.tvFemale.isSelected = false
            binding.ivFemale.isSelected = false

            binding.btnOther.isSelected = false
            binding.LLLayoutFemale.gone()
        }

        binding.ivFemale.setOnClickListener {
            sharedPref.genderUser = ConstantUser.GENDER_FEMALE

            binding.ivFemale.isSelected = true
            binding.tvFemale.isSelected = true

            binding.ivMale.isSelected = false
            binding.tvMale.isSelected = false

            binding.btnOther.isSelected = false
            binding.LLLayoutFemale.visible()

        }

        binding.btnOther.setOnClickListener {
            sharedPref.genderUser = ConstantUser.GENDER_OTHER
            binding.ivMale.isSelected = false
            binding.tvMale.isSelected = false

            binding.ivFemale.isSelected = false
            binding.tvFemale.isSelected = false

            binding.btnOther.isSelected = true
            binding.LLLayoutFemale.gone()


        }



        binding.tvPregnant.visible()
        binding.tvBreastfeeding.visible()
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