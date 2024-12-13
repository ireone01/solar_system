package com.example.solar_system_scope_app.UI.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.solar_system_scope_app.Effectmanager
import com.example.solar_system_scope_app.FilamentHelper
import com.example.solar_system_scope_app.FilamentView
import com.example.solar_system_scope_app.R
import com.example.solar_system_scope_app.UI.activity.MainActivity

class PlanetDetailFragment : Fragment() {
    private var planetNameTextView: TextView? = null
    private lateinit var exploreButton: Button
    private lateinit var encycloediaButton: Button
    private lateinit var structureButton: Button
    private var planetId: String? = null
    private var planetName1: String? = null
    private var currentFragment: Fragment? = null
    private lateinit var btn_cancel : ImageButton


    private lateinit var filamentHelper: FilamentHelper
    private lateinit var effectmanager: Effectmanager

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_planet_detail, container , false)

        exploreButton = view.findViewById(R.id.exploreButton)
        encycloediaButton = view.findViewById(R.id.encyclopediaButton)
        structureButton = view.findViewById(R.id.structureButton)
        btn_cancel = view.findViewById(R.id.btn_cancel_detail)

        val filamentView = activity?.findViewById<FilamentView>(R.id.solarSystemView)
            ?: throw IllegalStateException("FIlamentview not found")
        filamentHelper = filamentView.getFilamentHelper()
            ?: throw  IllegalStateException("filament helper not found")


        btn_cancel.setOnClickListener {
            view.findViewById<View>(R.id.fragment_container).visibility = View.GONE
            (activity as? MainActivity)?.viewVisible()
            filamentView.switchProjection()
        }

        exploreButton.setOnClickListener{
            if(filamentView.filament!!.currentCameraOffsetX != 0.0f) {
                filamentView.switchProjection()
            }
            filamentHelper.targetPlanet = filamentHelper.planets.find { it.name == planetName1 }
            effectmanager.activateEffect()
            replaceFragment(ExploreFragment() , "Thăm Quan")

            // nen doi cai nay sang su dung voi viewmodel tranh lien ket truc tiep
            (activity as? MainActivity)?.viewVisible()
         }
        encycloediaButton.setOnClickListener{
            val fragment = EncyclopediaFragment()
            val args = Bundle()
            args.putString("PLANET_NAME",planetName1)
            fragment.arguments = args
            replaceFragment(fragment , "Bách Khoa Toàn Thư")
            if(filamentView.filament!!.currentCameraOffsetX == 0.0f) {
                filamentView.switchProjection()
            }
        }

        structureButton.setOnClickListener{
            val fragment = StructureFragment()
            val args = Bundle()
            args.putString("PLANET_NAME",planetName1)
            fragment.arguments = args
            replaceFragment(fragment,"Cấu Trúc")
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val planetName = arguments?.getString("PLANET_NAME") ?: "Unknown Planet"
        planetId = arguments?.getString("PLANET_ID")
        planetName1 = planetName
        planetNameTextView = view.findViewById(R.id.planetNameTextView)
        planetNameTextView?.text = planetName


        effectmanager = Effectmanager(filamentHelper)
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .addToBackStack(tag)
            .commit()
        currentFragment = fragment
    }


}