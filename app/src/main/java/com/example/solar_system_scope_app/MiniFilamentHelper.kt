package com.example.solar_system_scope_app

import android.content.Context
import android.opengl.Matrix
import android.util.Log
import android.view.Choreographer
import android.view.MotionEvent
import android.view.SurfaceView
import com.example.solar_system_scope_app.model.DataManager
import com.example.solar_system_scope_app.model.Planet
import com.google.android.filament.*
import com.google.android.filament.gltfio.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MiniFilamentHelper(private val context: Context, private val surfaceView: SurfaceView,
                        ) {
    private val engine1 = FilamentManager.engine
    private val engine = engine1!!
    private var swapChain: SwapChain? = null
    private val renderer = engine.createRenderer()
    private val view = engine.createView()
    private val scene = engine.createScene()
    private val cameraEntity = EntityManager.get().create()
    private val camera = engine.createCamera(cameraEntity)
    private var width = 0
    private var height = 0
    private val choreographer = Choreographer.getInstance()

    private var rotationAngle = 0.0f
    private var rotationSpeed = 0.5f  //  điều chỉnh tốc độ quay
    private var modelEntity: Int = 0  // Lưu trữ entity của mô hình
    var planetName: String? = null

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private var lightEntity : Int = 0


    private var assetLoader: AssetLoader? = null
    private var asset: FilamentAsset? = null
    private var materialProvider: MaterialProvider? = null
    private var isPlanetLoaded: Boolean = false
    init {
        surfaceView.setOnTouchListener { _, motionEvent ->
            if(motionEvent.action == MotionEvent.ACTION_DOWN){
                Log.d("MiniFilamentHelper", "onTouchListener: planetName=$planetName, isPlanetLoaded=$isPlanetLoaded")
                if (!isPlanetLoaded) {
                    Log.d("MiniFilamentHelper", "Model not yet loaded, ignoring click.")
                    return@setOnTouchListener false
                }
                planetName?.let { name ->
                    onClickListener?.invoke(name)
                }
                true
            }else{
                false
            }
        }
    }
    private var onClickListener: ((String) -> Unit)? = null

    fun setClinkListener(listener: (String) -> Unit){
        onClickListener= listener
    }


    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            val frametime = System.nanoTime()

            rotationAngle += rotationSpeed
            if (renderer.beginFrame(swapChain!!, frametime)) {

                updateModelTransform()
                renderer.render(view)
                renderer.endFrame()
            }
            choreographer.postFrameCallback(this)
        }
    }


    private fun updateModelTransform() {
        val transformManager = engine.transformManager
        val instance = transformManager.getInstance(modelEntity)
        if (instance != 0) {
            val transformMatrix = FloatArray(16)
            Matrix.setIdentityM(transformMatrix, 0)

            // Áp dụng phép quay quanh trục Y
            Matrix.rotateM(transformMatrix, 0, rotationAngle, 0.0f, 1.0f, 0.0f)

            transformManager.setTransform(instance, transformMatrix)
        }
    }


    fun init(width: Int, height: Int) {
        val surface = surfaceView.holder.surface
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

    val LIGHT_DIRECTIONAL = 250000.0f
    // them anh sang vao minifilament
    private fun setupLighting() {
        if(lightEntity !=0){
            return
        }
        val directionalLight = EntityManager.get().create()

        LightManager.Builder(LightManager.Type.SUN)
            .color(1.0f, 1.0f, 1.0f)  // Màu trắng cho ánh sáng
            .intensity(LIGHT_DIRECTIONAL)    // Tăng độ sáng để chiếu sáng tốt hơn
            .direction(-0.5f, -1.0f, -0.5f)  // Điều chỉnh hướng của ánh sáng để chiếu sáng mô hình tốt hơn
            .castShadows(true)        // Cho phép đổ bóng nếu cần
            .build(engine, directionalLight)

        // Thêm đèn vào scene
        scene.addEntity(directionalLight)

        lightEntity = directionalLight
    }


    fun loadPlanetModel(planet: Planet) {
        clearPlanetModel()
        Log.d("MiniFilamentHelper", "=== Bắt đầu load model: ${planet.name} ===")
        planetName = planet.name
//        val entities = scene.entities
//        scene.removeEntities(entities)
//        entities.forEach { entity ->
//            engine.destroyEntity(entity)
//        }

        isPlanetLoaded = false
        setupLighting()


        //can xem lai doan nay xem da su dung buffer luu tru chua
        scope.launch {
            // Tải mô hình của hành tinh
            val buffer = withContext(Dispatchers.IO) {
              DataManager.getPlanetBuffer("${planet.name}.glb")
            }
            if(buffer == null){
                return@launch
            }
            withContext(Dispatchers.Main){
                Log.d("MiniFilamentHelper", "=== Model buffer load xong: ${planet.name} ===")
                materialProvider = UbershaderProvider(engine)
                 // Tạo và tải asset
                 assetLoader = AssetLoader(engine, materialProvider!!, EntityManager.get())
             asset = assetLoader!!.createAsset(buffer!!)


            if (asset == null) {
                Log.e("MiniFilamentHelper", "Không thể tạo asset cho hành tinh: ${planet.name}")
                return@withContext
            }


            val resourceLoader = ResourceLoader(engine)
            resourceLoader.loadResources(asset!!)
            resourceLoader.destroy()



                // Thêm entities vào scene
                scene.addEntities(asset!!.entities)
                Log.d("MiniFilamentHelper", "Số lượng Entity trong Scene: ${scene.entities.size}")

                modelEntity = asset!!.root
                // Đặt camera nhìn vào mô hình
                frameModel(asset!!)
                Log.d("MiniFilamentHelper", "=== Thêm model vào scene xong: ${planet.name} ===")
                isPlanetLoaded = true
            }
        }
    }
    private val modelCatch = mutableMapOf<String, ByteArray>()


    fun clearPlanetModel() {
        scope.launch(Dispatchers.Main) {
//            // Xóa các entity khỏi scene
//            val entities = scene.entities
//            if (entities.isNotEmpty()) {
//                scene.removeEntities(entities)
//                entities.forEach { entity ->
//                    engine.destroyEntity(entity)
//                }
//            }
            if (modelEntity != 0) {
                scene.removeEntity(modelEntity)
                engine.destroyEntity(modelEntity)
                modelEntity = 0
            }

            // Giải phóng dữ liệu nguồn và hủy asset
            asset?.let { currentAsset ->
                currentAsset.releaseSourceData()
                assetLoader?.destroyAsset(currentAsset)
                asset = null
            }

            // Hủy assetLoader
            assetLoader?.destroy()
            assetLoader = null

            // Hủy materialProvider
            materialProvider?.destroyMaterials()
            materialProvider = null
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
        job.cancel()
        if(modelEntity !=0){
            scene.removeEntity(modelEntity)
            engine.destroyEntity(modelEntity)
            modelEntity = 0
        }
        if(lightEntity !=0){
            scene.removeEntity(lightEntity)
            engine.destroyEntity(lightEntity)
            lightEntity = 0
        }
        asset?.let { currentAsset ->
            assetLoader?.destroyAsset(currentAsset)
            asset = null
        }

        // can xem lai cai nay
        surfaceView.setOnTouchListener(null)


        materialProvider?.destroyMaterials()
        materialProvider = null
        assetLoader?.destroy()
        assetLoader = null
        modelCatch.clear()

        engine.destroyRenderer(renderer)
        engine.destroyView(view)
        engine.destroyScene(scene)
        engine.destroyCameraComponent(cameraEntity)
        swapChain?.let {
            engine.destroySwapChain(it)
            swapChain = null
        }
    }
}