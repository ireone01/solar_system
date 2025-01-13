package com.wavez.trackerwater.feature.home

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.ActivityMainBinding
import com.wavez.trackerwater.feature.home.adapter.MainViewPagerAdapter
import com.wavez.trackerwater.feature.reminder.receiver.WaterReminderService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    companion object {
        const val PAGE_TODAY = 0

        const val PAGE_HISTORY = 1

        const val PAGE_INSIGHT= 2

        const val PAGE_PROFILE = 3


        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    private lateinit var adapter: MainViewPagerAdapter

    override fun createBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
        adapter = MainViewPagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false

        val serviceIntent = Intent(this, WaterReminderService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        }

        binding.bottomView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_today -> {
                    binding.viewPager.currentItem = PAGE_TODAY
                }
                R.id.nav_history -> {
                    binding.viewPager.currentItem = PAGE_HISTORY
                }
                R.id.nav_insights -> {
                    binding.viewPager.currentItem = PAGE_INSIGHT
                }
                R.id.nav_profile -> {
                    binding.viewPager.currentItem = PAGE_PROFILE
                }
            }
            true
        }
    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {
        super.initListener()
    }
}
