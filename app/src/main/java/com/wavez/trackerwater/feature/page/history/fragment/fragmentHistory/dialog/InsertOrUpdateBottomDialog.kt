package com.wavez.trackerwater.feature.page.history.fragment.fragmentHistory.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.lingvo.base_common.ui.BaseBottomSheetFragment
import com.wavez.trackerwater.databinding.DialogAddRecordBinding
import java.util.Calendar

class InsertOrUpdateBottomDialog : BaseBottomSheetFragment<DialogAddRecordBinding>() {

    companion object {
        fun newInstance() = InsertOrUpdateBottomDialog()
    }

    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogAddRecordBinding {
        return DialogAddRecordBinding.inflate(layoutInflater)
    }

    private var listener: AddRecordListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddRecordListener

        } catch (e: Exception) {
            try {
                listener = parentFragment as AddRecordListener
            } catch (e: Exception) {
            }

        }

    }

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        binding.npMin.apply {
            minValue = 0
            wrapSelectorWheel = false
            maxValue = 59
            value = 0

        }

        binding.npHour.apply {
            minValue = 0
            wrapSelectorWheel = false
            maxValue = 11
            value = 0

        }

        val pickerVals = arrayOf("AM", "PM")
        binding.npType.apply {
            minValue = 0
            wrapSelectorWheel = false
            maxValue = 1
            value = 0
            displayedValues = pickerVals
        }

        val calendar = Calendar.getInstance()
        val currentDate = calendar.timeInMillis
        val dateFormat = java.text.SimpleDateFormat("MMM d", java.util.Locale.ENGLISH)

        val dates = (0..6).map { offset ->
            calendar.timeInMillis = currentDate - offset * 24 * 60 * 60 * 1000L
            dateFormat.format(calendar.time)
        }.toTypedArray()

        binding.npDate.apply {
            minValue = 0
            maxValue = dates.size - 1
            wrapSelectorWheel = false
            displayedValues = dates
            value = 0
        }
    }

    private fun covertTimeToLongStamp(): Long {
        val dateIndex = binding.npDate.value
        val hour = binding.npHour.value
        val minute = binding.npMin.value
        val isAM = binding.npType.value == 0

        val calendar = Calendar.getInstance()
        val currentDate = calendar.timeInMillis

        calendar.timeInMillis = currentDate - dateIndex * 24 * 60 * 60 * 1000L

        var adjustedHour = hour
        if (hour == 12) {
            adjustedHour = if (isAM) 0 else 12
        } else {
            if (!isAM && hour < 12) {
                adjustedHour += 12
            }
        }

        calendar.set(Calendar.HOUR_OF_DAY, adjustedHour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis
    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            val amount = binding.edtAmount.text.toString().toIntOrNull() ?: 0
            if (amount < 0) {
                Toast.makeText(requireContext(), "Invalid input", Toast.LENGTH_SHORT).show()
            } else {
                listener?.onSaveRecord(amount, covertTimeToLongStamp())
            }
            dismiss()
        }

    }

    interface AddRecordListener {
        fun onSaveRecord(amount: Int, timeAdded: Long)
    }
}