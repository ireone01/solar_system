package com.example.solar_system_scope_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.filament.utils.Utils

class MainActivity : AppCompatActivity() {
    private lateinit var filamentView: FilamentView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.init()
        filamentView = FilamentView(this)
        setContentView(filamentView)
    }


}
