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

        // Thêm ánh sáng vào cảnh
        addDirectionalLight(engine, scene)
        addIndirectLightToScene(engine, scene)

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

        // Tải mô hình của hành tinh
        val buffer = readAsset(context, "${planet.name}.glb")
        val assetLoader = AssetLoader(engine, UbershaderProvider(engine), EntityManager.get())
        val asset = assetLoader.createAsset(ByteBuffer.wrap(buffer))
        if (asset == null) {
            Log.e("MiniFilamentHelper", "Không thể tạo asset cho hành tinh: ${planet.name}")
            return
        }

        // Tải tài nguyên cho asset
        val resourceLoader = ResourceLoader(engine)
        resourceLoader.loadResources(asset)
        resourceLoader.destroy()

        // Áp dụng vật liệu tùy chỉnh
        applyCustomMaterial(asset)

        // Thêm entities vào scene
        scene.addEntities(asset.entities)

        // Đặt camera nhìn vào mô hình
        frameModel(asset)
        Log.d("MiniFilamentHelper", "Số lượng Entity trong Scene: ${scene.entities.size}")
        // Giải phóng tài nguyên không cần thiết
        assetLoader.destroyAsset(asset)
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

    private fun applyCustomMaterial(asset: FilamentAsset) {
        // Tải vật liệu tùy chỉnh
        val bytes = readAsset(context, "materials/planet_material.filamat")
        if (bytes == null || bytes.isEmpty()) {
            Log.e("MiniFilamentHelperxxxex", "Không thể tải vật liệu tùy chỉnh")
            return
        }
        Log.i("MiniFilamentHelperxxxex", "Đang bắt đầu tải vật liệu tùy chỉnh")
        // Tạo ByteBuffer từ bytes
        val buffer = ByteBuffer.allocateDirect(bytes.size)
            .order(ByteOrder.nativeOrder())
            .put(bytes)
            .flip()

        // Tạo vật liệu từ buffer
        val material = Material.Builder()
            .payload(buffer, buffer.remaining())
            .build(engine)

        // Tạo MaterialInstance
        val materialInstance = material.createInstance()
        // Thiết lập tham số vật liệu (có thể điều chỉnh theo hành tinh)
        materialInstance.setParameter(
            "emissive",
            Colors.RgbaType.SRGB,
            1.0f, 1.0f, 1.0f, 1.0f // Màu phát sáng (trắng)
        )
        materialInstance.setParameter("emissiveIntensity", 100.0f)
        // Áp dụng vật liệu vào mô hình
        val renderableManager = engine.renderableManager
        asset.entities.forEach { entity ->
            if (renderableManager.hasComponent(entity)) {
                val renderableInstance = renderableManager.getInstance(entity)
                val primitiveCount = renderableManager.getPrimitiveCount(renderableInstance)
                for (i in 0 until primitiveCount) {
                    renderableManager.setMaterialInstanceAt(
                        renderableInstance,
                        i,
                        materialInstance
                    )
                }
            }
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
        val distance = radius / Math.tan(Math.toRadians(fov / 2.0))


        Log.d("CameraSetup", "Center: (${center[0]}, ${center[1]}, ${center[2]})")
        Log.d("CameraSetup", "HalfExtent: (${halfExtent[0]}, ${halfExtent[1]}, ${halfExtent[2]})")
        Log.d("CameraSetup", "Radius: $radius")
        Log.d("CameraSetup", "Distance: $distance")

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

    private fun addIndirectLightToScene(engine: Engine, scene: Scene) {
        val indirectLight = IndirectLight.Builder()
            .intensity(30_000.0f)
            .build(engine)
        scene.indirectLight = indirectLight
    }

    private fun addDirectionalLight(engine: Engine, scene: Scene) {
        val lightEntity = EntityManager.get().create()
        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(1.0f, 1.0f, 1.0f)
            .intensity(100_000.0f)
            .direction(-0.5f, -1.0f, -0.5f)
            .build(engine, lightEntity)
        scene.addEntity(lightEntity)
    }
}
