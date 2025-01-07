package com.wavez.trackerwater.feature.fragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.data.model.DrinkModel
import com.wavez.trackerwater.databinding.FragmentTodayBinding
import com.wavez.trackerwater.feature.drink.DrinkActivity
import com.wavez.trackerwater.feature.fragment.adapter.DrinkAdapter
import com.wavez.trackerwater.feature.fragment.viewModel.TodayViewModel
import com.wavez.trackerwater.feature.reminder.ReminderActivity
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
        todayViewModel.getTotal()

        adapter = DrinkAdapter(mutableListOf(), this::onClick)
        binding.rcvGlassWater.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rcvGlassWater.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    override fun initObserver() {
        super.initObserver()
        todayViewModel.drinkList.observe(viewLifecycleOwner) { drinks ->
            adapter.updateList(drinks)
        }
        todayViewModel.totalAmount.observe(viewLifecycleOwner) { total ->
            binding.tvTotal.text = total.toString()
            binding.waterWaveView.progress = total.toInt()
            binding.waterWaveView.max = 2000
        }
    }

    override fun initListener() {
        super.initListener()
        binding.btnDrink.setOnClickListener { drinkActivityLauncher.launch(Intent(context, DrinkActivity::class.java)) }
        binding.ivEditReminder.setOnClickListener { startActivity(Intent(requireContext(), ReminderActivity::class.java)) }

    }
    private val drinkActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            todayViewModel.getAllData()
            todayViewModel.getTotal()
        }
    }

    private fun onClick(drink: DrinkModel) {

    }

}