package com.wavez.trackerwater.feature.page.history.fragment.fragmentHistory.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.lingvo.base_common.ui.BaseBottomSheetFragment
import com.wavez.trackerwater.databinding.DialogAddRecordBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UpdateBottomDialog : BaseBottomSheetFragment<DialogAddRecordBinding>() {

    companion object {
        private const val AMOUNT = "AMOUNT"
        private const val TIMESTAMP = "TIMESTAMP"

        fun newInstance(amount: Int, timestamp: Long): UpdateBottomDialog {
            return UpdateBottomDialog().apply {
                arguments = Bundle().apply {
                    putInt(AMOUNT, amount)
                    putLong(TIMESTAMP, timestamp)
                }
            }
        }
    }
    override fun initializeBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): DialogAddRecordBinding {
        return DialogAddRecordBinding.inflate(layoutInflater)
    }

    private var listener: UpdateRecordListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as UpdateRecordListener
        } catch (e: Exception) {
            try {
                listener = parentFragment as UpdateRecordListener
            } catch (e: Exception) {
            }
        }
    }

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        val amount = arguments?.getInt(AMOUNT) ?: 0
        val timestamp = arguments?.getLong(TIMESTAMP) ?: System.currentTimeMillis()

        binding.edtAmount.setText(amount.toString())

        Log.e("minh", "amount $amount" )
        Log.e("minh", "amount $timestamp" )

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp

        val hour = calendar.get(Calendar.HOUR)
        val minute = calendar.get(Calendar.MINUTE)
        val isAM = calendar.get(Calendar.AM_PM) == Calendar.AM

        val days = mutableListOf<String>()
        for (i in 0 until 6) {
            val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
            days.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }

        binding.npDate.apply {
            minValue = 0
            maxValue = days.size - 1
            wrapSelectorWheel = false
            displayedValues = days.toTypedArray()
            value = 0
        }

        binding.npHour.apply {
            minValue = 1
            wrapSelectorWheel = false
            maxValue = 12
            value = if (hour == 0) 12 else hour
        }

        binding.npMin.apply {
            minValue = 0
            wrapSelectorWheel = false
            maxValue = 59
            value = minute
        }

        binding.npType.apply {
            wrapSelectorWheel = false
            minValue = 0
            maxValue = 1
            displayedValues = arrayOf("AM", "PM")
            value = if (isAM) 0 else 1
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

    override fun initListener() {

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
            val amountText = binding.edtAmount.text.toString()
            if (amountText.isBlank()) {
                Toast.makeText(requireContext(), "Amount cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toIntOrNull()
            if (amount == null || amount < 0) {
                Toast.makeText(requireContext(), "Invalid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val timestamp = covertTimeToLongStamp()
            listener?.onUpdateRecord(amount, timestamp)
            dismiss()
        }

    }

    interface UpdateRecordListener {
        fun onUpdateRecord(amount: Int, timeAdded: Long)
    }

}