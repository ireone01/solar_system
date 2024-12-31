package com.wavez.trackerwater.ui.drink

import android.os.Bundle
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.databinding.ActivityDrinkBinding

class DrinkActivity : BaseActivity<ActivityDrinkBinding>() {
    override fun createBinding(): ActivityDrinkBinding {
        return ActivityDrinkBinding.inflate(layoutInflater)
    }

    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
    }

    override fun initListener() {
        super.initListener()
        binding.ivClose.setOnClickListener { finish() }
    }

    override fun initObserver() {
        super.initObserver()
    }

}