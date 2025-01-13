package com.wavez.trackerwater.feature.page.today

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentTodayBinding
import com.wavez.trackerwater.evenbus.DataUpdatedEvent
import com.wavez.trackerwater.extension.gone
import com.wavez.trackerwater.extension.visible
import com.wavez.trackerwater.feature.drink.DrinkActivity
import com.wavez.trackerwater.feature.page.today.adapter.RecentDrinkAdapter
import com.wavez.trackerwater.feature.page.today.viewModel.TodayViewModel
import com.wavez.trackerwater.feature.reminder.ReminderActivity
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class TodayFragment : BaseFragment<FragmentTodayBinding>() {

    companion object {
        const val TOTAL_DRINK = 2000f

        private const val PROGRESS_100 = 100f

        private const val PROGRESS_95 = 95f

        private const val PROGRESS_80 = 80f
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTodayBinding
        get() = FragmentTodayBinding::inflate

    private val todayViewModel by viewModels<TodayViewModel>()

    private lateinit var adapter: RecentDrinkAdapter

    override val hasEvenBus: Boolean
        get() = true

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        binding.waterWaveView.setAnimDuration(3500)
        initAdapter()
    }

    override fun initObserver() {
        super.initObserver()
        todayViewModel.historyList.observe(viewLifecycleOwner) { drinks ->
            adapter.setData(drinks)
        }

        todayViewModel.totalDrank.observe(viewLifecycleOwner) { total ->
            syncUiDrinkWater(total)
        }

        todayViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visible()
            } else {
                binding.progressBar.gone()
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun syncUiDrinkWater(totalDrink: Int) {
        binding.tvTotalDrink.text = totalDrink.toString()
        val progress = ((totalDrink.toFloat() / TOTAL_DRINK) * PROGRESS_100).toInt()
        binding.waterWaveView.progressValue = if (progress > PROGRESS_95) PROGRESS_95.toInt() else progress
        binding.tvProgress.text = "$progress%"
        val progressHeight = (binding.viewProgress.height * (progress / PROGRESS_100)).toInt()

        val positionProgress =
            if (progress >= PROGRESS_95) -(binding.viewProgress.height.toFloat() - binding.tvPercent100.height) else -progressHeight.toFloat()

        binding.tvProgress.translationY = positionProgress

        binding.tvTotalDrink.isActivated = progress >= PROGRESS_80
        binding.tvUnit.isActivated = progress >= PROGRESS_80
        binding.tvNextReminder.isActivated = progress >= PROGRESS_80
        binding.tvTimeDownReminder.isActivated = progress >= PROGRESS_80


    }

    override fun initListener() {
        super.initListener()
        binding.btnDrink.setOnClickListener {
            startActivity(Intent(context, DrinkActivity::class.java))
        }

        binding.ivEditReminder.setOnClickListener {
            startActivity(Intent(requireContext(), ReminderActivity::class.java))
        }

        binding.ivDelete.setOnClickListener {
            todayViewModel.delete()
            binding.llOption.gone()
        }

        binding.btnDrinkRecent.setOnClickListener {
            todayViewModel.insertHistory()
            binding.llOption.gone()
        }

        binding.root.setOnClickListener {
            binding.llOption.gone()
        }


    }

    private fun initAdapter() {
        adapter = RecentDrinkAdapter()
        binding.rcvGlassWater.adapter = adapter
        adapter.onSelect = {
            todayViewModel.recentDrinkSelected = it
            binding.llOption.visible()
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDataUpdated(event: DataUpdatedEvent) {
        Log.d("minh", "Data updated: ${event.data}")
        todayViewModel.getAllData()
    }

}