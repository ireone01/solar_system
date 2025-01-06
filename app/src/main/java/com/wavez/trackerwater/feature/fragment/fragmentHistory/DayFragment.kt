package com.wavez.trackerwater.feature.fragment.fragmentHistory

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.data.model.DrinkModel
import com.wavez.trackerwater.databinding.FragmentDayBinding
import com.wavez.trackerwater.feature.fragment.fragmentHistory.adapter.HistoryAdapter
import com.wavez.trackerwater.feature.fragment.fragmentHistory.viewModel.DayViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class DayFragment : BaseFragment<FragmentDayBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDayBinding
        get() = FragmentDayBinding::inflate

    private val dayViewModel by viewModels<DayViewModel>()
    private lateinit var adapter: HistoryAdapter

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)

        dayViewModel.getAllData()
        dayViewModel.getTotal()

        adapter = HistoryAdapter(mutableListOf(), this::onDelete, this::onUpdate)
        binding.rcvHistory.layoutManager = LinearLayoutManager(context)
        binding.rcvHistory.adapter = adapter

        binding.chart.apply {
            setDrawGridBackground(false)
            isHighlightPerTapEnabled = true
            setTouchEnabled(true)
            setPinchZoom(false)
            setDrawBorders(false)
            animateY(1000)
        }

    }

    override fun initListener() {
        super.initListener()
        binding.chart.data
    }

    override fun initObserver() {
        super.initObserver()
        dayViewModel.historyList.observe(viewLifecycleOwner) { history ->
            adapter.updateList(history)
            updateChart(history)
        }

        dayViewModel.totalAmount.observe(viewLifecycleOwner) { total ->
            val valTotal = total * 0.001
            binding.tvTotal.text = String.format("%.3f l", valTotal)
            binding.tvGoal.text = "5.0 L"


        }
    }

    private fun onDelete(drink: DrinkModel) {
        dayViewModel.delete(drink)
    }

    private fun updateChart(historyList: List<DrinkModel>) {
        // Xử lý dữ liệu cho biểu đồ đường (LineChart)
        val lineEntries = mutableListOf<Entry>()
        val calendar = Calendar.getInstance()

        historyList.forEach { drink ->
            // Chuyển đổi drink.dateDrink (thời gian hiện tại) sang Calendar để lấy giờ và phút
            calendar.timeInMillis = drink.dateDrink
            val hour = calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) / 60f // Tính giờ và phút
            lineEntries.add(Entry(hour, drink.amountDrink.toFloat())) // Lưu giá trị vào Entry (x: giờ, y: amount)
        }

        // Sắp xếp theo trục X (theo giờ)
        lineEntries.sortBy { it.x }

        // Tạo LineDataSet
        val lineDataSet = LineDataSet(lineEntries, "Lượng nước uống (ml)").apply {
            color = resources.getColor(R.color.tv_s, null) // Thay bằng màu phù hợp
            valueTextColor = resources.getColor(R.color.black, null)
            valueTextSize = 12f
            lineWidth = 2f
            setDrawCircles(true)  // Hiển thị các điểm dữ liệu
            setCircleColor(resources.getColor(R.color.tv_s, null)) // Màu của các điểm
            setDrawCircleHole(false) // Không hiển thị lõi điểm
        }

        // Tạo LineData
        val lineData = LineData(lineDataSet)
        binding.chart.data = lineData

        // Thiết lập trục X
        binding.chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f // Hiển thị nhãn sau mỗi giờ
            isGranularityEnabled = true // Kích hoạt granularity
            axisMinimum = 0f
            axisMaximum = 24f
//            valueFormatter = IndexAxisValueFormatter(
//                listOf("0h", "4h", "8h", "12h", "16h", "20h", "24h")
//            )
            textSize = 12f
            textColor = resources.getColor(R.color.black, null)
        }
        binding.chart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when (value.toInt()) {
                    0 -> "0h"
                    4 -> "4h"
                    8 -> "8h"
                    12 -> "12h"
                    16 -> "16h"
                    20 -> "20h"
                    24 -> "24h"
                    else -> ""
                }
            }
        }
        binding.chart.xAxis.setLabelCount(7, true) // Hiển thị đầy đủ 7 nhãn


        // Thiết lập trục Y
        binding.chart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 600.0f
            granularity = 600.0f / 5 // Chia 5 phần cho trục Y
            textSize = 12f
            textColor = resources.getColor(R.color.black, null)
        }

        // Vô hiệu hóa trục Y bên phải
        binding.chart.axisRight.isEnabled = false

        // Thiết lập biểu đồ
        binding.chart.apply {
            description.isEnabled = false
            legend.isEnabled = true
            setTouchEnabled(true)
            setPinchZoom(false)
            animateY(1000)
            invalidate() // Cập nhật biểu đồ
        }

}




    private fun onUpdate(drink: DrinkModel) {
        showDateTimePicker(drink) { updatedCalendar ->
            drink.dateDrink = updatedCalendar.timeInMillis
            dayViewModel.edit(drink)

            val position = adapter.historyList.indexOf(drink)
            if (position != -1) {
                adapter.notifyItemChanged(position)
            }

        }
    }

    private fun showDateTimePicker(drink: DrinkModel, onDateTimeUpdated: (Calendar) -> Unit) {
        val calendar = Calendar.getInstance().apply { timeInMillis = drink.dateDrink }

        DatePickerDialog(
            binding.root.context,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                TimePickerDialog(
                    binding.root.context,
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        onDateTimeUpdated(calendar)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

}