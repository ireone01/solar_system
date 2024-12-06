package com.example.solar_system_scope_app

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Switch
import androidx.fragment.app.Fragment

class FragmentSetting : Fragment(){
    private lateinit var btn_cancel :ImageButton
    private lateinit var switch_QD : Switch
    private lateinit var switch_DS: Switch
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
        return view


    }
}