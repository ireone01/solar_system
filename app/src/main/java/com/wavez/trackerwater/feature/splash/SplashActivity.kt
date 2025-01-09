package com.wavez.trackerwater.feature.splash

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.R
import com.wavez.trackerwater.data.sharedPref.SharedPref
import com.wavez.trackerwater.databinding.ActivitySplashBinding
import com.wavez.trackerwater.feature.drink.DrinkActivity
import com.wavez.trackerwater.feature.home.MainActivity
import com.wavez.trackerwater.feature.language.activities.OnboardingLanguageActivity
import com.wavez.trackerwater.feature.onboarding.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun createBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)

    }

    companion object {
        private const val MAX_LOADING = 5000L
        private const val MIN_LOADING = 3000L
    }

    @Inject
    lateinit var sharedPref: SharedPref

    private var handler: Handler? = null


    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
        Log.d("hehehe", "initConfig: "+sharedPref.isNeedShowLanguage + sharedPref.isNeedShowLanguage)
        startLaunchSequence()

    }

    private fun startLaunchSequence() {
        handler = Handler(Looper.getMainLooper())
        handler?.postDelayed({
            nextScreen()
        }, MIN_LOADING)
    }

    private fun nextScreen() {
        removeHandler()
        if (sharedPref.isNeedShowLanguage) {
            startActivity(OnboardingLanguageActivity.newIntent(this),)
        } else {
            if (sharedPref.isNeedShowLanguage) {
                startActivity(OnboardingActivity.newIntent(this))
            } else {
                startActivity(MainActivity.newIntent(this))
            }
        }
    }

    private fun removeHandler() {
        handler?.removeCallbacksAndMessages(null)
    }

}
