package com.wavez.trackerwater.feature.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.databinding.ActivityOnboardingBinding
import com.wavez.trackerwater.feature.home.MainActivity
import com.wavez.trackerwater.feature.onboarding.adapter.ViewPagerAdapter
import com.wavez.trackerwater.feature.onboarding.fragment.FragmentOnboarding10
import com.wavez.trackerwater.feature.onboarding.fragment.FragmentOnboarding2
import com.wavez.trackerwater.feature.onboarding.fragment.FragmentOnboarding3
import com.wavez.trackerwater.feature.onboarding.fragment.FragmentOnboarding4
import com.wavez.trackerwater.feature.onboarding.fragment.FragmentOnboarding5
import com.wavez.trackerwater.feature.onboarding.fragment.FragmentOnboarding6
import com.wavez.trackerwater.feature.onboarding.fragment.FragmentOnboarding7
import com.wavez.trackerwater.feature.onboarding.fragment.FragmentOnboarding8
import com.wavez.trackerwater.feature.onboarding.fragment.FragmentOnboarding9

class OnboardingActivity : BaseActivity<ActivityOnboardingBinding>() {
    companion object {

        fun newIntent(context: Context) = Intent(context, OnboardingActivity::class.java)
    }

    private val fragmentList = arrayListOf(
        FragmentOnboarding2(),
        FragmentOnboarding3(),
        FragmentOnboarding4(),
        FragmentOnboarding5(),
        FragmentOnboarding6(),
        FragmentOnboarding7(),
        FragmentOnboarding8(),
        FragmentOnboarding9(),
        FragmentOnboarding10(),
    )

    override fun createBinding(): ActivityOnboardingBinding {
        return ActivityOnboardingBinding.inflate(layoutInflater)

    }

    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
        binding.progressBar.max = fragmentList.size * 10
        initAdapter()


    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {
        super.initListener()

        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < fragmentList.size - 1) {
                binding.viewPager.currentItem = currentItem + 1
            }
            if (currentItem == fragmentList.size - 1) {
                startActivity(MainActivity.newIntent(this))
            }
        }

        binding.btnBack.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem > 0) {
                binding.viewPager.currentItem = currentItem - 1
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                when (position) {
                    0 -> {
                        binding.btnBack.visibility = View.INVISIBLE
                        binding.progressBar.visibility = View.INVISIBLE
                    }

                    else -> {
                        binding.btnBack.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
                val progress =
                    ((position + positionOffset) / (fragmentList.size - 1)) * (binding.progressBar.max)
                binding.progressBar.progress = progress.toInt()
            }
        })

    }

    private fun initAdapter() {

        binding.viewPager.adapter = ViewPagerAdapter(this, fragmentList)
    }
}
