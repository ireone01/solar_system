package com.wavez.trackerwater.feature.fragment.fragmentHistory

import android.annotation.SuppressLint
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
import com.wavez.trackerwater.data.model.HistoryModel
import com.wavez.trackerwater.databinding.FragmentDayBinding
import com.wavez.trackerwater.evenbus.DataUpdatedEvent
import com.wavez.trackerwater.feature.fragment.fragmentHistory.adapter.HistoryAdapter
import com.wavez.trackerwater.feature.fragment.fragmentHistory.dialog.AddRecordDrinkBottomDialog
import com.wavez.trackerwater.feature.fragment.fragmentHistory.viewModel.DayViewModel
import com.wavez.trackerwater.util.TextUtils
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class DayFragment : BaseFragment<FragmentDayBinding>(),
    AddRecordDrinkBottomDialog.AddRecordListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentDayBinding
        get() = FragmentDayBinding::inflate

    private val dayViewModel by viewModels<DayViewModel>()
    override val hasEvenBus: Boolean
        get() = true
    private lateinit var adapter: HistoryAdapter
    private var goal = 5.0
    private var selectedDate = Calendar.getInstance()

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

        adapter.onUpdate = {
            onUpdate(it)
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

    }


    override fun initListener() {
        super.initListener()
        binding.ivAdd.setOnClickListener { openDialogAddRecord() }
        binding.ivPre.setOnClickListener { prevDay() }
        binding.ivNext.setOnClickListener { nextDay() }
    }

    private fun nextDay() {
        selectedDate.add(Calendar.DAY_OF_MONTH, 1)
        updateDateText()
        updateHistoryByDate()
    }

    private fun prevDay() {
        selectedDate.add(Calendar.DAY_OF_MONTH, -1)
        updateDateText()
        updateHistoryByDate()
    }

    private fun updateDateText() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.tvDay.text = dateFormat.format(selectedDate.time)
    }

    private fun updateHistoryByDate() {
        val startOfDay = selectedDate.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val endOfDay = selectedDate.apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis

        dayViewModel.getHistoryByDayRange(startOfDay, endOfDay)
    }


    private fun openDialogAddRecord() {
        AddRecordDrinkBottomDialog.newInstance()
            .show(childFragmentManager, AddRecordDrinkBottomDialog::class.java.simpleName)
    }

    private fun onDelete(drink: HistoryModel) {
        dayViewModel.delete(drink)
        EventBus.getDefault().post(DataUpdatedEvent(TextUtils.DELETE))
    }

    private fun updateChart(historyList: List<HistoryModel>) {
        val lineEntries = mutableListOf<Entry>()
        val calendar = Calendar.getInstance()

        historyList.forEach { drink ->
            calendar.timeInMillis = drink.dateHistory
            val hour = calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) / 60f
            lineEntries.add(Entry(hour, drink.amountHistory.toFloat()))
        }

        binding.chart.axisLeft.apply {
            axisMinimum = 0f
            axisMaximum = 2000.0f
            granularity = 2000.0f / 5
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
            setCircleColor(resources.getColor(R.color.white_100, null))
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

    private fun onUpdate(drink: HistoryModel) {
        showDateTimePicker(drink) { updatedCalendar ->
            drink.dateHistory = updatedCalendar.timeInMillis
            dayViewModel.edit(drink)
            val position = adapter.items.indexOf(drink)
            if (position != -1) {
                adapter.notifyItemChanged(position)
            }
        }
    }

    private fun showDateTimePicker(drink: HistoryModel, onDateTimeUpdated: (Calendar) -> Unit) {
        val calendar = Calendar.getInstance().apply { timeInMillis = drink.dateHistory }

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDataUpdated(event: DataUpdatedEvent) {
        Log.d("minh", "Data updated: ${event.data}")
        dayViewModel.getAllData()
    }

    override fun onSaveRecord(amount: Int, timeAdded: Long) {
        if (amount < 0) {
            binding.root.context.let {
                Toast.makeText(it, "Invalid amount", Toast.LENGTH_SHORT).show()
            }
            return
        }

        dayViewModel.insertIntake(amount)

        dayViewModel.insertHistory(
            HistoryModel(
                amountHistory = amount, dateHistory = timeAdded
            )
        )
        EventBus.getDefault().post(DataUpdatedEvent(TextUtils.UPDATE))
        binding.root.context.let {
            Toast.makeText(it, "Record saved successfully", Toast.LENGTH_SHORT).show()
        }

    }


}