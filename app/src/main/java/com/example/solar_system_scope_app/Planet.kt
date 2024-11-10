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
    val orbitSpeed: Float,//  Tốc độ di chuyển của hành tinh trên quỹ đạo.
    val scale: Float,// Tỷ lệ kích thước của hành tinh.
    val inclination: Float,// Độ nghiêng của mặt phẳng quỹ đạo, tính bằng độ.
    val axisTilt: Float,// Độ nghiêng của trục hành tinh, tính bằng độ.
    var rotation: Float = 0.0f,//  Góc tự quay của hành tinh quanh trục của nó.
    var rotationSpeed: Float = 1.0f , // Tốc độ tự quay của hành tinh quanh trục.
    val parent: Planet? = null,// Hành tinh mẹ của hành tinh này (nếu có).
    var dirtyFlag: Boolean = true,// có sự thay đổi cần cập nhật
    var transformMatrix : FloatArray= FloatArray(16)// lưu trữ các phép biến đổi của hành tinh trong không gian 3D.
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




}
