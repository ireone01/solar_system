package com.wavez.trackerwater.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentOnboading2Binding
import com.wavez.trackerwater.databinding.WheelPickerPair2Binding

class FragmentWheelPickerPair: BaseFragment<WheelPickerPair2Binding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> WheelPickerPair2Binding
        get() = WheelPickerPair2Binding::inflate

    companion object {
        private const val POSITION ="position"

        fun newInstance(
            position: Int ,
            listener : OnTimeSelectedListener,

        ): FragmentWheelPickerPair {
            val fragment = FragmentWheelPickerPair()
            fragment.timeSelectedListener = listener
            val args = Bundle()
            args.putInt(POSITION , position)
            fragment.arguments = args
            return fragment
        }
    }
    private var timeSelectedListener: OnTimeSelectedListener? = null
    private var position : Int = 0
    private lateinit var pickerHour: NumberPicker
    private lateinit var pickerMinute: NumberPicker
    private lateinit var pickerAmPm: NumberPicker

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        position = arguments?.getInt(POSITION) ?: 0
        pickerHour =binding.wheelPickerWeigh
        pickerMinute = binding.wheelPickerWeigh2
        pickerAmPm = binding.wheelPickerWeigh3

        initPickers()
        initListener()
    }

    private fun initPickers() {
        pickerHour.minValue = 1
        pickerHour.maxValue = 12
        pickerHour.value = 9

        pickerMinute.minValue = 0
        pickerMinute.maxValue = 59
        pickerMinute.value = 11

        val amPmValues = arrayOf("AM", "PM")
        pickerAmPm.minValue = 0
        pickerAmPm.maxValue = amPmValues.size - 1
        pickerAmPm.displayedValues = amPmValues
        pickerAmPm.value = 0
    }

    override fun initListener() {
        super.initListener()
        val onValueChangeListener = NumberPicker.OnValueChangeListener { _, _, _ ->
            val hour = pickerHour.value
            val minute = pickerMinute.value
            val amPmIndex = pickerAmPm.value
            val amPmText = if (amPmIndex == 0) "AM" else "PM"

            val timeString = String.format("%02d:%02d %s", hour, minute, amPmText)

            timeSelectedListener?.onTimeSelected(position, timeString)
        }

        pickerHour.setOnValueChangedListener(onValueChangeListener)
        pickerMinute.setOnValueChangedListener(onValueChangeListener)
        pickerAmPm.setOnValueChangedListener(onValueChangeListener)
    }
}