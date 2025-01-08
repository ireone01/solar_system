package com.wavez.trackerwater.feature.fragment.fragmentHistory

import com.wavez.trackerwater.feature.fragment.fragmentHistory.adapter.HistoryAdapter
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.wavez.trackerwater.data.model.DrinkModel
import com.wavez.trackerwater.databinding.FragmentDayBinding
import com.wavez.trackerwater.feature.fragment.fragmentHistory.dialog.AddRecordDrinkBottomDialog
import com.wavez.trackerwater.feature.fragment.fragmentHistory.viewModel.DayViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class DayFragment : BaseFragment<FragmentDayBinding>(),
    AddRecordDrinkBottomDialog.AddRecordListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDayBinding
        get() = FragmentDayBinding::inflate

    private val dayViewModel by viewModels<DayViewModel>()

    private lateinit var adapter: HistoryAdapter

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)

        dayViewModel.getAllData()
        dayViewModel.getTotal()
        initAdapter()

        binding.chart.apply {
            setDrawGridBackground(false)
            isHighlightPerTapEnabled = true
            setTouchEnabled(true)
            setPinchZoom(false)
            setDrawBorders(false)
            animateY(1000)
        }

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

        adapter.onUpdate = {
            onUpdate(it)
        }
    }

    override fun initObserver() {
        super.initObserver()
        dayViewModel.historyList.observe(viewLifecycleOwner) { history ->
            adapter.setData(history)
            updateChart(history)
        }

        dayViewModel.totalAmount.observe(viewLifecycleOwner) { total ->
            val valTotal = total * 0.001
            binding.tvTotal.text = String.format("%.3f l", valTotal)
            binding.tvGoal.text = "5.0 L"
        }
    }


    override fun initListener() {
        super.initListener()
        binding.ivAdd.setOnClickListener { openDialogAddRecord() }
    }

    private fun openDialogAddRecord() {
        AddRecordDrinkBottomDialog.newInstance()
            .show(childFragmentManager, AddRecordDrinkBottomDialog::class.java.simpleName)
    }

    private fun saveRecord(amount: Int, dateIndex: Int, hour: Int, minute: Int, am: Boolean) {
        if (amount != null && amount > 0) {
            binding.root.context.let {
                Toast.makeText(it, "Invalid amount", Toast.LENGTH_SHORT).show()
            }
            return
        }
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, dateIndex)
        calendar.set(Calendar.HOUR, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.AM_PM, if (am) Calendar.AM else Calendar.PM)

        val drinkTime = calendar.timeInMillis

        dayViewModel.insertDrink(
            DrinkModel(
                amountDrink = amount, dateDrink = drinkTime
            )
        )

        binding.root.context.let {
            Toast.makeText(it, "Record saved successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onDelete(drink: DrinkModel) {
        dayViewModel.delete(drink)
    }

    private fun updateChart(historyList: List<DrinkModel>) {
        val lineEntries = mutableListOf<Entry>()
        val calendar = Calendar.getInstance()

        historyList.forEach { drink ->
            calendar.timeInMillis = drink.dateDrink
            val hour = calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) / 60f
            lineEntries.add(Entry(hour, drink.amountDrink.toFloat()))
        }

        binding.chart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 600.0f
            granularity = 600.0f / 5
            textSize = 12f
            textColor = resources.getColor(R.color.black, null)
        }

        binding.chart.axisRight.isEnabled = false

        binding.chart.xAxis.apply {
            axisMinimum = 0f
            axisMaximum = 24f
            granularity = 24f / 7
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 12f
            textColor = resources.getColor(R.color.black, null)
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
            setCircleColor(resources.getColor(R.color.tv_s, null))
            lineWidth = 4f
        }

        val lineData = LineData(lineDataSet)

        binding.chart.apply {
            data = lineData
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(false)
            animateY(1000)
            invalidate()
        }
    }

    private fun onUpdate(drink: DrinkModel) {
        showDateTimePicker(drink) { updatedCalendar ->
            drink.dateDrink = updatedCalendar.timeInMillis
            dayViewModel.edit(drink)
            val position = adapter.items.indexOf(drink)
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
                    binding.root.context, { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        onDateTimeUpdated(calendar)
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onSaveRecord(amount: Int, timeAdded: Long) {
        Log.d("hehehehe", "onSaveRecord: " + amount)
        if (amount < 0) {
            binding.root.context.let {
                Toast.makeText(it, "Invalid amount", Toast.LENGTH_SHORT).show()
            }
            return
        }


        dayViewModel.insertDrink(
            DrinkModel(
                amountDrink = amount, dateDrink = timeAdded
            )
        )

        binding.root.context.let {
            Toast.makeText(it, "Record saved successfully", Toast.LENGTH_SHORT).show()
        }
    }

}