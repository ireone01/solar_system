package com.example.solar_system_scope_app

interface PlanetSelectionListener {
    fun onPlanetSelected(planetName:String)
    fun onPlanetDataChanged(planetName: String)

}