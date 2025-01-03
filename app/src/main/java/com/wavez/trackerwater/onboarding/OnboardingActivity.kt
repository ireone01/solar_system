package com.wavez.trackerwater.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.ActivityMe
import com.wavez.trackerwater.databinding.ActivityMeBinding
import com.wavez.trackerwater.databinding.ActivityOnboardingBinding

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

    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {
        super.initListener()
        initAdapter()
        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < fragmentList.size - 1) {
                binding.viewPager.currentItem = currentItem + 1
            }
            if(currentItem == fragmentList.size -1){
                val intent = Intent(this , ActivityMe::class.java )
                startActivity(intent)
            }
        }

        binding.btnBack.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem > 0) {
                binding.viewPager.currentItem = currentItem - 1
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("ViewPager2", "Page selected: $position")
            }
        })


    }

    private fun initAdapter() {

        binding.viewPager.adapter = ViewPagerAdapter(this, fragmentList)
    }
}
