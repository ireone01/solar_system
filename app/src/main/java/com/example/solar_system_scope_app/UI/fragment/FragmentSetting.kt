package com.example.solar_system_scope_app.UI.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Switch
import androidx.fragment.app.Fragment
import com.example.solar_system_scope_app.FilamentHelper
import com.example.solar_system_scope_app.FilamentManager
import com.example.solar_system_scope_app.R
import com.example.solar_system_scope_app.UI.activity.MainActivity
import com.example.solar_system_scope_app.model.PlanetDataProvider

class FragmentSetting : Fragment(){
    private lateinit var btn_cancel :ImageButton
    private lateinit var switch_QD : Switch
    private lateinit var switch_DS: Switch
    private lateinit var switch_TR: Switch
    private lateinit var seekBar1 : SeekBar
    private lateinit var seekBar2 : SeekBar

    private lateinit var filamentHelper: FilamentHelper
    private lateinit var sharedPreferences: SharedPreferences
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = requireContext().getSharedPreferences("switch_check", Context.MODE_PRIVATE)
        filamentHelper = FilamentManager.filamentHelper!!
        val view = inflater.inflate(R.layout.fragment_setting,container,false )
        btn_cancel = view.findViewById(R.id.btn_cancel)
        btn_cancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        switch_QD = view.findViewById(R.id.switch1)
        val isSwitch = sharedPreferences.getBoolean("SWITCH_QD",true)
        switch_QD.isChecked = isSwitch
        switch_QD.setOnCheckedChangeListener { _, ischecked ->
            sharedPreferences.edit().putBoolean("SWITCH_QD",ischecked).apply()
            if(ischecked){
                filamentHelper.let { filamentHelper ->
                    filamentHelper.setOrbitsVisible(true)
                }

            }else{
                filamentHelper.let { filamentHelper ->
                    filamentHelper.setOrbitsVisible(false)
                }
            }
        }


        switch_DS = view.findViewById(R.id.switch2)
        val isSwitchDS = sharedPreferences.getBoolean("SWITCH_DS", true)
        switch_DS.isChecked = isSwitchDS
        switch_DS.setOnCheckedChangeListener{ _ , ischecked ->
            sharedPreferences.edit().putBoolean("SWITCH_DS", ischecked).apply()
            (activity as? MainActivity)?.togglePlanetNamesVisibility(ischecked)
        }

        switch_TR = view.findViewById(R.id.switch3)
        val isSwitchTR = sharedPreferences.getBoolean("SWITCH_TR",false)
        switch_TR.isChecked = isSwitchTR
        switch_TR.setOnCheckedChangeListener{ _ , ischecked ->
            sharedPreferences.edit().putBoolean("SWITCH_TR",ischecked).apply()
            if(ischecked==true){
                PlanetDataProvider.setLanguage(requireContext(), "en")
            }else{
                PlanetDataProvider.setLanguage(requireContext(), "vn")
            }
        }
        seekBar1 = view.findViewById(R.id.seekBar1)
        seekBar2 = view.findViewById(R.id.seekBar2)


        seekBar1.max = 100
        val isSeekBar1 = sharedPreferences.getInt("Seek_Bar11",0)
        seekBar1.progress = isSeekBar1
        seekBar1.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val factor = seekBar1.progress/10f + 1f
                sharedPreferences.edit().putInt("Seek_Bar11",seekBar1.progress).apply()
                filamentHelper.setLightIntensity(factor)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        } )

        seekBar2.max = 100
        val isSeekBar2 = sharedPreferences.getInt("Seek_Bar22",0)
        seekBar2.progress = isSeekBar2
        seekBar2.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val factor = seekBar2.progress/10f + 1f
                sharedPreferences.edit().putInt("Seek_Bar22",seekBar2.progress).apply()
                filamentHelper.setLightSkyIntensity(factor)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        } )
        return view


    }
}