package com.example.solar_system_scope_app

import com.google.android.filament.gltfio.FilamentAsset


data class Planet(
    val name: String,  //Tên của hành tinh.
    val asset: FilamentAsset, // lưu trữ dữ liệu đồ họa
    val entity: Int, // mã định danh của hành tinh trong Filament Engine.
    var angle: Float,//Góc quỹ đạo của hành tinh
    val orbitRadiusA: Float,  // Bán kính trục lớn của quỹ đạo
    val orbitRadiusB: Float, //Bán kính trục nhỏ của quỹ đạo
    val eccentricity: Float,//Độ lệch tâm của quỹ đạo.
    var orbitSpeed: Float,//  Tốc độ di chuyển của hành tinh trên quỹ đạo.
    var scale: Float,// Tỷ lệ kích thước của hành tinh.
    val inclination: Float,// Độ nghiêng của mặt phẳng quỹ đạo, tính bằng độ.
    val axisTilt: Float,// Độ nghiêng của trục hành tinh, tính bằng độ.
    var rotation: Float,//  Góc tự quay của hành tinh quanh trục của nó.
    var rotationSpeed: Float = 1.0f, // Tốc độ tự quay của hành tinh quanh trục.
    val parent: Planet? = null,// Hành tinh mẹ của hành tinh này (nếu có).
    var dirtyFlag: Boolean = false,// có sự thay đổi cần cập nhật
    var transformMatrix : FloatArray= FloatArray(16),// lưu trữ các phép biến đổi của hành tinh trong không gian 3D
) {

    fun getPosition(): FloatArray {
        angle += orbitSpeed
        rotation += rotationSpeed
        val angleInRadians = Math.toRadians(angle.toDouble())
        val x: Float
        val z: Float
        val y = 0.0f
        if (parent == null) {
            val c = orbitRadiusA * eccentricity
            x = (orbitRadiusA * Math.cos(angleInRadians) - c).toFloat()
            z = (orbitRadiusB * Math.sin(angleInRadians)).toFloat()
        } else {
            x = (orbitRadiusA * Math.cos(angleInRadians)).toFloat()
            z = (orbitRadiusB * Math.sin(angleInRadians)).toFloat()
        }
        return floatArrayOf(x,y,z)
    }

}

data class PlanetDescription(
    val name: String,
    val id: String,
    val description: String,
    val encyclopedia: Encyclopedia,
    val additional_info: String,
    val structure : String,
    val distance : String,
    val in_milky_way : String
)

data class Encyclopedia(
    val equatorial_diameter: String,
    val mass: String,
    val distance_to_center: String,
    val rotation_period: String,
    val orbit: String,
    val gravity: String,
    val temperature: String
)


