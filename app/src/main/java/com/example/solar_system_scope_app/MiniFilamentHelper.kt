package com.example.solar_system_scope_app

import android.content.Context
import android.util.Log
import android.view.Choreographer
import android.view.Surface
import com.google.android.filament.*
import com.google.android.filament.gltfio.*
import com.google.android.filament.utils.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MiniFilamentHelper(private val context: Context, private val surface: Surface) {
    private val engine = Engine.create()
    private var swapChain: SwapChain? = null
    private val renderer = engine.createRenderer()
    private val view = engine.createView()
    private val scene = engine.createScene()
    private val camera = engine.createCamera(EntityManager.get().create())
    private var width = 0
    private var height = 0
    private val choreographer = Choreographer.getInstance()



    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            val frametime = System.nanoTime()
            if (renderer.beginFrame(swapChain!!, frametime)) {
                renderer.render(view)
                renderer.endFrame()
            }
            choreographer.postFrameCallback(this)
        }
    }

    fun init(width: Int, height: Int) {
        swapChain = engine.createSwapChain(surface)
        view.scene = scene
        view.camera = camera
        this.width = width
        this.height = height
        view.viewport = Viewport(0, 0, width, height)
        camera.setProjection(45.0, width.toDouble() / height, 0.1, 1000.0, Camera.Fov.VERTICAL)

        // Thêm ánh sáng vào scene
        setupLighting()

        // Bắt đầu render loop
        choreographer.postFrameCallback(frameCallback)
    }

    private fun setupLighting() {
        // Tạo một directional light để chiếu sáng toàn bộ cảnh
        val directionalLight = EntityManager.get().create()

        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(1.0f, 1.0f, 1.0f)  // Màu trắng cho ánh sáng
            .intensity(200_000.0f)    // Tăng độ sáng để chiếu sáng tốt hơn
            .direction(-0.5f, -1.0f, -0.5f)  // Điều chỉnh hướng của ánh sáng để chiếu sáng mô hình tốt hơn
            .castShadows(true)        // Cho phép đổ bóng nếu cần
            .build(engine, directionalLight)

        // Thêm đèn vào scene
        scene.addEntity(directionalLight)

        // Tạo thêm một point light để tạo thêm ánh sáng từ một điểm cụ thể
        val pointLight = EntityManager.get().create()

        LightManager.Builder(LightManager.Type.POINT)
            .color(1.0f, 1.0f, 1.0f)
            .intensity(100_000.0f)  // Tăng cường độ sáng của point light
            .position(0.0f, 2.0f, 4.0f) // Điều chỉnh vị trí của ánh sáng để chiếu sáng mô hình tốt hơn
            .falloff(50.0f)  // Tăng falloff để mở rộng phạm vi chiếu sáng
            .build(engine, pointLight)

        // Thêm point light vào scene
        scene.addEntity(pointLight)
    }

    private fun readAsset(context: Context, fileName: String): ByteArray {
        context.assets.open(fileName).use { input ->
            return input.readBytes()
        }
    }

    fun loadPlanetModel(planet: Planet) {
        // Xóa scene hiện tại
        val entities = scene.entities
        scene.removeEntities(entities)
        entities.forEach { entity ->
            engine.destroyEntity(entity)
        }


        // Tải mô hình của hành tinh
        val buffer = readAsset(context, "${planet.name}.glb")

      val materialProvider = UbershaderProvider(engine)
        // Tạo và tải asset
        val assetLoader = AssetLoader(engine, materialProvider, EntityManager.get())
        val asset = assetLoader.createAsset(ByteBuffer.wrap(buffer))

        val resourceLoader = ResourceLoader(engine)


        // Thêm entities vào scene
        scene.addEntities(asset!!.entities)
        Log.d("MiniFilamentHelper", "Số lượng Entity trong Scene: ${scene.entities.size}")

        // Đặt camera nhìn vào mô hình
        frameModel(asset)
    }

    fun clearPlanetModel() {
        // Kiểm tra xem scene có entities không
        val entities = scene.entities
        if (entities.isNotEmpty()) {
            // Xóa các entities khỏi scene
            scene.removeEntities(entities)

            // Giải phóng các entities khỏi engine
            entities.forEach { entity ->
                engine.destroyEntity(entity)
            }
            Log.d("MiniFilamentHelper", "Đã xóa tất cả các mô hình khỏi scene")
        } else {
            Log.d("MiniFilamentHelper", "Không có mô hình nào để xóa")
        }
    }

    private fun frameModel(asset: FilamentAsset) {
        val boundingBox = asset.boundingBox
        val center = boundingBox.center
        val halfExtent = boundingBox.halfExtent
        val radius = Math.sqrt(
            (halfExtent[0] * halfExtent[0] +
                    halfExtent[1] * halfExtent[1] +
                    halfExtent[2] * halfExtent[2]).toDouble()
        )

        val fov = 45.0
        val distance = radius / Math.tan(Math.toRadians(fov / 2.0)) + 2.0 // Tăng khoảng cách để tránh clipping

        Log.d("CameraSetup", "Center: (${center[0]}, ${center[1]}, ${center[2]})")
        Log.d("CameraSetup", "HalfExtent: (${halfExtent[0]}, ${halfExtent[1]}, ${halfExtent[2]})")
        Log.d("CameraSetup", "Bán kính mô hình (radius): $radius")
        Log.d("CameraSetup", "Khoảng cách camera (distance): $distance")
        Log.d("CameraSetup", "Vị trí camera (eye): ${center[0].toDouble()}, ${center[1].toDouble()}, ${center[2].toDouble() + distance}")

        // Đặt camera nhìn vào mô hình
        camera.lookAt(
            center[0].toDouble(),
            center[1].toDouble(),
            center[2].toDouble() + distance,
            center[0].toDouble(),
            center[1].toDouble(),
            center[2].toDouble(),
            0.0,
            1.0,
            0.0
        )
    }

    fun destroy() {
        choreographer.removeFrameCallback(frameCallback)
        engine.destroy()
    }
}
