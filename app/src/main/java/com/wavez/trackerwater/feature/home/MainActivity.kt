package com.wavez.trackerwater.feature.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.ActivityMainBinding
import com.wavez.trackerwater.feature.home.adapter.MainViewPagerAdapter
import com.wavez.trackerwater.feature.reminder.WaterReminderService
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override fun createBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var adapter: MainViewPagerAdapter


    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
        adapter = MainViewPagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        val serviceIntent = Intent(this, WaterReminderService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        }
    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {
        super.initListener()
        binding.btnToday.setOnClickListener { setPage(0) }
        binding.btnHistory.setOnClickListener { setPage(1) }
        binding.btnInsights.setOnClickListener { setPage(2) }
        binding.btnMe.setOnClickListener { setPage(3) }
    }

    @SuppressLint("ResourceAsColor")
    private fun setPage(page: Int) {
        binding.viewPager.setCurrentItem(page, false)
        when (page) {
            0 -> {
                binding.ivToday.setImageResource(R.drawable.ic_main_today_true)
                binding.tvToday.setTextColor(ContextCompat.getColor(this, R.color.white_100))
                binding.ivHistory.setImageResource(R.drawable.ic_main_history_false)
                binding.tvHistory.setTextColor(ContextCompat.getColor(this, R.color.primary_100))
                binding.ivInsights.setImageResource(R.drawable.ic_main_insights_false)
                binding.tvInsights.setTextColor(ContextCompat.getColor(this, R.color.primary_100))
                binding.ivMe.setImageResource(R.drawable.ic_main_profile_false)
                binding.viewBottom.setBackgroundResource(R.color.primary_500)
                binding.tvMe.setTextColor(ContextCompat.getColor(this, R.color.primary_100))
            }

            1 -> {
                binding.ivToday.setImageResource(R.drawable.ic_main_today_false)
                binding.viewBottom.setBackgroundResource(R.color.primary_500)
                binding.tvToday.setTextColor(ContextCompat.getColor(this, R.color.primary_100))
                binding.ivHistory.setImageResource(R.drawable.ic_main_history_true)
                binding.tvHistory.setTextColor(ContextCompat.getColor(this, R.color.white_100))
                binding.ivInsights.setImageResource(R.drawable.ic_main_insights_false)
                binding.tvInsights.setTextColor(ContextCompat.getColor(this, R.color.primary_100))
                binding.ivMe.setImageResource(R.drawable.ic_main_profile_false)
                binding.tvMe.setTextColor(ContextCompat.getColor(this, R.color.primary_100))
            }

            2 -> {
                binding.ivToday.setImageResource(R.drawable.ic_main_today_false)
                binding.viewBottom.setBackgroundResource(R.color.primary_600)
                binding.tvToday.setTextColor(ContextCompat.getColor(this, R.color.primary_100))
                binding.ivHistory.setImageResource(R.drawable.ic_main_history_false)
                binding.tvHistory.setTextColor(ContextCompat.getColor(this, R.color.primary_100))
                binding.ivInsights.setImageResource(R.drawable.ic_main_insights_true)
                binding.tvInsights.setTextColor(ContextCompat.getColor(this, R.color.white_100))
                binding.ivMe.setImageResource(R.drawable.ic_main_profile_false)
                binding.tvMe.setTextColor(ContextCompat.getColor(this, R.color.primary_100))
            }

            3 -> {
                binding.ivToday.setImageResource(R.drawable.ic_main_today_false)
                binding.tvToday.setTextColor(ContextCompat.getColor(this, R.color.primary_100))
                binding.ivHistory.setImageResource(R.drawable.ic_main_history_false)
                binding.tvHistory.setTextColor(ContextCompat.getColor(this, R.color.primary_100))
                binding.ivInsights.setImageResource(R.drawable.ic_main_insights_false)
                binding.viewBottom.setBackgroundResource(R.color.primary_600)
                binding.tvInsights.setTextColor(ContextCompat.getColor(this, R.color.primary_100))
                binding.ivMe.setImageResource(R.drawable.ic_main_profile_true)
                binding.tvMe.setTextColor(ContextCompat.getColor(this, R.color.white_100))
            }

        }
    }
}