package com.wavez.trackerwater.feature.myprofile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.databinding.FragmentMeWeightBinding

class FragmentDIalogWeight : BaseFragment<FragmentMeWeightBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMeWeightBinding
        get() = FragmentMeWeightBinding::inflate
    private var currentWeightKg  = 70
    private var currentWeightG = 0
    private var isKg : Boolean = true
    private val KG_TO_LB = 2.2f
    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)

        val weightType = arrayOf("Kg","Lb")
        pickerWheel(binding.numberPickerWeightType,0,weightType.size -1,weightType)

        val gList = (0..9).toList()
        val arrayg = gList.map{"."+it.toString()}.toTypedArray()
        pickerWheel(binding.numberPickerWeightG,0,arrayg.size-1,arrayg)
        setKgRange(currentWeightKg)
    }

    override fun initListener() {
        super.initListener()

        binding.numberPickerWeightKg.setOnValueChangedListener { picker, oldVal, newVal ->
            val displatedArray = picker.displayedValues
            currentWeightKg = displatedArray[newVal].toInt()
        }
        binding.numberPickerWeightG.setOnValueChangedListener { numberPicker, i, i2 ->
            val displlatedArray = numberPicker.displayedValues
            currentWeightG = displlatedArray[i2][1].toInt() - 48

        }

        binding.numberPickerWeightType.setOnValueChangedListener { picker, i, newVal ->
            if(newVal == 0){
                if(!isKg){
                    var newKg = ((currentWeightKg + currentWeightG.toFloat()/10.0) / KG_TO_LB).toFloat()
                    val newG = ((newKg - newKg.toInt())*10.0).toInt()
                    Log.e("newGnewKg", "newG  = ${(newKg - newKg.toInt())*10.0} , newKg =${newKg}")
                    binding.numberPickerWeightG.value = newG
                    currentWeightG = newG
                    setKgRange(newKg.toInt())
                }
            }else{
                if(isKg){
                    Log.e("newGnewKg", "currentWeightKg  = ${currentWeightKg} , currentWeightG =${currentWeightG}")
                    var newLb = ((currentWeightKg + currentWeightG.toFloat()/10.0) *KG_TO_LB).toFloat()
                    val newG = ((newLb - newLb.toInt())*10.0).toInt()
                    currentWeightG = newG
                    Log.e("newGnewKg", "newG  = ${(newLb - newLb.toInt())*10.0} , newKg =${newLb}")
                    binding.numberPickerWeightG.value = newG
                    setLbRange(newLb.toInt())
                }
            }
        }



    }



    private fun setKgRange(defaultKg:Int){
        isKg = true
        val rangeKg = (30..100).toList()
        val arrayKg = rangeKg.map { it.toString() }.toTypedArray()

        pickerWheel(binding.numberPickerWeightKg,0,arrayKg.size -1 , arrayKg)
        val clampedKg = defaultKg.coerceIn(30,100)
        val indexOfKg = rangeKg.indexOf(clampedKg)
        binding.numberPickerWeightKg.value = indexOfKg

        currentWeightKg = clampedKg

    }

    private fun setLbRange(defaultLb: Int) {
        isKg = false

        val rangeLb = (60..230).toList()
        val arrayLb = rangeLb.map { it.toString() }.toTypedArray()

        pickerWheel(binding.numberPickerWeightKg, 0, arrayLb.size - 1, arrayLb)
        val clampedLb = defaultLb.coerceIn(60, 230)
        val indexOfLb = rangeLb.indexOf(clampedLb)
        binding.numberPickerWeightKg.value = indexOfLb

        currentWeightKg = clampedLb
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