package com.wavez.trackerwater.feature.language.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.wavez.trackerwater.extension.gone
import com.wavez.trackerwater.extension.visible
import com.wavez.trackerwater.feature.onboarding.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingLanguageActivity : BaseLanguageActivity() {

    companion object {

        fun newIntent(context: Context) =
            Intent(context, OnboardingLanguageActivity::class.java)
    }

    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
        binding.btnBack.gone()
        binding.btnSave.visible()
        binding.btnSave.isEnabled = false
        binding.btnNext.gone()
    }

    override fun onSaveLanguage() {
        super.onSaveLanguage()
        viewModel.saveNeedLanguage()
        startActivity(OnboardingActivity.newIntent(this@OnboardingLanguageActivity))
        finish()
    }

    override fun updateStateButton(isEnabled: Boolean) {
        binding.tvNext.isEnabled = isEnabled
        binding.btnSave.isEnabled = isEnabled
        binding.btnSave.isActivated = isEnabled

    }
}