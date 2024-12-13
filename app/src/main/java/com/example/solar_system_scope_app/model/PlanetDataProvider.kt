package com.example.solar_system_scope_app.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PlanetDataProvider {
    private var planets: List<PlanetDescription> = emptyList()
    private var planetsS : List<CelestialBodies> = emptyList()
    fun loadPlanets(context: Context){
        val jsonString = context.assets.open("Solar_System_Planet_Data.json")
            .bufferedReader()
            .use { it.readText() }
        val planetListType = object : TypeToken<List<PlanetDescription>>() {}.type
        planets = Gson().fromJson(jsonString, planetListType)
    }

    fun loadPlanetsStructure(context: Context){
        val jsonString = context.assets.open("cau_truc.json")
            .bufferedReader()
            .use { it.readText() }
        val planetListType = object : TypeToken<List<CelestialBodies>>() {}.type
        planetsS = Gson().fromJson(jsonString, planetListType)

        }


    fun getPlanetById(name: String): PlanetDescription? {
        return planets.find { it.name == name }
    }
    fun getPlanetByNameStruct(name: String): CelestialBodies? {
        return planetsS.find { it.name == name }
    }
}