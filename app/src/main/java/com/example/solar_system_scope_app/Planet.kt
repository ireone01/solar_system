package com.example.solar_system_scope_app

import com.google.android.filament.Engine
import com.google.android.filament.EntityManager
import com.google.android.filament.LightManager
import com.google.android.filament.Scene
import com.google.android.filament.gltfio.FilamentAsset

data class Planet(
    val name: String,
    val asset: FilamentAsset,
    var angle: Float,
    val orbitRadiusA: Float,
    val orbitRadiusB: Float,
    val eccentricity: Float,
    val orbitSpeed: Float,
    val scale: Float,
    val inclination: Float,
    val axisTilt: Float,
    var rotation: Float = 0.0f,
    var rotationSpeed: Float = 1.0f,
    val parent: Planet? = null
) {
    fun getPosition(): FloatArray {
        // Tính toán vị trí của hành tinh trong không gian 3D
        val radianAngle = Math.toRadians(angle.toDouble())

        // Tính toán vị trí trong không gian 3D
        val x = (orbitRadiusA * Math.cos(radianAngle)).toFloat()
        val z = (orbitRadiusA * Math.sin(radianAngle)).toFloat()
        val y = 0f // Có thể cập nhật nếu cần thiết với độ nghiêng

        // Nếu có hành tinh mẹ, cộng thêm vị trí của hành tinh mẹ
        return if (parent != null) {
            val parentPosition = parent.getPosition()
            floatArrayOf(x + parentPosition[0], y + parentPosition[1], z + parentPosition[2])
        } else {
            floatArrayOf(x, y, z)
        }
    }

    fun updateOrbit(deltaTime: Float) {
        // Cập nhật góc quỹ đạo dựa trên tốc độ quỹ đạo và thời gian
        angle = (angle + orbitSpeed * deltaTime) % 360
    }



}
