package com.wavez.trackerwater.feature.language.activities

import android.os.Bundle
import androidx.activity.viewModels
import com.lingvo.base_common.model.DataResult
import com.lingvo.base_common.ui.BaseActivity
import com.m17_stepcounter.feature.setting.language.LanguageAdapter
import com.wavez.trackerwater.feature.language.LanguageViewModel
import com.wavez.trackerwater.feature.language.model.Language
import com.wavez.trackerwater.databinding.ActivityBaseLanguageBinding
import com.wavez.trackerwater.extension.gone
import com.wavez.trackerwater.extension.visible

abstract class BaseLanguageActivity : BaseActivity<ActivityBaseLanguageBinding>() {

    override fun createBinding(): ActivityBaseLanguageBinding {
        return ActivityBaseLanguageBinding.inflate(layoutInflater)
    }

    val viewModel by viewModels<LanguageViewModel>()

    protected lateinit var adapter: LanguageAdapter

    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
        initAdapter()
        viewModel.getUiLanguages()
    }

    override fun initObserver() {
        super.initObserver()

        viewModel.selectedUiLanguage.observe(this) {
            adapter.onLanguageSelected(it.code)
        }

        viewModel.uiLanguageState.observe(this) {
            binding.progressBar.gone()
            when (it) {
                is DataResult.Loading -> {
                    binding.progressBar.visible()
                }

                is DataResult.Success -> {
                    binding.progressBar.gone()
                    onLanguagesChanged(it.data)
                }

                else -> {}
            }
        }
    }

    override fun initListener() {
        super.initListener()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            viewModel.saveConfig()
            onSaveLanguage()
        }
    }

    private fun initAdapter() {

        adapter = LanguageAdapter().apply {
            onSelected = {
                viewModel.onLanguageChanged(it)
                updateStateButton(true)
            }
        }
        binding.rvLanguage.adapter = adapter
    }

    open fun updateStateButton(isEnabled: Boolean) {}

    open fun onSaveLanguage() {}

    open fun onLanguagesChanged(languages: List<Language>) {
        adapter.setData(languages)
    }

}