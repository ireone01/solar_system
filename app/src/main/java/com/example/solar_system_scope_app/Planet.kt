package com.example.solar_system_scope_app

import com.google.android.filament.gltfio.FilamentAsset

data class Planet(
    val name: String ,
    val asset: FilamentAsset,
    var angle: Float,
    val orbitRadiusA: Float ,
    val orbitRadiusB: Float,
    val eccentricity:Float,
    val orbitSpeed:Float ,
    val scale: Float
)