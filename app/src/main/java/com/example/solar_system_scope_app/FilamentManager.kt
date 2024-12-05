package com.example.solar_system_scope_app

import android.content.Context
import android.util.Log
import android.view.Surface
import com.google.android.filament.Engine
import com.google.android.filament.Scene

object FilamentManager {
     var engine : Engine? = null
     var scene: Scene? = null
    var filamentHelper: FilamentHelper? = null
        private set

    fun initialize(context: Context, surface: Surface){
        if(filamentHelper == null){
            engine = Engine.create()
            scene = engine?.createScene()
            filamentHelper = FilamentHelper(context , surface,engine!!,scene!!)
            Log.d("FilamentManager", "FilamentHelper đã được khởi tạo.")
        }
    }

    fun destroy(){
        filamentHelper?.destroy()
        scene?.let {  engine?.destroyScene(it)}
        engine?.destroy()

        filamentHelper = null
        scene = null
        engine = null
        Log.d("FilamentManager", "FilamentHelper đã được hủy.")
    }
}