package com.wavez.trackerwater.feature.myprofile

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.lingvo.base_common.ui.BaseDialogFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.FragmentMeTimeFormatBinding

class FragmentDialogTimeFormat : BaseDialogFragment<FragmentMeTimeFormatBinding>() {
    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMeTimeFormatBinding {
        return FragmentMeTimeFormatBinding.inflate(inflater, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window.setGravity(Gravity.BOTTOM)
            window.setBackgroundDrawableResource(android.R.color.transparent)

        }
    }

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)


        val listTimeFormat = arrayOf("Follow The System", "24-Hour Time", "12-Hour Time")

        pickerWheel(
            view.findViewById(R.id.number_picker_time_format),
            0,
            listTimeFormat.size - 1,
            listTimeFormat
        )
    }

    private fun pickerWheel(
        picker: NumberPicker,
        min: Int,
        max: Int,
        list: Array<String>
    ) {
        picker.minValue = min
        picker.maxValue = max
        picker.displayedValues = list
        picker.wrapSelectorWheel = false
    }
}