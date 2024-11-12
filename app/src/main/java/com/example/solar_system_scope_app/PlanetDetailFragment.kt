package com.example.solar_system_scope_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class PlanetDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_planet_detail , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val planetName = arguments?.getString("PLANET_NAME") ?: "Unknown Planet"

        val planetNameTextView: TextView = view.findViewById(R.id.planetNameTextView)
        planetNameTextView.text = planetName
    }
}