package com.wavez.trackerwater.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.data.model.DrinkModel
import com.wavez.trackerwater.databinding.FragmentTodayBinding
import com.wavez.trackerwater.ui.drink.DrinkActivity
import com.wavez.trackerwater.ui.drink.DrinkViewModel
import com.wavez.trackerwater.ui.fragment.adapter.DrinkAdapter
import com.wavez.trackerwater.ui.fragment.viewModel.TodayViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodayFragment :BaseFragment<FragmentTodayBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTodayBinding
        get() = FragmentTodayBinding::inflate

    private val todayViewModel by viewModels<TodayViewModel>()
    private val TAG = "minh"

    private lateinit var adapter: DrinkAdapter

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)

        todayViewModel.getAllData()

        adapter = DrinkAdapter(mutableListOf(), this::onDelete)
        binding.rcvGlassWater.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rcvGlassWater.adapter = adapter
    }

    override fun initObserver() {
        super.initObserver()
        todayViewModel.drinkList.observe(viewLifecycleOwner) { drinks ->
            adapter.updateList(drinks)
        }
    }

    override fun initListener() {
        super.initListener()
        binding.btnDrink.setOnClickListener { startActivity(Intent(context, DrinkActivity::class.java)) }
    }
    private fun onDelete(drink: DrinkModel) {
        todayViewModel.delete(drink)
    }

}