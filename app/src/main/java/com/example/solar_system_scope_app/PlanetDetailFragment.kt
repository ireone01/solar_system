package com.example.solar_system_scope_app

import android.nfc.Tag
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson

class PlanetDetailFragment : Fragment() {
    private var planetNameTextView: TextView? = null
    private lateinit var exploreButton: Button
    private lateinit var encycloediaButton: Button
    private lateinit var structureButton: Button
    private var planetId: String? = null
    private var planetName1: String? = null
    private var currentFragment: Fragment? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_planet_detail , container , false)

        exploreButton = view.findViewById(R.id.exploreButton)
        encycloediaButton = view.findViewById(R.id.encyclopediaButton)
        structureButton = view.findViewById(R.id.structureButton)



        exploreButton.setOnClickListener{
            replaceFragment(ExploreFragment() , "Thăm Quan")
        }
        encycloediaButton.setOnClickListener{
            val fragment = EncyclopediaFragment()
            val args = Bundle()
            args.putString("PLANET_NAME",planetName1)
            fragment.arguments = args
            replaceFragment(fragment , "Bách Khoa Toàn Thư")
        }

        structureButton.setOnClickListener{
            replaceFragment(StructureFragment(),"Cấu Trúc")
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
    }

    fun updatePlanetName(newName: String) {
        planetName1 = newName
        planetNameTextView?.text = newName

        val encyclopediaFragment = childFragmentManager.findFragmentByTag("Bách Khoa Toàn Thư") as? EncyclopediaFragment
        encyclopediaFragment?.updateData(newName)
    }
    private fun replaceFragment(fragment: Fragment, tag: String) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .addToBackStack(tag)
            .commit()
        currentFragment = fragment
    }


}