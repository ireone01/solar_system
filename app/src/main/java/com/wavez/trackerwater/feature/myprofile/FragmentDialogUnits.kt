package com.wavez.trackerwater.feature.myprofile

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.lingvo.base_common.ui.BaseDialogFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.FragmentMeUnitBinding

class FragmentDialogUnits : BaseDialogFragment<FragmentMeUnitBinding>() {
    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMeUnitBinding {
        return FragmentMeUnitBinding.inflate(inflater,container,false)
    }

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)


        val unitsList1 = arrayOf("US Oz","UK Oz", "ml","L")
        val unitsList2 = arrayOf("kg","lbs")

        val wheelpicker1 = view.findViewById<NumberPicker>(R.id.number_picker_unit1)
        val wheelpicker2 = view.findViewById<NumberPicker>(R.id.number_picker_unit2)

        pickerWheel(wheelpicker1,0,unitsList1.size -1,unitsList1)
        pickerWheel(wheelpicker2,0,unitsList2.size - 1,unitsList2)
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