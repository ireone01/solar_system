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
    import com.example.solar_system_scope_app.model.CelestialBodies
    import com.example.solar_system_scope_app.model.PlanetDataProvider
    import java.util.Locale

    class StructureFragment: BaseFragment(){
        private lateinit var buttonBack: AppCompatImageButton
        private var planetName: String? = null
        private var planetDB: CelestialBodies? = null
        private var suB: String = ""
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_structure, container, false)
            buttonBack = view.findViewById(R.id.btn_back)
            btnRead = view.findViewById(R.id.btn_speech)


            buttonBack.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            planetName = arguments?.getString("PLANET_NAME")
            planetDB = PlanetDataProvider.getPlanetByNameStruct(planetName ?: "")
            view.findViewById<TextView>(R.id.title4).visibility = View.GONE
            view.findViewById<TextView>(R.id.in_milky_way).visibility = View.GONE
            planetDataStruct(view)

            tts = TextToSpeech(requireContext(), this)
            btnRead.setOnClickListener {
                planetDB?.let { planet ->
                    val textToRead = buildString {
                        append("Cấu Trúc : ${planet.layers.mo_ta}")
                        append("\n")
                        append(suB)
                    }
                    AudioManager.pause()
                    readText(textToRead)
                }
            }

            return view
        }


        private fun planetDataStruct(view: View) {
            planetDB?.let {
                suB=""
                view.findViewById<TextView>(R.id.title).text = it.name.uppercase()
                view.findViewById<TextView>(R.id.subtitle).text = "Hành tinh"
                view.findViewById<TextView>(R.id.text_cau_truc).text = it.layers.mo_ta
                var descrip =  it.layers.crust.posotion + "\n"+
                        it.layers.crust.composition + "\n" +
                       it.layers.crust.characteristics
                suB +="Phần Vỏ : " + descrip +"\n"
                view.findViewById<TextView>(R.id.description).text = descrip
                descrip = it.layers.mantle.posotion + "\n"+
                       it.layers.mantle.composition + "\n" +
                        it.layers.mantle.characteristics
                suB += "Lớp Manti : "+ descrip +"\n"
                view.findViewById<TextView>(R.id.structure).text = descrip
                descrip =  it.layers.core.posotion + "\n"+
                        it.layers.core.composition + "\n" +
                       it.layers.core.characteristics
                suB +=" Phần Lõi :" + descrip +"\n"
                view.findViewById<TextView>(R.id.distance).text = descrip
                if(planetName == "Jupiter" || planetName == "Saturn" ){
                    view.findViewById<TextView>(R.id.title4).visibility = View.VISIBLE
                    view.findViewById<TextView>(R.id.in_milky_way).visibility = View.VISIBLE
                    descrip = it.layers.silicate_water_layer?.posotion + "\n"+
                            it.layers.silicate_water_layer?.composition + "\n" +
                           it.layers.silicate_water_layer?.characteristics
                    view.findViewById<TextView>(R.id.in_milky_way).text = descrip
                    suB +="LỚP SILIC NƯỚC : " + descrip +"\n"

                }else{
                    view.findViewById<TextView>(R.id.title4).visibility = View.GONE
                    view.findViewById<TextView>(R.id.in_milky_way).visibility = View.GONE
                }

            }

        }

    }