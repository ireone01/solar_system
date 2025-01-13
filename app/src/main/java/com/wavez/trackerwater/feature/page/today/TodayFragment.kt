package com.wavez.trackerwater.feature.page.today

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.data.model.RecentDrink
import com.wavez.trackerwater.databinding.DialogCongratulationsBinding
import com.wavez.trackerwater.databinding.FragmentTodayBinding
import com.wavez.trackerwater.evenbus.DataUpdatedEvent
import com.wavez.trackerwater.extension.gone
import com.wavez.trackerwater.extension.visible
import com.wavez.trackerwater.feature.drink.DrinkActivity
import com.wavez.trackerwater.feature.page.today.adapter.RecentDrinkAdapter
import com.wavez.trackerwater.feature.page.today.dialog.CongratulationsDialog
import com.wavez.trackerwater.feature.page.today.viewModel.TodayViewModel
import com.wavez.trackerwater.feature.reminder.ReminderActivity
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class TodayFragment : BaseFragment<FragmentTodayBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTodayBinding
        get() = FragmentTodayBinding::inflate

    private val todayViewModel by viewModels<TodayViewModel>()
    private var isDialogShown = false
    private val TAG = "minh"

    private lateinit var adapter: RecentDrinkAdapter

    override val hasEvenBus: Boolean
        get() = true

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        initAdapter()
    }

    @SuppressLint("SetTextI18n")
    override fun initObserver() {
        super.initObserver()
        todayViewModel.historyList.observe(viewLifecycleOwner) { drinks ->
            adapter.setData(drinks)
        }

        todayViewModel.totalAmount.observe(viewLifecycleOwner) { total ->
            binding.tvTotal.text = total.toString()
            val progressValue = ((total.toFloat() / 2000f) * 100f).toInt()
            binding.waterWaveView.progressValue = progressValue

            if (progressValue > 100) {
                binding.waterWaveView.progressValue = 100
            }

//            if (progressValue in 1..99 && !isDialogShown) {
//                isDialogShown = true
//                CongratulationsDialog.newInstance(progressValue)
//                    .show(childFragmentManager, CongratulationsDialog::class.java.simpleName)
//            } else if (progressValue >= 100 && !isDialogShown) {
//                isDialogShown = true
//                CongratulationsDialog.newInstance(progressValue)
//                    .show(childFragmentManager, CongratulationsDialog::class.java.simpleName)
//            } else if (progressValue < 100) {
//                isDialogShown = false
//            }
        }

        todayViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visible()
            } else {
                binding.progressBar.gone()
            }
        }

    }

    override fun initListener() {
        super.initListener()
        binding.btnDrink.setOnClickListener {
            startActivity(Intent(context, DrinkActivity::class.java))
        }

        binding.ivEditReminder.setOnClickListener {
            startActivity(Intent(requireContext(), ReminderActivity::class.java))
        }
    }

    private fun initAdapter() {
        adapter = RecentDrinkAdapter()
        binding.rcvGlassWater.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rcvGlassWater.adapter = adapter
        adapter.onSelect = {
            onSelect(it)
        }
    }

    private fun onSelect(drink: RecentDrink) {
        binding.option.visible()
        binding.ivDelete.setOnClickListener {
            todayViewModel.delete(drink)
            binding.option.gone()
        }
        binding.btnDrink2.setOnClickListener {
            todayViewModel.insertHistory(drink.amountHistory)
            binding.option.gone()
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDataUpdated(event: DataUpdatedEvent) {
        Log.d("minh", "Data updated: ${event.data}")
        todayViewModel.getAllData()
    }

}