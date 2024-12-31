package com.wavez.trackerwater.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.FragmentTodayBinding
import com.wavez.trackerwater.ui.DrinkActivity

class TodayFragment :BaseFragment<FragmentTodayBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTodayBinding
        get() = FragmentTodayBinding::inflate

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {
        super.initListener()
        binding.btnDrink.setOnClickListener { startActivity(Intent(context, DrinkActivity::class.java)) }
        binding.rcvGlassWater.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }
}