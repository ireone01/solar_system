package com.wavez.trackerwater.feature.page.history.fragment.fragmentHistory

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.data.model.HistoryDrink
import com.wavez.trackerwater.databinding.FragmentDayBinding
import com.wavez.trackerwater.evenbus.DataUpdatedEvent
import com.wavez.trackerwater.feature.page.history.fragment.fragmentHistory.adapter.HistoryAdapter
import com.wavez.trackerwater.feature.page.history.fragment.fragmentHistory.dialog.InsertBottomDialog
import com.wavez.trackerwater.feature.page.history.fragment.fragmentHistory.dialog.UpdateBottomDialog
import com.wavez.trackerwater.feature.page.history.fragment.fragmentHistory.viewModel.DayViewModel
import com.wavez.trackerwater.util.TextUtils
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Calendar

@AndroidEntryPoint
class DayFragment : BaseFragment<FragmentDayBinding>(), InsertBottomDialog.AddRecordListener,
    UpdateBottomDialog.UpdateRecordListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDayBinding
        get() = FragmentDayBinding::inflate

    private val dayViewModel by viewModels<DayViewModel>()
    override val hasEvenBus: Boolean
        get() = true
    private lateinit var adapter: HistoryAdapter
    private var goal = 5.0

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)

        initAdapter()
    }

    private fun initAdapter() {
        adapter = HistoryAdapter()
        binding.rcvHistory.layoutManager = LinearLayoutManager(context)
        binding.rcvHistory.adapter = adapter

        adapter.onSelected = {

        }

        adapter.onDelete = { it ->
            onDelete(it)
        }

        adapter.onUpdate = { historyModel ->
            onUpdate(historyModel)
        }
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun initObserver() {
        super.initObserver()
        dayViewModel.historyList.observe(viewLifecycleOwner) { history ->
            adapter.setData(history)
            updateChart(history)
        }

        dayViewModel.totalAmount.observe(viewLifecycleOwner) { total ->
            val valTotal = total * 0.001
            binding.tvTotal.text = String.format("%.3f l", valTotal)
            binding.tvGoal.text = goal.toString() + "l"
        }

        dayViewModel.currentDateText.observe(viewLifecycleOwner) { date ->
            binding.tvDay.text = date
        }

    }

    override fun initListener() {
        super.initListener()
        binding.ivAdd.setOnClickListener { openDialogAddRecord() }
        binding.ivPre.setOnClickListener { dayViewModel.prevDay() }
        binding.ivNext.setOnClickListener { dayViewModel.nextDay() }
    }

    private fun openDialogAddRecord() {
        InsertBottomDialog.newInstance().show(childFragmentManager, InsertBottomDialog::class.java.simpleName)
    }

    private fun onDelete(drink: HistoryDrink) {
        dayViewModel.delete(drink)
        EventBus.getDefault().post(DataUpdatedEvent(TextUtils.DELETE))
    }

    private fun onUpdate(drink: HistoryDrink) {
        UpdateBottomDialog.newInstance(drink.amountHistory, drink.dateHistory)
            .show(childFragmentManager, UpdateBottomDialog::class.java.simpleName)
    }

    private fun updateChart(historyList: List<HistoryDrink>) {
        val lineEntries = mutableListOf<Entry>()
        val calendar = Calendar.getInstance()

        historyList.forEach { drink ->
            calendar.timeInMillis = drink.dateHistory
            val hour = calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) / 60f
            val amount = drink.amountHistory.toFloat()

            if (hour in 0f..24f && amount >= 0) {
                lineEntries.add(Entry(hour, amount))
            }
        }

        binding.chart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 2000f
            granularity = 400f
            textSize = 12f
            setDrawGridLines(false)
            textColor = resources.getColor(R.color.white_100, null)
        }

        binding.chart.axisRight.isEnabled = false

        binding.chart.xAxis.apply {
            axisMinimum = 0f
            axisMaximum = 24f
            granularity = 3f
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 12f
            textColor = resources.getColor(R.color.white_100, null)
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val hour = value.toInt()
                    return "${hour}h"
                }
            }
        }

        val lineDataSet = LineDataSet(lineEntries, "").apply {
            setDrawValues(false)
            setDrawCircles(true)
            setCircleColor(resources.getColor(R.color.white_100, null))
            color = resources.getColor(R.color.white_64, null)
            lineWidth = 2f
        }

        val lineData = LineData(lineDataSet)

        binding.chart.apply {
            data = lineData
            description.isEnabled = false
            legend.isEnabled = false
            legend.textSize = 12f
            setTouchEnabled(true)
            setPinchZoom(true)
            animateY(1000)
            invalidate()
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDataUpdated(event: DataUpdatedEvent) {
        Log.d("minh", "Data updated: ${event.data} ")
        dayViewModel.getAllData()
    }

    override fun onSaveRecord(amount: Int, timeAdded: Long) {
        if (amount < 0) {
            binding.root.context.let {
                Toast.makeText(it, "Invalid amount", Toast.LENGTH_SHORT).show()
            }
            return
        }

        dayViewModel.insertHistory(
            HistoryDrink(
                amountHistory = amount, dateHistory = timeAdded
            )
        )
        EventBus.getDefault().post(DataUpdatedEvent(TextUtils.INSERT))
    }

        override fun onUpdateRecord(amount: Int, timeAdded: Long) {
            if (amount < 0) {
                binding.root.context.let {
                    Toast.makeText(it, "Invalid amount", Toast.LENGTH_SHORT).show()
                }
                return
            }

            dayViewModel.edit(
                HistoryDrink(
                    amountHistory = amount, dateHistory = timeAdded
                )
            )
            EventBus.getDefault().post(DataUpdatedEvent(TextUtils.UPDATE))
        }
}