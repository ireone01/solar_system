package com.example.solar_system_scope_app.UI.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import com.example.solar_system_scope_app.R
import com.example.solar_system_scope_app.model.AudioManager
import com.example.solar_system_scope_app.model.PlanetDataProvider
import com.example.solar_system_scope_app.model.PlanetDescription
import java.util.Locale

class EncyclopediaFragment :BaseFragment() {
    private lateinit var buttonBack :AppCompatImageButton
    private var planetId: String? = null
    private var planetDB: PlanetDescription? = null
    private lateinit var tcl1 : TextView
    private lateinit var tcl2 : TextView
    private lateinit var tcl3 : TextView
    private lateinit var tcl4 : TextView
    private lateinit var tcl5 : TextView
    private lateinit var tcl6 : TextView
    private lateinit var tcl7 : TextView
    private lateinit var ttl1 : TextView
    private lateinit var ttl2 : TextView
    private lateinit var ttl3 : TextView
    private lateinit var ttl4 : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.encyclopedia_fragment,container , false)
        buttonBack = view.findViewById(R.id.btn_back)
        btnRead = view.findViewById(R.id.btn_speech)
        tcl1  = view.findViewById(R.id.TextColumn1)
        tcl2  = view.findViewById(R.id.TextColumn2)
        tcl3  = view.findViewById(R.id.TextColumn3)
        tcl4  = view.findViewById(R.id.TextColumn4)
        tcl5  = view.findViewById(R.id.TextColumn5)
        tcl6  = view.findViewById(R.id.TextColumn6)
        tcl7  = view.findViewById(R.id.TextColumn7)
        ttl1 = view.findViewById(R.id.title1)
        ttl2 = view.findViewById(R.id.title2)
        ttl3 = view.findViewById(R.id.title3)
        ttl4 = view.findViewById(R.id.title4)

        tcl1.text = getString(R.string.ency_frag_TC1)
        tcl2.text = getString(R.string.ency_frag_TC2)
        tcl3.text = getString(R.string.ency_frag_TC3)
        tcl4.text = getString(R.string.ency_frag_TC4)
        tcl5.text = getString(R.string.ency_frag_TC5)
        tcl6.text = getString(R.string.ency_frag_TC6)
        tcl7.text = getString(R.string.ency_frag_TC7)
        ttl1.text = getString(R.string.ency_frag_TL1)
        ttl2.text = getString(R.string.ency_frag_TL2)
        ttl3.text = getString(R.string.ency_frag_TL3)
        ttl4.text = getString(R.string.ency_frag_TL4)


        buttonBack.setOnClickListener{
            parentFragmentManager.popBackStack()
            if(tts.isSpeaking){
                tts.stop()
                AudioManager.start(requireContext())
            }
        }

        planetId = arguments?.getString("PLANET_NAME")
        planetDB = PlanetDataProvider.getPlanetById(planetId ?: "")

        displayPlanetData(view)

        tts = TextToSpeech(requireContext(), this)
        btnRead.setOnClickListener {
            if (tts.isSpeaking) {
                tts.stop()
                AudioManager.start(requireContext())
            } else {
                planetDB?.let { planet ->
                    val textToRead = buildString {
                        append(planet.name.uppercase())
                        append("\n")
                        append(planet.description)
                        append("\n")
                        append("${getString(R.string.ency_frag_TC1)} : ${planet.encyclopedia.equatorial_diameter}")
                        append("\n")
                        append("${getString(R.string.ency_frag_TC2)}: ${planet.encyclopedia.mass}")
                        append("\n")
                        append("${getString(R.string.ency_frag_TC3)}: ${planet.encyclopedia.distance_to_center}")
                        append("\n")
                        append("${getString(R.string.ency_frag_TC4)}: ${planet.encyclopedia.rotation_period}")
                        append("\n")
                        append("${getString(R.string.ency_frag_TC5)}: ${planet.encyclopedia.orbit}")
                        append("\n")
                        append("${getString(R.string.ency_frag_TC6)}: ${planet.encyclopedia.gravity}")
                        append("\n")
                        append("${getString(R.string.ency_frag_TC7)}: ${planet.encyclopedia.temperature}")
                        append("\n")
                        append("${getString(R.string.ency_frag_TL1)}: ${planet.additional_info}")
                        append("\n")
                        append("${getString(R.string.ency_frag_TL2)}: ${planet.structure}")
                        append("\n")
                        append("${getString(R.string.ency_frag_TL3)}: ${planet.distance}")
                        append("\n")
                        append("${getString(R.string.ency_frag_TL4)}: ${planet.in_milky_way}")
                    }
                    AudioManager.pause()
                     readText(textToRead)

                }

            }
        }

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


