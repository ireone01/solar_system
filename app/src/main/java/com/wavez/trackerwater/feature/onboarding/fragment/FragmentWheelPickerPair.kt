package com.wavez.trackerwater.feature.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseFragment
import com.shawnlin.numberpicker.NumberPicker
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.WheelPickerPair2Binding
import com.wavez.trackerwater.feature.onboarding.MealType
import com.wavez.trackerwater.feature.onboarding.OnTimeSelectedListener

class FragmentWheelPickerPair: BaseFragment<WheelPickerPair2Binding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> WheelPickerPair2Binding
        get() = WheelPickerPair2Binding::inflate

    companion object {
        private const val POSITION ="position"
        private const val MEAL_TYPE ="meal_type"
        fun newInstance(
            position: Int,
            listener : OnTimeSelectedListener,
            mealType: MealType
        ): FragmentWheelPickerPair {
            val fragment = FragmentWheelPickerPair()
            fragment.timeSelectedListener = listener
            val args = Bundle()
            args.putInt(POSITION , position)
            args.putString(MEAL_TYPE, mealType.name)
            fragment.arguments = args
            return fragment
        }
    }
    private lateinit var mealType: MealType
    private var timeSelectedListener: OnTimeSelectedListener? = null
    private var position : Int = 0
    private lateinit var pickerHour: NumberPicker
    private lateinit var pickerMinute: NumberPicker
    private lateinit var pickerAmPm: NumberPicker
    private var dontEat :Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val typeString = arguments?.getString(MEAL_TYPE, MealType.BREAKFAST.name)
        mealType = MealType.valueOf(typeString!!)
    }

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        position = arguments?.getInt(POSITION) ?: 0
        pickerHour =binding.wheelPickerWeigh
        pickerMinute = binding.wheelPickerWeigh2
        pickerAmPm = binding.wheelPickerWeigh3

        val textEat = binding.textEat
        val btnEat = binding.btnEat
        when(mealType){
            MealType.BREAKFAST -> textEat.text = getString(R.string.dont_eat_breakfast)
            MealType.LUNCH -> textEat.text = getString(R.string.dont_eat_lunch)
            MealType.DINNER -> textEat.text = getString(R.string.dont_eat_dinner)
        }
        initPickers()
        initListener()
        btnEat.setOnCheckedChangeListener {_ ,isChecked ->
            dontEat = isChecked
            pickerHour.isEnabled = !isChecked
            pickerMinute.isEnabled = !isChecked
            pickerAmPm.isEnabled = !isChecked
            if(dontEat){
                timeSelectedListener?.onTimeSelected(position,"")
            }
        }
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