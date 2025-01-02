package com.wavez.trackerwater.splash

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.lifecycleScope
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.MainActivity
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.ActivityMainBinding
import com.wavez.trackerwater.databinding.ActivitySplashBinding
import com.wavez.trackerwater.onboarding.OnboardingActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun createBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)

    }

    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
        lifecycleScope.launch {
            delay(3000)
            startActivity(OnboardingActivity.newIntent(this@SplashActivity))

        }
    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {
        super.initListener()

    }
}
