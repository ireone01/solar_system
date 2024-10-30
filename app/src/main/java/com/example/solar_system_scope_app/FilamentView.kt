package com.example.solar_system_scope_app


import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Choreographer
import android.view.GestureDetector


import android.view.MotionEvent
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import com.google.android.filament.Engine
import com.google.android.filament.EntityManager
import com.google.android.filament.LightManager
import com.google.android.filament.Scene
import com.google.android.filament.utils.Utils
import org.w3c.dom.Text
import kotlin.math.pow
import kotlin.math.sqrt


lateinit var earth812: Planet
lateinit var moon812: Planet
lateinit var sun812: Planet
lateinit var mecury812 : Planet
lateinit var saturn812 : Planet
lateinit var mars812 : Planet
lateinit var jupiter812 : Planet
lateinit var uranus812 : Planet
lateinit var neptune812 : Planet
lateinit var venus812 : Planet
var targetPlanet: Planet? = null
// can sua may thang ghe phia tren
class FilamentView @JvmOverloads constructor(context: Context,
                                             attrs: AttributeSet? = null)
    : SurfaceView(context,attrs), SurfaceHolder.Callback {


    private var filament: FilamentHelper? = null


    private val choreographer = Choreographer.getInstance()




    private var infoPanel: View? = null
    private var planetNameTextView : TextView? = null


    private val engine = Engine.create()
    private val scene = engine.createScene()
    private lateinit var gestureDetector: GestureDetector
    private lateinit var gestureHandler: GestureHandler


    private var miniFilamentHelper: MiniFilamentHelper? = null

    companion object {
        init {
            System.loadLibrary("filament-jni")
            Utils.init()
        }
    }

    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            Log.d("FilamentView", "doFrame called with frameTimeNanos: $frameTimeNanos")
            filament?.render()
            choreographer.postFrameCallback(this)
        }
    }


    init {
        holder.addCallback(this)


        gestureDetector = GestureDetector(context, GestureListener())
    }


    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            e?.let {
                val x = it.x
                val y = it.y
                handleDoubleTap(x, y)
            }
            return super.onDoubleTap(e)
        }
    }




    fun setMiniFilamentHelper(helper: MiniFilamentHelper){
        this.miniFilamentHelper = helper
    }


    fun setInfoPanel(infoPanel: View , planetNameTextView: TextView){
        this.infoPanel = infoPanel
        this.planetNameTextView = planetNameTextView
    }


    private fun handleDoubleTap(x: Float, y: Float) {
        val planets = listOf(sun812, earth812, moon812, mecury812, saturn812, mars812, jupiter812, uranus812, neptune812, venus812)


        val clickedPlanet = planets.minByOrNull { planet ->
            val planetScreenPos = filament?.getScreenPosition(planet) ?: return@minByOrNull Float.MAX_VALUE
            val dx = planetScreenPos.x - x
            val dy = planetScreenPos.y - y
            sqrt(dx * dx + dy * dy)
        }


        clickedPlanet?.let { planet ->
            val planetScreenPos = filament?.getScreenPosition(planet)
            if (planetScreenPos != null) {
                val distance = sqrt((planetScreenPos.x - x).pow(2) + (planetScreenPos.y - y).pow(2))
                val touchThreshold = 100f


                if (distance < touchThreshold) {
                    targetPlanet = planet
                    filament?.updateCameraTransform()




                    post {
                        planetNameTextView?.text = planet.name
                        infoPanel?.visibility = View.VISIBLE


                        miniFilamentHelper?.loadPlanetModel(planet)
                    }
                }
            }
        }
    }








    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d("FilamentView", "surfaceCreated called")
//        val planets = listOf(sun812, earth812, moon812, mecury812, saturn812, mars812, jupiter812, uranus812, neptune812, venus812)


        filament = FilamentHelper(context, holder.surface)
        gestureHandler = GestureHandler(filament)
        filament?.let {
            sun812 = it.addPlanet(
                fileName = "Sun.glb",
                name = "Sun",
                orbitRadiusA = 0f,
                eccentricity =  0f,
                orbitSpeed = 0f,
                scale = 0.1f,
                inclination =  0f ,
                axisTilt = 0.0f,
                rotationSpeed =  0.4f
            )
            it.loadBackground("sky_background.glb")


            mecury812 =    it.addPlanet(
                fileName = "Mercury.glb",
                name = "Mercury",
                orbitRadiusA = 2.0f,
                eccentricity =  0.2056f,
                orbitSpeed = 0.5f,
                scale = 0.05f,
                inclination =  7.0f ,
                axisTilt = 0.0f,
                rotationSpeed =  1.0f
            )
            venus812 = it.addPlanet(
                fileName = "Venus.glb",         // Tên file mô hình của Sao Kim
                name = "Venus",                 // Tên hành tinh
                orbitRadiusA = 3.7f,            // Bán kính quỹ đạo của Sao Kim so với giá trị bạn dùng cho Sao Thủy (khoảng 0.723 AU so với 0.387 AU cho Sao Thủy)
                eccentricity = 0.0067f,         // Độ lệch tâm của Sao Kim, rất gần với hình tròn
                orbitSpeed = 0.35f,             // Tốc độ quỹ đạo của Sao Kim (quay quanh Mặt Trời mất khoảng 224,7 ngày Trái Đất)
                scale = 0.005f,                 // Kích thước Sao Kim, tương đối lớn hơn so với Sao Thủy
                inclination = 3.39f,            // Độ nghiêng quỹ đạo của Sao Kim so với mặt phẳng hoàng đạo
                axisTilt = 177.4f,              // Độ nghiêng trục quay của Sao Kim, gần như lật ngược (tự quay ngược)
                rotationSpeed = -1.48f          // Tốc độ tự quay của Sao Kim, rất chậm và quay ngược (một ngày Sao Kim dài khoảng 243 ngày Trái Đất)


            )
            earth812 = it.addPlanet(
                fileName = "Earth.glb",          // Tên file mô hình của Trái Đất
                name = "Earth",                  // Tên hành tinh
                orbitRadiusA = 5.0f,             // Bán kính quỹ đạo (k2hoảng 1 AU, tăng tỷ lệ so với Sao Thủy và Sao Kim)
                eccentricity = 0.0167f,          // Độ lệch tâm quỹ đạo của Trái Đất, gần tròn
                orbitSpeed = 0.3f,               // Tốc độ quỹ đạo (mất 365.25 ngày để hoàn thành một vòng quanh Mặt Trời)
                scale = 0.00525f,                    // Kích thước tương đối của Trái Đất
                inclination = 0.00005f,          // Độ nghiêng quỹ đạo của Trái Đất (rất nhỏ, gần như không nghiêng)
                axisTilt = 23.44f,               // Độ nghiêng trục quay của Trái Đất, tạo ra các mùa
                rotationSpeed = 1.0f,


                )
            moon812 = it.addPlanet(
                fileName = "Moon.glb",
                name = "Moon",
                orbitRadiusA = 130.9f, // Khoảng cách trung bình từ Mặt Trăng đến Trái Đất (đơn vị thiên văn)
                eccentricity = 0.0549f,
                orbitSpeed = 2.5f, // Tốc độ quay quanh Trái Đất của Mặt Trăng
                scale = 12f,
                inclination = 5.14f,
                axisTilt = 6.68f,
                rotationSpeed = 13.36f, // Tốc độ tự quay của Mặt Trăng
                parent = earth812
            )


            mars812 =    it.addPlanet(
                fileName = "Mars.glb",
                name = "Mars",
                orbitRadiusA = 7.6f,
                eccentricity = 0.0934f,         // Độ lệch tâm quỹ đạo của Sao Hỏa
                orbitSpeed = 0.33f,             // Tốc độ quay quanh Mặt Trời, tỷ lệ so với Trái Đất
                scale = 0.371f,                  // Tỷ lệ kích thước của Sao Hỏa so với Trái Đất
                inclination = 1.85f,            // Độ nghiêng của quỹ đạo so với mặt phẳng hoàng đạo
                axisTilt = 25.19f,              // Độ nghiêng của trục tự quay của Sao Hỏa
                rotationSpeed = 1.02f           // Tốc độ tự quay của Sao Hỏa (gần tương đương với Trái Đất)
            )


            jupiter812 =     it.addPlanet(
                fileName = "Jupiter.glb",
                name = "Jupiter",
                orbitRadiusA = 11f,    // 10.4f
                eccentricity = 0.049f,
                orbitSpeed = 2.5f*0.084f,
                scale = 0.1f,
                inclination = 1.31f,
                axisTilt = 3.13f,
                rotationSpeed = 2.41f           // Tốc độ tự quay nhanh (một ngày của Sao Mộc chỉ kéo dài khoảng 10 giờ)
            )


            saturn812 = it.addPlanet(
                fileName = "Saturn.glb",
                name = "Saturn",
                orbitRadiusA = 16f,    // 19.16f
                eccentricity = 0.056f,
                orbitSpeed = 2.5f*0.034f,
                scale = 3f,
                inclination = 2.49f,
                axisTilt = 26.73f,
                rotationSpeed = 2.24f           // Tốc độ tự quay nhanh (một ngày của Sao Thổ chỉ kéo dài khoảng 10.7 giờ)
            )
            uranus812 =   it.addPlanet(
                fileName = "Uranus.glb",
                name = "Uranus",
                orbitRadiusA = 19.22f,   // 38.44f
                eccentricity = 0.046f,
                orbitSpeed = 2.5f*0.012f,
                scale = 0.001f,
                inclination = 0.77f,
                axisTilt = 97.77f,
                rotationSpeed = 1.41f
            )
            neptune812 = it.addPlanet(
                fileName = "Neptune.glb",
                name = "Neptune",
                orbitRadiusA = 22f,
                eccentricity = 0.010f,
                orbitSpeed = 2.5f*0.006f,
                scale = 0.007f,
                inclination = 1.77f,
                axisTilt = 28.32f,
                rotationSpeed = 1.48f           // Tốc độ tự quay (một ngày của Sao Hải Vương kéo dài khoảng 16 giờ)
            )
        }
//        initPlanetLights(planets, engine, scene)
        choreographer.postFrameCallback(frameCallback)
    }
    //    fun initPlanetLights(planets: List<Planet>, engine: Engine, scene: Scene) {
//        planets.forEach { planet ->
//            val lightEntity = EntityManager.get().create()
//            LightManager.Builder(LightManager.Type.POINT)
//                .color(1.0f, 1.0f, 0.9f) // Màu ánh sáng
//                .intensity(50000f)         // Điều chỉnh cường độ để vừa đủ sáng
//                .falloff(50.0f)           // Điều chỉnh độ rơi của ánh sáng
//                .build(engine, lightEntity)
//            scene.addEntity(lightEntity)
//        }
//    }
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d("FilamentView", "surfaceChanged called: width=$width, height=$height")
        filament?.resize(width,height)
        filament?.let {


            choreographer.removeFrameCallback(frameCallback)


            it.destroySwapChain()
            it.createSwapChain(holder.surface)
            it.resize(width, height)
            it.updateScreenSize(width, height)
            choreographer.postFrameCallback(frameCallback)
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d("FilamentView", "surfaceDestroyed called")
        choreographer.removeFrameCallback(frameCallback)
        filament?.destroy()
        filament = null
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        gestureHandler.handleTouch(event)
        return true
    }
}