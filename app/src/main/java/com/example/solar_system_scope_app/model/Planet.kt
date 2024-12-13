package com.example.solar_system_scope_app.model

import com.google.android.filament.gltfio.FilamentAsset
import com.google.gson.annotations.SerializedName
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Date


data class Planet(
    val name: String,  //Tên của hành tinh.
    val asset: FilamentAsset, // lưu trữ dữ liệu đồ họa
    val entity: Int, // mã định danh của hành tinh trong Filament Engine.
    var angle: Float = 0.0f, // Góc quỹ đạo ban đầu của hành tinh.
    val orbitRadiusA: Float,  // Bán kính trục lớn của quỹ đạo
    val orbitRadiusB: Float, // Bán kính trục nhỏ của quỹ đạo
    val eccentricity: Float, // Độ lệch tâm của quỹ đạo.
    var orbitSpeed: Float, // Tốc độ di chuyển của hành tinh trên quỹ đạo.
    var scale: Float, // Tỷ lệ kích thước của hành tinh.
    val inclination: Float, // Độ nghiêng của mặt phẳng quỹ đạo, tính bằng độ.
    val axisTilt: Float, // Độ nghiêng của trục hành tinh, tính bằng độ.
    val rotation: Float, // Góc tự quay của hành tinh quanh trục của nó.
    var rotationSpeed: Float = 1.0f, // Tốc độ tự quay của hành tinh quanh trục.
    val parent: Planet? = null, // Hành tinh mẹ của hành tinh này (nếu có).
    var dirtyFlag: Boolean = false, // Có sự thay đổi cần cập nhật
    var transformMatrix: FloatArray = FloatArray(16), // Lưu trữ các phép biến đổi của hành tinh trong không gian 3D
    var tempAngle: Float = angle, // Góc quỹ đạo tạm thời
    var tempRotation: Float = rotation // Góc tự quay tạm thời
) {

    // Đặt mốc thời gian là 1/1/2000
    private val epochTime = "01/01/2000 00:00:00"
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")

    //thời gian đã trôi qua từ 1/1/2000 đến hiện tại
    private fun getElapsedTimeInSeconds(): Long {
        val startDate = dateFormatter.parse(epochTime)
        val currentDate = Date()
        val diffInMillis = currentDate.time - startDate.time
        return diffInMillis / 1000 // Số giây đã trôi qua
    }

    // Hàm tính toán góc quỹ đạo tại thời điểm hiện tại
    private fun calculateCurrentAngle(elapsedTimeInSeconds: Long): Float {
        // Công thức tính góc quỹ đạo (theo đơn vị giây)
        val angleChange = orbitSpeed * elapsedTimeInSeconds // Tính sự thay đổi góc
        val newAngle = (angle + angleChange) % 360f // Góc tại thời điểm hiện tại, lấy dư theo 360 độ
        return newAngle
    }

    // Hàm tính toán vị trí của hành tinh tại thời điểm hiện tại
    fun getPosition(): FloatArray {
        val elapsedTimeInSeconds = getElapsedTimeInSeconds()
        if(angle == 0.0f) {
            // Tính toán góc quỹ đạo tại thời điểm hiện tại
            tempAngle = calculateCurrentAngle(elapsedTimeInSeconds)
            angle = tempAngle
        }
        // Chuyển góc thành radians
        val angleInRadians = Math.toRadians(tempAngle.toDouble())


        val x: Float
        val z: Float
        val y = 0.0f

        val c = orbitRadiusA * eccentricity // Khoảng cách lệch tâm
        x = (orbitRadiusA * Math.cos(angleInRadians) - c).toFloat() // Tính x
        z = (orbitRadiusB * Math.sin(angleInRadians)).toFloat() // Tính z

        return floatArrayOf(x, y, z) // Trả về vị trí
    }

}

// can xem xet lai
data class PlanetInfo(
    val fileName: String,
    val name: String,
    val orbitRadiusA: Float,
    val eccentricity: Float,
    val orbitSpeed: Float,
    val scale: Float,
    val inclination: Float,
    val axisTilt: Float,
    val rotation: Float,
    val rotationSpeed: Float,
    val parentName: PlanetInfo? = null,
    val buffer: ByteBuffer
)



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

data class CelestialBodies(
    val id : String,
    val name : String,
    val layers : Layers

)

data class Layers(
    @SerializedName("mô_tả_cơ_bản")  val mo_ta : String,
    @SerializedName("hình_ảnh_mặt_cắt")  val core_img : String,
    @SerializedName("vỏ") val crust : Crust,
    @SerializedName("manti") val mantle : Mantle,
    @SerializedName("lõi") val core : Core,
    @SerializedName("lớp_silic_nước") val silicate_water_layer : Silicate?
)
data class Crust(
    @SerializedName("id") val id : String,
    @SerializedName("vị_trí_và_độ_dày") val posotion : String,
    @SerializedName("thành_phần") val composition : String,
    @SerializedName("đặc_điểm") val characteristics : String
)

data class Mantle(
    @SerializedName("id")  val id : String,
    @SerializedName("vị_trí_và_độ_dày") val posotion : String,
    @SerializedName("thành_phần") val composition : String,
    @SerializedName("đặc_điểm") val characteristics : String
)
data class Core(
    @SerializedName("id")  val id : String,
    @SerializedName("vị_trí_và_độ_dày") val posotion : String,
    @SerializedName("thành_phần") val composition : String,
    @SerializedName("đặc_điểm") val characteristics : String
)
data class Silicate(
    @SerializedName("id") var id : String ,
    @SerializedName("vị_trí_và_độ_dày") var posotion : String,
    @SerializedName("thành_phần") var composition : String,
    @SerializedName("đặc_điểm") var characteristics : String
)
