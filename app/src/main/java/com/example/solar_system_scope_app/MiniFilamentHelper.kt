package com.example.solar_system_scope_app

import android.content.Context
import android.util.Log
import android.view.Choreographer
import android.view.Surface
import com.google.android.filament.*
import com.google.android.filament.gltfio.*
import com.google.android.filament.utils.Float3
import java.nio.ByteBuffer

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
            Log.d("MiniFilamentHelper", "doFrame called at time: $frameTimeNanos")
            if (renderer.beginFrame(swapChain!!, frametime)) {
                renderer.render(view)
                Log.d("MiniFilamentHelper", "Render thành công cho frame tại thời gian: $frametime")
                renderer.endFrame()
            }else{
                Log.e("MiniFilamentHelper", "Không thể bắt đầu frame")
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




        // Bắt đầu render loop
        choreographer.postFrameCallback(frameCallback)
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
        Log.d("MiniFilamentHelper", "Đang tải mô hình cho hành tinh: ${planet.name}")
        // Tải mô hình của hành tinh
        val buffer = readAsset(context, "${planet.name}.glb")
        val assetLoader = AssetLoader(engine, UbershaderProvider(engine), EntityManager.get())
        val asset = assetLoader.createAsset(ByteBuffer.wrap(buffer))
        if (asset == null) {
            Log.e("MiniFilamentHelper", "Không thể tạo asset cho hành tinh: ${planet.name}")
            return
        } else {
            Log.d("MiniFilamentHelper", "Asset được tạo thành công cho hành tinh: ${planet.name}")
        }

        val resourceLoader = ResourceLoader(engine)
        resourceLoader.loadResources(asset)
        Log.d("MiniFilamentHelper", "Đã tải tài nguyên cho asset")


        scene.addEntities(asset.entities)
        Log.d("MiniFilamentHelper", "Số lượng entities trong asset: ${asset.entities.size}")

        val boundingBox = asset.boundingBox
        Log.d("MiniFilamentHelper", "Bounding box của mô hình: ")
        val center = boundingBox.center
        val halfExtent = boundingBox.halfExtent
        val radius = Math.sqrt((halfExtent[0] * halfExtent[0] + halfExtent[1] * halfExtent[1] + halfExtent[2] * halfExtent[2]).toDouble())

        val fov = 45.0
        val distance = radius / Math.tan(Math.toRadians(fov/2.0))

        val up = Float3(0.0f , 1.0f,0.0f)
        // Đặt camera nhìn vào mô hình
        camera.lookAt(
            center[0].toDouble(), center[1].toDouble(), center[2].toDouble()+ distance.toDouble(),
            center[0].toDouble(), center[1].toDouble(), center[2].toDouble(),
            0.0, 1.0, 0.0)

        Log.d("MiniFilamentHelper", "Bán kính mô hình (radius): $radius")
        Log.d("MiniFilamentHelper", "Khoảng cách camera (distance): $distance")
        Log.d("MiniFilamentHelper", "Vị trí camera (eye): ${center[0].toDouble()}, ${center[1].toDouble()}, ${center[2].toDouble()+ distance.toDouble()}")

    }

    fun destroy() {
        choreographer.removeFrameCallback(frameCallback)
        engine.destroy()
    }
}
