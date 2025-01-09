package com.wavez.trackerwater.feature.language.activities

import android.content.Context
import android.content.Intent
import com.wavez.trackerwater.feature.language.model.Language
import com.wavez.trackerwater.feature.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingLanguageActivity : BaseLanguageActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SettingLanguageActivity::class.java)
        }
    }

    override fun initListener() {
        super.initListener()
        adapter.onLanguageSelected(viewModel.getConfig())

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onLanguagesChanged(languages: List<Language>) {
        super.onLanguagesChanged(languages)
        viewModel.setLanguageDefault(languages)
    }

    override fun onSaveLanguage() {
        startActivity(MainActivity.newIntent(this).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
}