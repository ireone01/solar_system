package com.example.solar_system_scope_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment

class EncyclopediaFragment : Fragment() {
    private lateinit var buttonBack :AppCompatImageButton
    private var planetId: String? = null
    private var planetDB: PlanetDescription? = null
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

        planetId = arguments?.getString("PLANET_NAME")
        planetDB = PlanetDataProvider.getPlanetById(planetId ?: "")

        displayPlanetData(view)

        return view
    }

    private fun displayPlanetData(view : View){
        planetDB?.let {
            view.findViewById<TextView>(R.id.subtitle).text = it.description
            view.findViewById<TextView>(R.id.equatorial_diameter).text = it.encyclopedia.equatorial_diameter
            view.findViewById<TextView>(R.id.mass).text = it.encyclopedia.mass

            view.findViewById<TextView>(R.id.title).text = it.name.uppercase()
            view.findViewById<TextView>(R.id.distance_to_center).text = it.encyclopedia.distance_to_center
            view.findViewById<TextView>(R.id.rotation_period).text = it.encyclopedia.rotation_period
            view.findViewById<TextView>(R.id.orbit_period).text = it.encyclopedia.orbit
            view.findViewById<TextView>(R.id.surface_gravity).text = it.encyclopedia.gravity
            view.findViewById<TextView>(R.id.surface_temperature).text = it.encyclopedia.temperature
            view.findViewById<TextView>(R.id.description).text = it.additional_info
            view.findViewById<TextView>(R.id.structure).text = it.structure
            view.findViewById<TextView>(R.id.distance).text = it.distance
            view.findViewById<TextView>(R.id.in_milky_way).text = it.in_milky_way
        }
    }
}


