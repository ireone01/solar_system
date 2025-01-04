package com.example.solar_system_scope_app.model

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PlanetDataProvider {
    private var planets: List<PlanetDescription> = emptyList()
    private var planetsS : List<CelestialBodies> = emptyList()
    private var currentLanguage: String = "vi"

    fun setLanguage(context: Context,languageCode: String){
        val sharedPreferences = context.getSharedPreferences("app_preferences",Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("language",languageCode).apply()
        currentLanguage = languageCode
        loadPlanets(context)
        loadPlanetsStructure(context)
    }

    fun getLanguage(context:Context): String {
        val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("language","vi") ?: "vi"
    }

    fun loadPlanets(context: Context) {
        try {
            val jsonString = context.assets.open(
                if (getLanguage(context) == "vi") "Solar_System_Planet_Data.json"
                else "Solar_System_Planet_Data_en.json"
            )
                .bufferedReader()
                .use { it.readText() }
            val planetListType = object : TypeToken<List<PlanetDescription>>() {}.type
            planets = Gson().fromJson(jsonString, planetListType)
        }catch (e:Exception){
            Log.e("Solar_System_Planet_Data", "khong the open")
            e.printStackTrace()
        }
    }
    fun loadPlanetsStructure(context: Context){
        try{
        val jsonString = context.assets.open(
            if(getLanguage(context) == "vi"){ "cau_truc.json"}else{
                "cau_truc_en.json"
            }
        )
            .bufferedReader()
            .use { it.readText() }
        val planetListType = object : TypeToken<List<CelestialBodies>>() {}.type
        planetsS = Gson().fromJson(jsonString, planetListType)

        }catch (e: Exception){
            e.printStackTrace()
        }
    }


    fun getPlanetById(name: String): PlanetDescription? {
        return planets.find { it.name == name }
    }
    fun getPlanetByNameStruct(name: String): CelestialBodies? {
        return planetsS.find { it.name == name }
    }
}