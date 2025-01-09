package com.wavez.trackerwater.feature.fragment.fragmentHistory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.data.model.HistoryModel
import com.wavez.trackerwater.databinding.FragmentMonthBinding
import com.wavez.trackerwater.feature.fragment.fragmentHistory.viewModel.MonthViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class MonthFragment : BaseFragment<FragmentMonthBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMonthBinding
        get() = FragmentMonthBinding::inflate

    private val monthViewModel by viewModels<MonthViewModel>()

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
    }

    override fun initListener() {
        super.initListener()
    }

    override fun initObserver() {
        super.initObserver()
        monthViewModel.getHistoryByMonth(System.currentTimeMillis())

        monthViewModel.historyList.observe(viewLifecycleOwner) { list ->
            updateChart(list)
        }
    }

    private fun updateChart(historyList: List<HistoryModel>) {
        val barEntries = mutableListOf<BarEntry>()
        val dayLabels = mutableListOf<String>()

        val calendar = Calendar.getInstance()
        val totalDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        val dailyData = FloatArray(totalDaysInMonth) { 0f }

        if (historyList.isNotEmpty()) {
            historyList.forEach { drink ->
                calendar.timeInMillis = drink.dateHistory
                val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

                dailyData[dayOfMonth - 1] += drink.amountHistory.toFloat()
            }
        }

        for (day in 1..totalDaysInMonth) {
            dayLabels.add(day.toString())
            barEntries.add(BarEntry(day.toFloat(), dailyData[day - 1]))
        }

        binding.chart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 2000f
            granularity = 500f
            setDrawGridLines(false)
            textSize = 12f
            textColor = resources.getColor(R.color.black, null)
        }

        binding.chart.axisRight.isEnabled = false

        binding.chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            textSize = 12f
            textColor = resources.getColor(R.color.black, null)
            valueFormatter = IndexAxisValueFormatter(dayLabels)
        }

        val barDataSet = BarDataSet(barEntries, "").apply {
            valueTextSize = 12f
            valueTextColor = resources.getColor(R.color.black, null)
            color = resources.getColor(R.color.tv_s, null)
            setDrawValues(false)
        }

        val barData = BarData(barDataSet).apply {
            barWidth = 0.9f
        }

        binding.chart.apply {
            data = barData
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(false)
            animateY(1000)
            invalidate()
        }
    }
}
