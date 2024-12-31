package com.wavez.trackerwater.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.R
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