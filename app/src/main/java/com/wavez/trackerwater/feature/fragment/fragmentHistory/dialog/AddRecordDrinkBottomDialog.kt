package com.wavez.trackerwater.feature.fragment.fragmentHistory.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
                listener = parentFragment as AddRecordListener
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

    }

    private fun covertTimeTiLOngStamp(): Long {
        // Lấy giá trị từ giao diện
        val dateIndex = binding.npDate.value
        val hour = binding.npHour.value
        val minute = binding.npMin.value
        val isAM = binding.npType.value == 0

        // Lấy ngày hiện tại và thêm ngày được chọn
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, dateIndex - 15)

        // Thiết lập giờ và phút
        var adjustedHour = hour
        if (!isAM) {
            if (hour < 12) adjustedHour += 12 // Chuyển PM thành giờ 24h
        } else if (hour == 12) {
            adjustedHour = 0 // Xử lý đặc biệt cho 12 AM
        }

        calendar.set(Calendar.HOUR_OF_DAY, adjustedHour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // Trả về timestamp
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
                listener?.onSaveRecord(amount, covertTimeTiLOngStamp())
            }
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




    interface AddRecordListener {
        fun onSaveRecord(amount: Int, timeAdded: Long)
    }
}