package com.example.solar_system_scope_app

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PlanetDataProvider.loadPlanets(this)
        PlanetDataProvider.loadPlanetsStructure(this)
    }
}