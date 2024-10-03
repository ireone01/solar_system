package com.example.solar_system_scope_app

import android.content.Context
import android.opengl.Matrix
import android.util.Log
import android.view.Surface
import androidx.core.graphics.rotationMatrix
import com.google.android.filament.*
import com.google.android.filament.gltfio.*
import com.google.android.filament.utils.scale
import java.nio.ByteBuffer


class FilamentHelper(private val context: Context, private var surface: Surface) {

    private val engine: Engine = Engine.create()
    private var swapChain: SwapChain = engine.createSwapChain(surface)
    private val renderer: Renderer = engine.createRenderer()
    private val scene: Scene = engine.createScene()
    private val view: View = engine.createView()
    private val camera: Camera
    private val cameraEntity: Int
    private val materialProvider: MaterialProvider

    private val assetLoader: AssetLoader
    private val resourceLoader: ResourceLoader
    private val entityManager: EntityManager = EntityManager.get()
    private var asset: FilamentAsset? = null
    private var totalScale = 1.0f

    private var rotationX = 0.0f
    private var rotationY = 0.0f

    init {
        Log.d("FilamentHelper", "Khởi tạo FilamentHelper")
        Log.d("FilamentHelper", "Engine created: $engine")
        Log.d("FilamentHelper", "Renderer created: $renderer")

        cameraEntity = entityManager.create()
        camera = engine.createCamera(cameraEntity)

        camera.lookAt(
            0.0, 0.0, 10.0,  // Vị trí camera (lùi ra xa hơn)
            0.0, 0.0, 0.0,   // Nhìn vào gốc tọa độ
            0.0, 1.0, 0.0    // Hướng lên trên
        )

        view.scene = scene
        view.camera = camera

        camera.setProjection(45.0, 16.0 / 9.0, 0.1, 100.0, Camera.Fov.VERTICAL)
        camera.lookAt(
            0.0, 0.0, 3.0,  // Vị trí camera
            0.0, 0.0, 0.0,  // Nhìn vào đâu
            0.0, 1.0, 0.0   // Hướng lên trên
        )

        materialProvider = UbershaderProvider(engine)
        assetLoader = AssetLoader(engine, materialProvider, entityManager)
        resourceLoader = ResourceLoader(engine)


        val lightEntity = EntityManager.get().create()

        // Thiết lập ánh sáng định hướng
        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(1.0f, 1.0f, 1.0f)        // Màu trắng
            .intensity(400_000.0f)          // Độ sáng
            .direction(0.0f, -1.0f, -1.0f)  // Hướng ánh sáng (từ trên xuống và về phía camera)
            .castShadows(true)
            .build(engine, lightEntity)


        // Thêm ánh sáng vào scene
        scene.addEntity(lightEntity)

    }

    fun loadGlb(fileName: String) {
        Log.d("FilamentHelper", "Bắt đầu load GLB: $fileName")
        val buffer = readAsset(context, fileName)
        Log.d("FilamentHelper", "Đã đọc tệp GLB, kích thước: ${buffer.size} bytes")
        asset = assetLoader.createAsset(ByteBuffer.wrap(buffer))

        if (asset == null) {
            Log.e("FilamentHelper", "Không thể tạo asset từ tệp GLB")
            throw IllegalStateException("Không thể tải mô hình GLTF.")
        } else {
            Log.d("FilamentHelper", "Đã tạo asset thành công")
        }

        resourceLoader.loadResources(asset!!)
        Log.d("FilamentHelper", "Đã tải tài nguyên cho asset")
        scene.addEntities(asset!!.entities)
        Log.d("FilamentHelper", "Đã thêm entities vào scene")


        val transformManager = engine.transformManager
        val rootEntity = asset!!.root

        val instance = transformManager.getInstance(rootEntity)
        if (instance != 0) {
            val transformMatrix = FloatArray(16)
            Matrix.setIdentityM(transformMatrix, 0)
            Matrix.rotateM(transformMatrix , 0 , 78.0f, 0.0f,1.0f,0.0f)
            Matrix.scaleM(transformMatrix, 0, 0.1f, 0.1f, 0.1f)  // Giảm kích thước xuống 50%
            transformManager.setTransform(instance, transformMatrix)
        }
    }

    fun resize(width: Int, height: Int) {
        Log.d("FilamentHelper", "resize: width=$width, height=$height")
        view.viewport = Viewport(0, 0, width, height)
        val aspect = width.toDouble() / height
        camera.setProjection(45.0, aspect, 0.1, 1000.0, Camera.Fov.VERTICAL)
    }

    fun render() {
        if(swapChain == null){
            Log.e("FilamentHelper", "SwapChain is null, cannot render.")
            return
        }

        try {
            var frametime = System.nanoTime()
            Log.d("FilamentHelper", "Bắt đầu render, frameTimeNanos: $frametime")
            if (renderer.beginFrame(swapChain, frametime)) {
                Log.d("FilamentHelper", "beginFrame thành công")
                renderer.render(view)
                Log.d("FilamentHelper", "render view thành công")
                renderer.endFrame()
                Log.d("FilamentHelper", "endFrame thành công")
            } else {
                Log.e("FilamentHelper", "beginFrame thất bại")
            }
        } catch (e: Exception) {
            Log.e("FilamentHelper", "Lỗi trong render: ${e.message}", e)
        }
    }

    fun destroy() {
        Log.d("FilamentHelper", "Destroying FilamentHelper")
        // Giải phóng tài nguyên
        if (asset != null) {
            assetLoader.destroyAsset(asset!!)
            asset = null
        }
        assetLoader.destroy()
        resourceLoader.destroy()

        engine.destroyRenderer(renderer)
        engine.destroyView(view)
        engine.destroyScene(scene)
        engine.destroyCameraComponent(cameraEntity)
        engine.destroySwapChain(swapChain)
        engine.destroy()
    }
    fun destroySwapChain() {
        Log.d("FilamentHelper", "Destroying SwapChain")
        engine.destroySwapChain(swapChain)
    }

    fun createSwapChain(surface: Surface) {
        Log.d("FilamentHelper", "Creating SwapChain")
        this.surface = surface
        swapChain = engine.createSwapChain(surface)
    }

    private fun readAsset(context: Context, fileName: String): ByteArray {
        context.assets.open(fileName).use { input ->
            return input.readBytes()
        }
    }

    private fun updateModelTransform() {
        val transformManager =engine.transformManager
        val rootEntity = asset!!.root
        val instance = transformManager.getInstance(rootEntity)

        if(instance!= 0 ){
            val transformMatrix = FloatArray(16)
            Matrix.setIdentityM(transformMatrix , 0 )

            val rotationMatrixX  = FloatArray(16)
            Matrix.setRotateM(rotationMatrixX , 0 , rotationX , 1.0f , 0.0f , 0.0f )

            val rotationMatrixY = FloatArray(16)
            Matrix.setRotateM(rotationMatrixY, 0, rotationY, 0.0f, 1.0f, 0.0f)

            // Kết hợp các phép xoay
            val rotationMatrix = FloatArray(16)
            Matrix.multiplyMM(rotationMatrix, 0, rotationMatrixY, 0, rotationMatrixX, 0)

            // Áp dụng phép xoay vào ma trận biến đổi
            Matrix.multiplyMM(transformMatrix, 0, transformMatrix, 0, rotationMatrix, 0)

            // Áp dụng tỉ lệ
            Matrix.scaleM(transformMatrix, 0, totalScale, totalScale, totalScale)

            transformManager.setTransform(instance, transformMatrix)


        }
    }

    fun rotateModel(deltaX : Float , deltaY : Float) {
        rotationX += deltaY * 0.1f
        rotationY += deltaX * 0.1f
        updateModelTransform()
        }


    fun scaleModel(scaleFactor: Float){
        totalScale *=scaleFactor

        totalScale = Math.max(0.1f , Math.min(totalScale ,2.0f))
        updateModelTransform()
    }
    }

