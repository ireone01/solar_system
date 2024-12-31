package com.wavez.trackerwater.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.ActivityMainBinding
import com.wavez.trackerwater.fragment.adapter.MainViewPagerAdapter

class MainActivity: BaseActivity<ActivityMainBinding>() {
    private lateinit var adapter: MainViewPagerAdapter
    override fun createBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
        adapter = MainViewPagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {
        super.initListener()
        binding.btnToday.setOnClickListener { setPage(0)}
        binding.btnHistory.setOnClickListener { setPage(1) }
        binding.btnInsights.setOnClickListener { setPage(2) }
        binding.btnMe.setOnClickListener { setPage(3) }
    }

    @SuppressLint("ResourceAsColor")
    private fun setPage(page: Int){
        binding.viewPager.setCurrentItem(page, false)
        when(page){
            0-> {
                binding.ivToday.setImageResource(R.drawable.ic_main_today_true)
                binding.tvToday.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.ivHistory.setImageResource(R.drawable.ic_main_history_false)
                binding.tvHistory.setTextColor(ContextCompat.getColor(this, R.color.black_2))
                binding.ivInsights.setImageResource(R.drawable.ic_main_insights_false)
                binding.tvInsights.setTextColor(ContextCompat.getColor(this, R.color.black_2))
                binding.ivMe.setImageResource(R.drawable.ic_main_me_false)
                binding.tvMe.setTextColor(ContextCompat.getColor(this, R.color.black_2))
            }
            1->{
                binding.ivToday.setImageResource(R.drawable.ic_main_today_false)
                binding.tvToday.setTextColor(ContextCompat.getColor(this, R.color.black_2))
                binding.ivHistory.setImageResource(R.drawable.ic_main_history_true)
                binding.tvHistory.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.ivInsights.setImageResource(R.drawable.ic_main_insights_false)
                binding.tvInsights.setTextColor(ContextCompat.getColor(this, R.color.black_2))
                binding.ivMe.setImageResource(R.drawable.ic_main_me_false)
                binding.tvMe.setTextColor(ContextCompat.getColor(this, R.color.black_2))
            }
            2->{
                binding.ivToday.setImageResource(R.drawable.ic_main_today_false)
                binding.tvToday.setTextColor(ContextCompat.getColor(this, R.color.black_2))
                binding.ivHistory.setImageResource(R.drawable.ic_main_history_false)
                binding.tvHistory.setTextColor(ContextCompat.getColor(this, R.color.black_2))
                binding.ivInsights.setImageResource(R.drawable.ic_main_insights_true)
                binding.tvInsights.setTextColor(ContextCompat.getColor(this, R.color.black))
                binding.ivMe.setImageResource(R.drawable.ic_main_me_false)
                binding.tvMe.setTextColor(ContextCompat.getColor(this, R.color.black_2))
            }
            3->{
                binding.ivToday.setImageResource(R.drawable.ic_main_today_false)
                binding.tvToday.setTextColor(ContextCompat.getColor(this, R.color.black_2))
                binding.ivHistory.setImageResource(R.drawable.ic_main_history_false)
                binding.tvHistory.setTextColor(ContextCompat.getColor(this, R.color.black_2))
                binding.ivInsights.setImageResource(R.drawable.ic_main_insights_false)
                binding.tvInsights.setTextColor(ContextCompat.getColor(this, R.color.black_2))
                binding.ivMe.setImageResource(R.drawable.ic_main_me_true)
                binding.tvMe.setTextColor(ContextCompat.getColor(this, R.color.black))
            }

        }
    }
}