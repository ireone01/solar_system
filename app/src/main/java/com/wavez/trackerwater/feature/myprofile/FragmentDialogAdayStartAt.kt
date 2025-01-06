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
import com.wavez.trackerwater.databinding.FragmentMeDayStartAtBinding

class FragmentDialogAdayStartAt : BaseDialogFragment<FragmentMeDayStartAtBinding>() {
    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMeDayStartAtBinding {
       return FragmentMeDayStartAtBinding.inflate(inflater,container,false)
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
        val dayStartList= arrayOf("00:00","01:00","02:00","03:00","04:00","05:00","06:00")
        val pickerNumber : NumberPicker = view.findViewById(R.id.number_picker_aday_start_at)
        pickerWheel(pickerNumber,0,dayStartList.size-1,dayStartList)
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