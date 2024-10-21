package com.example.solar_system_scope_app

import android.content.Context
import android.util.Log
import android.view.Choreographer
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.google.android.filament.Engine
import com.google.android.filament.EntityManager
import com.google.android.filament.LightManager
import com.google.ar.core.Earth

lateinit var earth812: Planet
lateinit var moon812: Planet
lateinit var sun812: Unit
lateinit var mecury812 : Planet
class FilamentView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private var filament: FilamentHelper? = null

    private val choreographer = Choreographer.getInstance()

    private var lastX = 0f
    private var lastY = 0f

    private var lastDistance = 0f
    private var isPinching = false

    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            Log.d("FilamentView", "doFrame called with frameTimeNanos: $frameTimeNanos")
            filament?.render()
            choreographer.postFrameCallback(this)
        }
    }

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d("FilamentView", "surfaceCreated called")
        filament = FilamentHelper(context, holder.surface)
        filament?.let {
           sun812 = it.loadGlb("sun.glb")
            it.loadBackground("sky_background.glb")

        mecury812 =    it.addPlanet(
                fileName = "mercury.glb",
                name = "Mercury",
                orbitRadiusA = 2.0f,
                eccentricity =  0.2056f,
                orbitSpeed = 0.5f,
                scale = 0.05f,
                inclination =  7.0f ,
                axisTilt = 0.0f,
                rotationSpeed =  1.0f
            )
          val venus = it.addPlanet(
                fileName = "venus.glb",         // Tên file mô hình của Sao Kim
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
                fileName = "earth.glb",          // Tên file mô hình của Trái Đất
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
                fileName = "moon.glb",
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

        }
        choreographer.postFrameCallback(frameCallback)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d("FilamentView", "surfaceChanged called: width=$width, height=$height")
        filament?.let {

            choreographer.removeFrameCallback(frameCallback)

            it.destroySwapChain()
            it.createSwapChain(holder.surface)
            it.resize(width, height)

            choreographer.postFrameCallback(frameCallback)
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d("FilamentView", "surfaceDestroyed called")
        choreographer.removeFrameCallback(frameCallback)
        filament?.destroy()
        filament = null
    }
    private fun calculateDistance(event: MotionEvent):Float {
        val dx = event.getX(1) - event.getX(0)
        val dy = event.getY(1) - event.getY(0)
        return Math.sqrt((dx * dx + dy * dy ).toDouble()).toFloat()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y

                isPinching = false
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                if(event.pointerCount == 2) {
                    lastDistance = calculateDistance(event)
                    isPinching = true
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if(isPinching && event.pointerCount == 2) {
                    val newDistance = calculateDistance(event)
                    val scaleFactor = newDistance / lastDistance

                    // Giới hạn scaleFactor để ngăn nhảy đột ngột
                    val minScaleFactor = 0.95f
                    val maxScaleFactor = 1.05f
                    val clampedScaleFactor = Math.max(minScaleFactor, Math.min(scaleFactor, maxScaleFactor))

                    filament?.zoomCamera(clampedScaleFactor)
                    lastDistance = newDistance
                } else if(!isPinching) {
                    val deltaX = event.x - lastX
                    val deltaY = event.y - lastY
                    filament?.rotateCamera(deltaX, deltaY)
                    lastX = event.x
                    lastY = event.y
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                isPinching = false
            }
        }
        return true
    }


}
