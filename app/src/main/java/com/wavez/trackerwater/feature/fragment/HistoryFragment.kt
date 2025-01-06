package com.wavez.trackerwater.feature.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.FragmentHistoryBinding
import com.wavez.trackerwater.feature.fragment.adapter.HistoryViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {
    private lateinit var adapter: HistoryViewPagerAdapter
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHistoryBinding
        get() = FragmentHistoryBinding::inflate


    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        adapter = HistoryViewPagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setPage(position)
            }
        })

    }

    override fun initListener() {
        super.initListener()

        binding.btnDay.setOnClickListener { setPage(0) }
        binding.btnWeek.setOnClickListener { setPage(1) }
        binding.btnMonth.setOnClickListener { setPage(2) }

    }

    override fun initObserver() {
        super.initObserver()
    }

    private fun setPage(page: Int) {
        binding.viewPager.currentItem = page
        when (page) {
            0 -> {
                binding.tvDay.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.vDayTrue.visibility = View.VISIBLE
                binding.vDayFalse.visibility = View.INVISIBLE
                binding.tvWeek.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_56))
                binding.vWeekTrue.visibility = View.INVISIBLE
                binding.vWeekFalse.visibility = View.VISIBLE
                binding.tvMonth.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_56))
                binding.vMonthTrue.visibility = View.INVISIBLE
                binding.vMonthFalse.visibility = View.VISIBLE
            }

            1 -> {
                binding.tvDay.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_56))
                binding.vDayTrue.visibility = View.INVISIBLE
                binding.vDayFalse.visibility = View.VISIBLE
                binding.tvWeek.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                binding.vWeekTrue.visibility = View.VISIBLE
                binding.vWeekFalse.visibility = View.INVISIBLE
                binding.tvMonth.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_56))
                binding.vMonthTrue.visibility = View.INVISIBLE
                binding.vMonthFalse.visibility = View.VISIBLE
            }

            2 -> {
                binding.tvDay.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_56))
                binding.vDayTrue.visibility = View.INVISIBLE
                binding.vDayFalse.visibility = View.VISIBLE
                binding.tvWeek.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_56))
                binding.vWeekTrue.visibility = View.INVISIBLE
                binding.vWeekFalse.visibility = View.VISIBLE
                binding.tvMonth.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_56))
                binding.vMonthTrue.visibility = View.VISIBLE
                binding.vMonthFalse.visibility = View.INVISIBLE
            }


        }
    }

}