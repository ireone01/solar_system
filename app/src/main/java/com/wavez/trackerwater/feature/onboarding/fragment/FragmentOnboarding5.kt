package com.wavez.trackerwater.feature.onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentOnboading5Binding

class FragmentOnboarding5 : BaseFragment<FragmentOnboading5Binding>() {

    private var currentWeight = 70

    private var isKg = true

    private val KG_TO_LB = 2.2f

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOnboading5Binding
        get() = FragmentOnboading5Binding::inflate

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)

        val unitList = arrayOf("Kg", "Lb")
        pickerWheel(binding.wheelPickerWeigh2, 0, unitList.size - 1, unitList)
        binding.wheelPickerWeigh2.value = 0


        setKgRange(currentWeight)
    }

    override fun initListener() {
        super.initListener()

    binding.wheelPickerWeigh.setOnValueChangedListener { picker, oldVal, newVal ->

            val displayedArray = picker.displayedValues
         currentWeight = displayedArray[newVal].toInt()
        }

        binding.wheelPickerWeigh2.setOnValueChangedListener { _, _, newVal ->
            if (newVal == 0) {
               if (!isKg) {
                   val newKg = (currentWeight / KG_TO_LB).toInt()
                    setKgRange(newKg)
                }
            } else {
                  if (isKg) {
                    val newLb = (currentWeight * KG_TO_LB).toInt()
                    setLbRange(newLb)
                }
            }
        }
    }

    private fun setKgRange(defaultKg: Int) {
        isKg = true
        val rangeKg = (30..100).toList()
        val arrayKg = rangeKg.map { it.toString() }.toTypedArray()


        pickerWheel(binding.wheelPickerWeigh, 0, arrayKg.size - 1, arrayKg)
        val clampedKg = defaultKg.coerceIn(30, 100) // chặn trong khoảng 30..100
        val indexOfKg = rangeKg.indexOf(clampedKg)
        binding.wheelPickerWeigh.value = indexOfKg

       currentWeight = clampedKg
    }


    private fun setLbRange(defaultLb: Int) {
        isKg = false

        val rangeLb = (60..230).toList()
        val arrayLb = rangeLb.map { it.toString() }.toTypedArray()

        pickerWheel(binding.wheelPickerWeigh, 0, arrayLb.size - 1, arrayLb)
        val clampedLb = defaultLb.coerceIn(60, 230)
        val indexOfLb = rangeLb.indexOf(clampedLb)
        binding.wheelPickerWeigh.value = indexOfLb

        currentWeight = clampedLb
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
