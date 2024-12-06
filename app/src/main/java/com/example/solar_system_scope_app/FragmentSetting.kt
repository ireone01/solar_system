package com.example.solar_system_scope_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class FragmentSetting : Fragment(){
    private lateinit var btn_cancel :ImageButton
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting,container,false )
        btn_cancel = view.findViewById(R.id.btn_cancel)
        btn_cancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        return view


    }
}