package com.example.solar_system_scope_app

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PlanetDataProvider {
    private var planets: List<PlanetDescription> = emptyList()

    fun loadPlanets(context: Context){
        val jsonString = context.assets.open("Solar_System_Planet_Data.json")
            .bufferedReader()
            .use { it.readText() }
        val planetListType = object : TypeToken<List<PlanetDescription>>() {}.type
        planets = Gson().fromJson(jsonString, planetListType)
    }

    fun getPlanetById(name: String): PlanetDescription? {
        return planets.find { it.name == name }
    }
}