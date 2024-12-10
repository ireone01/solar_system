    package com.example.solar_system_scope_app

    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.appcompat.widget.AppCompatImageButton
    import androidx.fragment.app.Fragment
    import org.w3c.dom.Text

    class StructureFragment: Fragment() {
        private lateinit var buttonBack: AppCompatImageButton
        private var planetName: String? = null
        private var planetDB: CelestialBodies? = null

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_structure, container, false)
            buttonBack = view.findViewById(R.id.btn_back)
            buttonBack.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            planetName = arguments?.getString("PLANET_NAME")
            planetDB = PlanetDataProvider.getPlanetByNameStruct(planetName ?: "")
            view.findViewById<TextView>(R.id.title4).visibility = View.GONE
            view.findViewById<TextView>(R.id.in_milky_way).visibility = View.GONE
            planetDataStruct(view)

            return view
        }

        private fun planetDataStruct(view: View) {
            planetDB?.let {
                view.findViewById<TextView>(R.id.title).text = it.name.uppercase()
                view.findViewById<TextView>(R.id.subtitle).text = "Hành tinh"
                view.findViewById<TextView>(R.id.text_cau_truc).text = it.layers.mo_ta
                var descrip = "Vị trí và độ dày: " + it.layers.crust.posotion + "\n"+
                        "Thành phần: " + it.layers.crust.composition + "\n" +
                        "Đặc điểm: "+it.layers.crust.characteristics
                view.findViewById<TextView>(R.id.description).text = descrip
                descrip =  "Vị trí và độ dày: " + it.layers.mantle.posotion + "\n"+
                        "Thành phần: " + it.layers.mantle.composition + "\n" +
                        "Đặc điểm: "+it.layers.mantle.characteristics
                view.findViewById<TextView>(R.id.structure).text = descrip
                descrip =  "Vị trí và độ dày: " + it.layers.core.posotion + "\n"+
                        "Thành phần: " + it.layers.core.composition + "\n" +
                        "Đặc điểm: "+it.layers.core.characteristics
                view.findViewById<TextView>(R.id.distance).text = descrip
                if(planetName == "Jupiter" || planetName == "Saturn" ){
                    view.findViewById<TextView>(R.id.title4).visibility = View.VISIBLE
                    view.findViewById<TextView>(R.id.in_milky_way).visibility = View.VISIBLE
                    descrip =  "Vị trí và độ dày: " + it.layers.silicate_water_layer?.posotion + "\n"+
                            "Thành phần: " + it.layers.silicate_water_layer?.composition + "\n" +
                            "Đặc điểm: "+it.layers.silicate_water_layer?.characteristics
                    view.findViewById<TextView>(R.id.in_milky_way).text = descrip

                }else{
                    view.findViewById<TextView>(R.id.title4).visibility = View.GONE
                    view.findViewById<TextView>(R.id.in_milky_way).visibility = View.GONE
                }

            }

        }
    }