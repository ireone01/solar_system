package com.wavez.trackerwater.feature.page.history.fragment.fragmentHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.data.model.HistoryDrink
import com.wavez.trackerwater.databinding.FragmentWeekBinding
import com.wavez.trackerwater.feature.page.history.fragment.fragmentHistory.viewModel.WeekViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class WeekFragment : BaseFragment<FragmentWeekBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentWeekBinding
        get() = FragmentWeekBinding::inflate

    private val weekViewModel by viewModels<WeekViewModel>()

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
    }

    override fun initListener() {
        super.initListener()
        binding.ivPre.setOnClickListener { weekViewModel.previousWeek() }
        binding.ivNext.setOnClickListener { weekViewModel.nextWeek() }

    }

    override fun initObserver() {
        super.initObserver()
        weekViewModel.historyList.observe(viewLifecycleOwner) { list ->
            updateChart(list)
        }

        weekViewModel.total.observe(viewLifecycleOwner, { total ->
            binding.tvTotal.text = total.toString()
        })

        weekViewModel.average.observe(viewLifecycleOwner, { avg ->
            binding.tvAvg.text = avg.toString()
        })

        weekViewModel.weekRange.observe(viewLifecycleOwner, { weekRange ->
            binding.tvDay.text = weekRange
        })

    }

    private fun updateChart(historyList: List<HistoryDrink>) {
        val barEntries = mutableListOf<BarEntry>()
        val dayLabels = mutableListOf<String>()

        val calendar = Calendar.getInstance()

        val dailyData = FloatArray(7) { 0f }

        val daysOfWeek =
            arrayOf("S", "M", "T", "W", "T", "F", "S")

        if (historyList.isNotEmpty()) {
            historyList.forEach { drink ->
                calendar.timeInMillis = drink.dateHistory
                val dayOfWeek =
                    calendar.get(Calendar.DAY_OF_WEEK) - 1

                dailyData[dayOfWeek] += drink.amountHistory.toFloat()
            }
        }

        // Thêm các giá trị vào barEntries
        for (i in 0..6) {
            dayLabels.add(daysOfWeek[i])
            barEntries.add(
                BarEntry(
                    i.toFloat(),
                    dailyData[i]
                )
            )
        }

        binding.chart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 2000f
            granularity = 500f
            textSize = 12f
            textColor = resources.getColor(R.color.black, null)
        }

        binding.chart.axisRight.isEnabled = false

        binding.chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            textSize = 12f
            setDrawGridLines(false)
            textColor = resources.getColor(R.color.black, null)
            valueFormatter =
                IndexAxisValueFormatter(dayLabels)
        }

        val barDataSet = BarDataSet(barEntries, "").apply {
            valueTextSize = 12f
            valueTextColor = resources.getColor(R.color.black, null)
            color = resources.getColor(R.color.white_100, null)
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