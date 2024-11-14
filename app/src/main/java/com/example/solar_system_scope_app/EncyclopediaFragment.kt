package com.example.solar_system_scope_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment

class EncyclopediaFragment : Fragment() {
    private lateinit var buttonBack :AppCompatImageButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.encyclopedia_fragment,container , false)
        buttonBack = view.findViewById(R.id.btn_back)


        buttonBack.setOnClickListener{
            parentFragmentManager.popBackStack()
        }


        return view
    }
}


