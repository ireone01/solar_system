package com.wavez.trackerwater.feature.fragment.fragmentHistory.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseBottomSheetFragment
import com.wavez.trackerwater.databinding.DialogAddRecordBinding
import java.util.Calendar

class AddRecordDrinkBottomDialog : BaseBottomSheetFragment<DialogAddRecordBinding>() {

    companion object {
        fun newInstance() = AddRecordDrinkBottomDialog()
    }
    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogAddRecordBinding {
        return DialogAddRecordBinding.inflate(layoutInflater)
    }

    private var listener: AddRecordListener? =null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddRecordListener

        } catch (e: Exception) {
            try {
                ;listener = parentFragment as AddRecordListener
            } catch (e: Exception) {}

        }

    }

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        binding.npMin.apply {
            minValue = 1
            wrapSelectorWheel = false
            maxValue = 60
        }

        binding.npHour.apply {
            minValue = 1
            wrapSelectorWheel = false
            maxValue = 24
        }

        val calendar = Calendar.getInstance()
        val currentDate = calendar.timeInMillis
        val dateFormat = java.text.SimpleDateFormat("dd/MM", java.util.Locale.getDefault())

        val dates = (0 until 30).map { offset ->
            calendar.timeInMillis = currentDate + (offset - 15) * 24 * 60 * 60 * 1000L
            dateFormat.format(calendar.time)
        }.toTypedArray()

        binding.npDate.apply {
            minValue = 0
            maxValue = dates.size - 1
            wrapSelectorWheel = false
            displayedValues = dates
            value = 15
        }

        val pickerVals = arrayOf("AM", "PM")
        binding.npType.apply {
            minValue = 0
            wrapSelectorWheel = false
            maxValue = 1
            displayedValues = pickerVals
        }

        val amount = binding.edtAmount.text.toString().toIntOrNull() ?: 0
        val dateIndex = binding.npDate.value
        val hour = binding.npHour.value
        val minute = binding.npMin.value
        val isAM = binding.npType.value == 0




    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnSave.setOnClickListener {
//            val amount = binding.edtAmount.text.toString().toIntOrNull() ?: 0
//            if (amount > 0) {
//                Toast.makeText(requireContext(), "Invalid input", Toast.LENGTH_SHORT).show()
//            } else {
//                listener?.onSaveRecord(amount, covertTimeTiLOngStamp())
////                saveRecord(amount, dateIndex, hour, minute, isAM)
//            }
            listener?.onSaveRecord(100, covertTimeTiLOngStamp())
            dismiss()

        }

        binding.npType.setOnValueChangedListener { _, _, newVal ->
            if (newVal == 0) {
                binding.npHour.apply {
                    minValue = 1
                    wrapSelectorWheel = false
                    maxValue = 24
                }
            } else {
                binding.npHour.apply {
                    minValue = 1
                    wrapSelectorWheel = false
                    maxValue = 12
                }
            }
        }
        
    }

    private fun covertTimeTiLOngStamp():Long {
        return System.currentTimeMillis()
    }


    interface AddRecordListener {
        fun onSaveRecord(amount: Int, timeAdded: Long)
    }
}