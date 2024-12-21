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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.encyclopedia_fragment,container , false)
        buttonBack = view.findViewById(R.id.btn_back)
        btnRead = view.findViewById(R.id.btn_speech)


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
                        append("Đường kính xích đạo : ${planet.encyclopedia.equatorial_diameter}")
                        append("\n")
                        append("Khối lượng: ${planet.encyclopedia.mass}")
                        append("\n")
                        append("Khoảng cách tới trung tâm thiên hà: ${planet.encyclopedia.distance_to_center}")
                        append("\n")
                        append("Chu kỳ tự quay: ${planet.encyclopedia.rotation_period}")
                        append("\n")
                        append("Chu kỳ quỹ đạo quanh thiên hà: ${planet.encyclopedia.orbit}")
                        append("\n")
                        append("Trọng lực bề mặt: ${planet.encyclopedia.gravity}")
                        append("\n")
                        append("Nhiệt độ bề mặt: ${planet.encyclopedia.temperature}")
                        append("\n")
                        append("GIỚI THIỆU: ${planet.additional_info}")
                        append("\n")
                        append("CẤU TẠO: ${planet.structure}")
                        append("\n")
                        append("KHOẢNG CÁCH: ${planet.distance}")
                        append("\n")
                        append("TRONG DẢI NGÂN HÀ: ${planet.in_milky_way}")
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


