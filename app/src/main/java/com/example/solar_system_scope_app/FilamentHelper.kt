package com.example.solar_system_scope_app

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.opengl.Matrix
import android.util.Log
import android.view.Choreographer
import android.view.Surface
import com.google.android.filament.*
import com.google.android.filament.gltfio.*
import com.google.android.filament.utils.Mat4
import com.google.android.filament.utils.angle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class FilamentHelper(private val context: Context,
                     private var surface: Surface,
                    private val engine: Engine,
                    private val scene: Scene) {


    private var swapChain: SwapChain? = null
    private val renderer: Renderer = engine.createRenderer()
    private val view: View = engine.createView()
    private val camera: Camera
    private val cameraEntity: Int
    private val materialProvider: MaterialProvider

    private val assetLoader: AssetLoader
    private val resourceLoader: ResourceLoader
    private val entityManager: EntityManager = EntityManager.get()
    private var asset: FilamentAsset? = null



    // Biến để điều chỉnh camera trên trục X
     var currentCameraOffsetX: Float = 0f
    private var previousCameraOffsetX: Float = 0f
    private var targetCameraOffsetX: Float = 0f

    // Biến để quản lý chuyển tiếp cameraOffsetX
    private var isCameraOffsetTransitioning: Boolean = false

    // Biến thời gian cho chuyển tiếp cameraOffsetX
    private var cameraOffsetTransitionStartTime: Long = 0L
    private var cameraOffsetTransitionDuration: Long = 1000L // Thời gian chuyển tiếp, ví dụ 1 giây


     var cameraRotationX = 0f
     var cameraRotationY = 0f
    var cameraDistance = 10f
    private val minDistance = 1f
    private val maxDistance = 70f

    val planets = mutableListOf<Planet>()

    private var screenWidth: Float = 0f
    private var screenHeight: Float = 0f


    var currentTargetPosition = floatArrayOf(0.0f, 0.0f, 0.0f)
    var previousTargetPosition = floatArrayOf(0.0f, 0.0f, 0.0f)
    var targetTargetPosition = floatArrayOf(0.0f, 0.0f, 0.0f)
    var transitionStartTime = 0L
    val transitionDuration = 1000L
    var isTransitioning = false
    var width1:Int = 0
    var height1: Int = 0
    var targetPlanet: Planet? = null
        set(value) {
            if (field != value) {
                previousTargetPosition = currentTargetPosition.copyOf()
                targetTargetPosition = value?.getPosition() ?: floatArrayOf(0.0f, 0.0f, 0.0f)
                transitionStartTime = System.currentTimeMillis()
                isTransitioning = true
                field = value

            }
        }
    private val backgroundLoader : BackgroundLoader
    private val choreographer = Choreographer.getInstance()
    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
          render()
            choreographer.postFrameCallback(this)
        }
        
    }
    private var listener: PlanetNameListener? = null
    interface PlanetNameListener {
        fun onPlanetNameUpdated(planetName: String)
    }

    init {
        swapChain = engine.createSwapChain(surface)
        cameraEntity = entityManager.create()
        camera = engine.createCamera(cameraEntity)
        camera.lookAt(
            0.0, 0.0, 10.0,  // Vị trí camera (lùi ra xa hơn)
            0.0, 0.0, 0.0,   // Nhìn vào gốc tọa độ
            0.0, 1.0, 0.0    // Hướng lên trên
        )

        view.scene = scene
        view.camera = camera

        choreographer.postFrameCallback(frameCallback)

        view.isPostProcessingEnabled =true

        val bloomOptions = View.BloomOptions().apply {
            enabled = true
            strength = 2f       // Điều chỉnh cường độ của hiệu ứng Bloom
            resolution = 1080       // Độ phân giải của hiệu ứng Bloom
            levels = 10
            blendMode = View.BloomOptions.BlendMode.ADD
            threshold = false     // Sử dụng ngưỡng phát sáng
            highlight =1.0f
        }
        view.bloomOptions = bloomOptions
        camera.setProjection(45.0, 16.0 / 9.0, 0.1, 100.0, Camera.Fov.VERTICAL)
        camera.lookAt(
            0.0, 0.0, 3.0,  // Vị trí camera
            0.0, 0.0, 0.0,  // Nhìn vào đâu
            0.0, 1.0, 0.0   // Hướng lên trên
        )

        materialProvider = UbershaderProvider(engine)
        assetLoader = AssetLoader(engine, materialProvider, entityManager)
        resourceLoader = ResourceLoader(engine)



        val sunLightEntity = EntityManager.get().create()
        LightManager.Builder(LightManager.Type.POINT)
            .color(1.0f, 1.0f, 0.9f)
            .intensity(1000000f)
            .position(0.0f, 2f, 0.0f)
            .falloff(2000000.0f)
            .build(engine, sunLightEntity)

        scene.addEntity(sunLightEntity)

        val skyLightEntity = EntityManager.get().create()
        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(1.0f, 1.0f,0.9f)
            .intensity(10000f)
            .build(engine,skyLightEntity)
        scene.addEntity(skyLightEntity)

        val positions = mutableListOf(
            Triple(1.8f, 1.8f, 1.8f),
            Triple(-1.8f, 1.8f, 1.8f),
            Triple(1.8f, -1.8f, 1.8f),
            Triple(-1.8f, -1.8f, 1.8f),
            Triple(1.8f, 1.8f, -1.8f),
            Triple(-1.8f, 1.8f, -1.8f),
            Triple(1.8f, -1.8f, -1.8f),
            Triple(-1.8f, -1.8f, -1.8f),

            )


        for ((x, y, z) in positions) {
            val lightEntity = EntityManager.get().create()
            LightManager.Builder(LightManager.Type.SPOT)
                .color(1.0f, 1.0f, 0.8f)
                .intensity(12000000f) // Cường độ thấp hơn để hỗ trợ ánh sáng chính
                .position(x, y, z) // Vị trí dựa trên tọa độ x, y, z từ mảng positions
                .direction(-x, -y , -z)
                .falloff(50000.0f) // Giảm falloff để ánh sáng không quá xa
                .build(engine, lightEntity)
            scene.addEntity(lightEntity)


        }


        backgroundLoader = BackgroundLoader(context, engine, scene, assetLoader, resourceLoader)


        updateCameraTransform()

    }


//
//    private var orbitVisible: Boolean = true

    fun setOrbitsVisible(visible:Boolean){
//        if(visible == orbitVisible) {
//            return
//        }

        if(visible){

            for(entity in orbitEntities){
                scene.addEntity(entity)
            }
        }else{
            for(entity in orbitEntities){
                scene.removeEntity(entity)
            }
        }
//        orbitVisible = visible
    }
//
//    fun areOrbitVisible(): Boolean{
//        return orbitVisible
//    }


    fun getScreenPosition(planet: Planet): PointF? {
        val camera = camera ?: return null
        val viewMatrix = FloatArray(16)
        val projectionMatrixDouble = DoubleArray(16)

        // Lấy ma trận view và projection từ camera
        camera.getViewMatrix(viewMatrix)
        camera.getProjectionMatrix(projectionMatrixDouble)

        // Chuyển đổi DoubleArray sang FloatArray
        val projectionMatrix = projectionMatrixDouble.map { it.toFloat() }.toFloatArray()

        // Tính toán vị trí trên màn hình
        val planetPos = planet.getPosition()
        val worldPos = floatArrayOf(planetPos[0], planetPos[1], planetPos[2], 1.0f)
        val clipSpacePos = FloatArray(4)

        multiplyMatrix(projectionMatrix, viewMatrix, clipSpacePos, worldPos)

        if (clipSpacePos[3] != 0f) {
            clipSpacePos[0] /= clipSpacePos[3]
            clipSpacePos[1] /= clipSpacePos[3]
            clipSpacePos[2] /= clipSpacePos[3]
        }

        val screenX = ((clipSpacePos[0] + 1) / 2) * screenWidth
        val screenY = ((1 - clipSpacePos[1]) / 2) * screenHeight

        return if (clipSpacePos[0] in -1f..1f && clipSpacePos[1] in -1f..1f && clipSpacePos[2] in -1f..1f) {
            PointF(screenX, screenY)
        } else {
            null
        }
    }
    private fun multiplyMatrix(
        proj: FloatArray,
        view: FloatArray,
        result: FloatArray,
        pos: FloatArray
    ) {
        val temp = FloatArray(16)
        Matrix.multiplyMM(temp, 0, proj, 0, view, 0)
        Matrix.multiplyMV(result, 0, temp, 0, pos, 0)
    }



    fun updateScreenSize(width: Int, height: Int) {
        screenWidth = width.toFloat()
        screenHeight = height.toFloat()
    }
    fun loadBackground(fileName: String) {
        backgroundLoader.loadBackground(fileName)
    }


    fun resize(width: Int, height: Int) {
        view.viewport = Viewport(0, 0, width, height)
        val aspect = width.toDouble() / height
        val verticalFov = 45.0
        val near = 0.1
        val far = 1000.0


            // Thiết lập ma trận chiếu không đối xứng
            val offsetX = currentCameraOffsetX.toDouble() // Điều chỉnh giá trị này theo nhu cầu
            val eyeZ = cameraDistance.toDouble()

            // Tính toán top và bottom của frustum
            val verticalFovRadians = Math.toRadians(verticalFov)
            val top = near * Math.tan(verticalFovRadians / 2)
            val bottom = -top

            // Tính toán right và left của frustum trước khi điều chỉnh
            val right = top * aspect
            val left = -right

            // Điều chỉnh left và right để tạo ma trận chiếu không đối xứng
            val leftAdjusted = left - offsetX * near / eyeZ
            val rightAdjusted = right - offsetX * near / eyeZ

            // Tạo mảng DoubleArray cho ma trận chiếu (theo thứ tự cột chính - column-major order)
            val projectionMatrix = DoubleArray(16)


// Cột thứ nhất (column 0)
            projectionMatrix[0] = (2 * near) / (rightAdjusted - leftAdjusted) // m00
            projectionMatrix[1] = 0.0                                         // m10
            projectionMatrix[2] = 0.0                                         // m20
            projectionMatrix[3] = 0.0                                         // m30

// Cột thứ hai (column 1)
            projectionMatrix[4] = 0.0                                         // m01
            projectionMatrix[5] = (2 * near) / (top - bottom)                 // m11
            projectionMatrix[6] = 0.0                                         // m21
            projectionMatrix[7] = 0.0                                         // m31

// Cột thứ ba (column 2)
            projectionMatrix[8] = (rightAdjusted + leftAdjusted) / (rightAdjusted - leftAdjusted) // m02
            projectionMatrix[9] = (top + bottom) / (top - bottom)                                 // m12
            projectionMatrix[10] = -(far + near) / (far - near)                                   // m22
            projectionMatrix[11] = -1.0                                                           // m32

// Cột thứ tư (column 3)
            projectionMatrix[12] = 0.0                                          // m03
            projectionMatrix[13] = 0.0                                          // m13
            projectionMatrix[14] = -(2 * far * near) / (far - near)             // m23
            projectionMatrix[15] = 0.0                                          // m33

            // Thiết lập ma trận chiếu tùy chỉnh
            camera.setCustomProjection(projectionMatrix, near, far)


        width1 = width
        height1 = height
    }






    fun setOrbitSpeedMultiplier(multiplier: Float){
        orbitSpeedMultiplier = multiplier
    }



    fun render() {
        if (swapChain == null) {
            Log.e("FilamentHelper", "SwapChain is null, cannot render.")
            return
        }
        val frametime = System.nanoTime()
        val currentTime = System.currentTimeMillis()
        for (planet in planets) {

            // Cập nhật góc quỹ đạo và tự quay
            planet.tempAngle += planet.orbitSpeed * orbitSpeedMultiplier
            planet.tempRotation += planet.rotationSpeed * orbitSpeedMultiplier

            // Tính toán vị trí trên quỹ đạo
            val position = planet.getPosition()
            val x = position[0]
            val y = position[1]
            val z = position[2]

            // Tạo ma trận nghiêng trục hành tinh (axis tilt)
            val tiltMatrix = FloatArray(16)
            Matrix.setIdentityM(tiltMatrix, 0)
            Matrix.rotateM(tiltMatrix, 0, planet.axisTilt, 1.0f, 0.0f, 0.0f)

            // Tạo ma trận biến đổi cục bộ (modelMatrix)
            val modelMatrix = FloatArray(16)
            Matrix.setIdentityM(modelMatrix, 0)

            // Áp dụng nghiêng trục trước khi quay quanh trục
            Matrix.multiplyMM(modelMatrix, 0, tiltMatrix, 0, modelMatrix, 0)

            // Áp dụng tự quay quanh trục của hành tinh
            Matrix.rotateM(modelMatrix, 0, planet.tempRotation, 0.0f, 1.0f, 0.0f)

            // Áp dụng scale
            Matrix.scaleM(modelMatrix, 0, planet.scale, planet.scale, planet.scale)

            // Tạo ma trận biến đổi quỹ đạo (orbitMatrix)
            val orbitMatrix = FloatArray(16)
            Matrix.setIdentityM(orbitMatrix, 0)

            // Áp dụng độ nghiêng quỹ đạo (inclination)
            if (planet.inclination != 0.0f) {
                Matrix.rotateM(orbitMatrix, 0, planet.inclination, 0.0f, 0.0f, 1.0f)
            }

            // Dịch chuyển đến vị trí trên quỹ đạo
            Matrix.translateM(orbitMatrix, 0, x, y, z)

            // Kết hợp các ma trận biến đổi: finalMatrix = orbitMatrix * modelMatrix
            val finalMatrix = FloatArray(16)
            Matrix.multiplyMM(finalMatrix, 0, orbitMatrix, 0, modelMatrix, 0)

            // Gán ma trận biến đổi cho hành tinh
            planet.transformMatrix = finalMatrix


        }

        for (planet in planets) {
            val transformManager = engine.transformManager
            val rootEntity = planet.asset.root
            val instance = transformManager.getInstance(rootEntity)
            if (instance != 0) {
                transformManager.setTransform(instance, planet.transformMatrix)
            }
        }
//        listener?.onPlanetNameUpdated(currentPlanetName)
        (context as MainActivity).showPlanetNames()

        updateCameraTransform()
        if (swapChain != null && renderer != null) {
            if (renderer.beginFrame(swapChain!!, frametime)) {
                renderer.render(view)
                renderer.endFrame()
            }
        } else {
            Log.e("FilamentHelper", "SwapChain hoặc Renderer chưa sẵn sàng")
        }
    }





    fun destroy() {
        choreographer.removeFrameCallback(frameCallback)
        // Giải phóng tài nguyên
        if (asset != null) {
            assetLoader.destroyAsset(asset!!)
            asset = null
        }

        planets.forEach {
                planets -> assetLoader.destroyAsset(planets.asset)
        }
        planets.clear()


        assetLoader.destroy()
        resourceLoader.destroy()
        backgroundLoader.destroy()
        engine.destroyRenderer(renderer)
        engine.destroyView(view)
        engine.destroyScene(scene)
        engine.destroyCameraComponent(cameraEntity)
        engine.destroySwapChain(swapChain!!)
        engine.destroy()
    }
    fun destroySwapChain() {
        swapChain?.let {
            engine.destroySwapChain(it)
            swapChain = null
        }
    }

    fun createSwapChain(surface: Surface) {
        this.surface = surface
        swapChain = engine.createSwapChain(surface)
    }

    fun readAsset(context: Context, fileName: String): ByteBuffer {
        context.assets.open(fileName).use { input ->
            val bytes = input.readBytes()
            val buffer = ByteBuffer.allocateDirect(bytes.size)
            buffer.put(bytes)
            buffer.flip()
            return buffer
        }
    }




    fun rotateCamera(deltaX: Float , deltaY: Float){
        cameraRotationY = (cameraRotationY + deltaX * 0.1f) % 360f
        cameraRotationX += deltaY * 0.1f

        cameraRotationX = Math.max(-89f ,Math.min(89f , cameraRotationX))

        updateCameraTransform()
    }

    fun zoomCamera(scaleFactor: Float) {
        cameraDistance *= scaleFactor
        // Giới hạn khoảng cách camera
        cameraDistance = Math.max(minDistance, Math.min(maxDistance, cameraDistance))

        updateCameraTransform()

    }
    private fun easeInOutQuad(t: Float): Float {
        return if (t < 0.5f) {
            2 * t * t
        } else {
            -1 + (4 - 2 * t) * t
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun updateCameraTransform() {


            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - transitionStartTime

            if (isTransitioning) {
                if (elapsedTime < transitionDuration) {
                    val t = elapsedTime.toFloat() / transitionDuration.toFloat()
                    val easedT = easeInOutQuad(t)

                    // Nội suy vị trí mục tiêu hiện tại
                    currentTargetPosition[0] =
                        previousTargetPosition[0] * (1 - easedT) + targetTargetPosition[0] * easedT
                    currentTargetPosition[1] =
                        previousTargetPosition[1] * (1 - easedT) + targetTargetPosition[1] * easedT
                    currentTargetPosition[2] =
                        previousTargetPosition[2] * (1 - easedT) + targetTargetPosition[2] * easedT
                } else {
                    // Hoàn thành chuyển tiếp
                    currentTargetPosition = targetTargetPosition.copyOf()
                    isTransitioning = false
                 }

            } else {
                // Khi không trong quá trình chuyển tiếp, cập nhật vị trí mục tiêu theo hành tinh di chuyển
                targetPlanet?.let { planet ->
                    currentTargetPosition = planet.getPosition()
                }

            }
        val elapsedOffsetTime = currentTime - cameraOffsetTransitionStartTime
        if (isCameraOffsetTransitioning) {
            if (elapsedOffsetTime < cameraOffsetTransitionDuration) {
                val t = elapsedOffsetTime.toFloat() / cameraOffsetTransitionDuration.toFloat()
                val easedT = easeInOutQuad(t.coerceIn(0f, 1f))

                // Nội suy cameraOffsetX hiện tại
                currentCameraOffsetX =
                    previousCameraOffsetX * (1 - easedT) + targetCameraOffsetX * easedT
            } else {
                // Hoàn thành chuyển tiếp
                currentCameraOffsetX = targetCameraOffsetX
                isCameraOffsetTransitioning = false
            }
            // Gọi lại hàm resize để cập nhật ma trận chiếu
            resize(width1, height1)

        }


            // Tính toán vị trí camera dựa trên currentTargetPosition
            val radX = Math.toRadians(cameraRotationX.toDouble())
            val radY = Math.toRadians(cameraRotationY.toDouble())

            val camX = (cameraDistance * Math.cos(radX) * Math.sin(radY)).toFloat() + currentTargetPosition[0]
            val camY = (cameraDistance * Math.sin(radX)).toFloat() + currentTargetPosition[1]
            val camZ = (cameraDistance * Math.cos(radX) * Math.cos(radY)).toFloat() + currentTargetPosition[2]

            // Cập nhật hướng nhìn của camera

                camera.lookAt(
                    camX.toDouble(),
                    camY.toDouble(),
                    camZ.toDouble(),
                    currentTargetPosition[0].toDouble() ,
                    currentTargetPosition[1].toDouble(),
                    currentTargetPosition[2].toDouble(),
                    0.0,
                    1.0,
                    0.0  // Hướng lên trên
                )


            }

   // hàm để bắt đầu và kết thúc chuyển tiếp
    fun startCameraOffsetTransition(toOffsetX: Float, duration: Long = 1000L) {
        isCameraOffsetTransitioning = true
        cameraOffsetTransitionStartTime = System.currentTimeMillis()
        cameraOffsetTransitionDuration = duration

        previousCameraOffsetX = currentCameraOffsetX
        targetCameraOffsetX = toOffsetX
       Log.d("startCameraOffsetTransition","run okee toOffsetX = ${toOffsetX}")
    }


    @SuppressLint("SuspiciousIndentation")
    fun addPlanet(fileName: String,
                  name: String,
                  orbitRadiusA: Float,
                  eccentricity:Float,
                  orbitSpeed: Float,
                  scale: Float,
                  inclination: Float,
                  axisTilt: Float,
                  rotation: Float,
                  rotationSpeed: Float,
                  parent :  Planet? = null,
                  buffer : ByteBuffer ) : Planet {

        val planetAsset = assetLoader.createAsset(buffer)
        val  orbitRadiusB = orbitRadiusA * Math.sqrt((1 - eccentricity * eccentricity).toDouble()).toFloat()
        if (planetAsset == null) {
            Log.e("FilamentHelper01", "Không thể tạo asset từ tệp $fileName")
            throw IllegalStateException("Không thể tải mô hình GLTF.")
        }

        resourceLoader.loadResources(planetAsset)
        scene.addEntities(planetAsset.entities)
        val entity = planetAsset.root
        val planet = Planet(
            name = name,
            asset = planetAsset,
            entity = entity ,
            orbitRadiusA = orbitRadiusA,
            orbitRadiusB = orbitRadiusB,
            eccentricity = eccentricity,
            orbitSpeed = orbitSpeed,
            scale = scale,
            inclination = inclination,
            rotation = rotation ,
            axisTilt = axisTilt,
            rotationSpeed = rotationSpeed,
            parent = parent
        )

        // Đặt kích thước ban đầu cho hành tinh
        val transformManager = engine.transformManager
        val rootEntity = planetAsset.root
        val instance = transformManager.getInstance(rootEntity)
        if (instance != 0) {
            val transformMatrix = FloatArray(16)
            Matrix.setIdentityM(transformMatrix, 0)
            Matrix.scaleM(transformMatrix, 0, scale, scale, scale)
            transformManager.setTransform(instance, transformMatrix)
        }

        if (planet.parent != null) {
            val parentInstance = transformManager.getInstance(planet.parent.asset.root)
            if (instance != 0 && parentInstance != 0) {
                transformManager.setParent(instance, parentInstance)
            } else {
                Log.e("addPlanet", "Không thể thiết lập parent cho $name")
            }
        }
        planets.add(planet)

        if(parent == null){
            val  segments = getSegmentsForOrbit(cameraDistance)
            val (vertexData, indexData) = createOrbitRing(orbitRadiusA, orbitRadiusB,eccentricity, segments,thickness = 0.02f, verticalThickness = 0.01f)
            val (vertexBuffer, indexBuffer) = createOrbitBuffers(engine, vertexData, indexData)
            val orbitMaterialInstance = createOrbitMaterial(engine)
            if(orbitMaterialInstance !=null) {
                val orbitEntity =  addOrbitEntityToScene(
                    engine,
                    scene,
                    vertexBuffer,
                    indexBuffer,
                    orbitMaterialInstance
                )
                orbitEntities.add(orbitEntity)
                applyOrbitInclination(engine , orbitEntity , inclination)
            }else{
                Log.e("addPlanet", "orbitMaterialInstance là null")
            }

        } else {
            // Nếu hành tinh có parent
            val  segments = getSegmentsForOrbit(cameraDistance)
            val (vertexData, indexData) = createOrbitRing(
                orbitRadiusA,
                orbitRadiusB,
                eccentricity,
                segments,
                thickness = 0.05f, // Nhỏ hơn quỹ đạo của các hành tinh quanh Mặt Trời
                verticalThickness = 0.05f
            )
            val (vertexBuffer, indexBuffer) = createOrbitBuffers(engine, vertexData, indexData)
            val orbitMaterialInstance = createOrbitMaterial(engine)
            if (orbitMaterialInstance != null) {
                val orbitEntity = addOrbitEntityToScene(
                    engine,
                    scene,
                    vertexBuffer,
                    indexBuffer,
                    orbitMaterialInstance
                )

                applyOrbitInclination(engine, orbitEntity, inclination)

                // Thiết lập ring quỹ đạo là con của hành tinh cha
                val orbitInstance = transformManager.getInstance(orbitEntity)
                val parentInstance = transformManager.getInstance(parent.asset.root)
                if (orbitInstance != 0 && parentInstance != 0) {
                    transformManager.setParent(orbitInstance, parentInstance)
                }

            } else {
                Log.e("addPlanet", "orbitMaterialInstance là null")
            }
        }

        return planet
    }

    fun reloadOrbits(){

        removeOrbits()

        for(planet in planets){
            val segments = getSegmentsForOrbit(cameraDistance)
            val (vertexData ,indexData) = createOrbitRing(planet.orbitRadiusA,
                planet.orbitRadiusB , planet.eccentricity , thickness = 0.02f , verticalThickness =  0.01f)
            val (vertexBuffer , indexBuffer) = createOrbitBuffers(engine , vertexData, indexData)
            val orbitMaterialInstance = createOrbitMaterial(engine)
            if(orbitMaterialInstance != null){
                val orbitEntity = addOrbitEntityToScene(engine, scene , vertexBuffer,
                    indexBuffer , orbitMaterialInstance)
                applyOrbitInclination(engine , orbitEntity , planet.inclination)
                orbitEntities.add(orbitEntity)

                if(planet.parent != null){
                    val transformManager = engine.transformManager
                    val orbitInstance = transformManager.getInstance(orbitEntity)
                    val parentInstance = transformManager.getInstance(planet.parent.asset.root)
                    if(orbitInstance != 0  && parentInstance != 0){
                        transformManager.setParent(orbitInstance , parentInstance)
                    }
                }
            }
        }
    }
    fun scalePlanet(planet: Planet,  scale: Float){
            val transformManager = engine.transformManager
            val instance = transformManager.getInstance(planet.entity)
        if (instance != 0) {
                val currentTranform = FloatArray(16)
                transformManager.getTransform(instance, currentTranform)


                val scaleMatrix = FloatArray(16)
                Matrix.setIdentityM(scaleMatrix, 0)
                Matrix.scaleM(scaleMatrix, 0 , scale, scale, scale )

                Matrix.multiplyMM(currentTranform,0,currentTranform , 0 , scaleMatrix ,0)

                transformManager.setTransform(instance , currentTranform)
            }
        planet.scale = scale
    }

    fun getChildPlanets(parent: Planet): List<Planet> {
        return planets.filter { it.parent == parent }
    }

    private val orbitEntities = mutableListOf<Int>()
    fun removeOrbits(){
        for(entity in orbitEntities) {
            scene.removeEntity(entity)
            engine.destroyEntity(entity)
        }
        orbitEntities.clear()
    }


    private val orbitRingCache = mutableMapOf<String,Pair<FloatArray,ShortArray>>()
    fun createOrbitRing(
        orbitRadiusA: Float,
        orbitRadiusB: Float,
        eccentricity: Float,
        segments: Int = 40,
        thickness: Float = 0.01f,
        verticalThickness: Float = 0.01f // Độ dày theo trục Y
    ): Pair<FloatArray, ShortArray> {

        val cacheKey = "$orbitRadiusA - $orbitRadiusB - $eccentricity - $segments - $thickness - $verticalThickness"
        orbitRingCache[cacheKey]?.let {
            return it
        }

        val vertexCount = (segments + 1) * 4 // Mỗi segment có 4 đỉnh (top outer, top inner, bottom outer, bottom inner)
        val vertexData = FloatArray(vertexCount * 3) // Mỗi đỉnh có 3 giá trị (x, y, z)
        val indexCount = segments * 18
        val indexData = ShortArray(indexCount)

        val c = orbitRadiusA * eccentricity // Khoảng cách từ tâm elip đến tiêu điểm

        var vertexIndex = 0
        var indexIndex = 0

        val halfHeight = verticalThickness / 2

        val cosValue = FloatArray(segments + 1)
        val sinValue = FloatArray(segments + 1)
        for(i in 0..segments){
            val angle = (2.0 * Math.PI * i / segments).toFloat()
            cosValue[i] = Math.cos(angle.toDouble()).toFloat()
            sinValue[i] = Math.sin(angle.toDouble()).toFloat()
        }


        for (i in 0..segments) {
            val angle = (2.0 * Math.PI * i / segments).toFloat()
            val cosAngle = Math.cos(angle.toDouble()).toFloat()
            val sinAngle = Math.sin(angle.toDouble()).toFloat()

            val xOuter = ((orbitRadiusA + thickness / 2) * cosAngle) - c
            val zOuter = (orbitRadiusB + thickness / 2) * sinAngle
            val xInner = ((orbitRadiusA - thickness / 2) * cosAngle) - c
            val zInner = (orbitRadiusB - thickness / 2) * sinAngle

            // đỉnh trên ngoài
            vertexData[vertexIndex++] = xOuter
            vertexData[vertexIndex++] = halfHeight
            vertexData[vertexIndex++] = zOuter

            // trên trong
            vertexData[vertexIndex++] = xInner
            vertexData[vertexIndex++] = halfHeight
            vertexData[vertexIndex++] = zInner

            // dưới ngoài
            vertexData[vertexIndex++] = xOuter
            vertexData[vertexIndex++] = -halfHeight
            vertexData[vertexIndex++] = zOuter

            // dưới trong
            vertexData[vertexIndex++] = xInner
            vertexData[vertexIndex++] = -halfHeight
            vertexData[vertexIndex++] = zInner
        }

        for (i in 0 until segments) {
            val start = i * 4

            // Mặt bên ngoài
            indexData[indexIndex++] = (start).toShort()
            indexData[indexIndex++] = (start + 4).toShort()
            indexData[indexIndex++] = (start + 2).toShort()

            indexData[indexIndex++] = (start + 2).toShort()
            indexData[indexIndex++] = (start + 4).toShort()
            indexData[indexIndex++] = (start + 6).toShort()


            // Mặt trên
            indexData[indexIndex++] = (start).toShort()
            indexData[indexIndex++] = (start + 1).toShort()
            indexData[indexIndex++] = (start + 5).toShort()

            indexData[indexIndex++] = (start).toShort()
            indexData[indexIndex++] = (start + 5).toShort()
            indexData[indexIndex++] = (start + 4).toShort()


            // Mặt dưới
            indexData[indexIndex++] = (start + 2).toShort()
            indexData[indexIndex++] = (start + 6).toShort()
            indexData[indexIndex++] = (start + 7).toShort()

            indexData[indexIndex++] = (start + 2).toShort()
            indexData[indexIndex++] = (start + 7).toShort()
            indexData[indexIndex++] = (start + 3).toShort()
        }
        val result = Pair(vertexData, indexData)
        orbitRingCache[cacheKey] = result
        return result
    }



    fun getSegmentsForOrbit(cameraDistance: Float): Int {
        return when {
            cameraDistance < 5f -> 100
            cameraDistance < 10f -> 70
            cameraDistance < 30f ->40
            else -> 20
        }
    }


    fun createOrbitBuffers(
        engine: Engine,
        vertexData: FloatArray,
        indexData: ShortArray
    ): Pair<VertexBuffer, IndexBuffer> {
        val vertexBuffer = VertexBuffer.Builder()
            .bufferCount(1)
            .vertexCount(vertexData.size / 3)
            .attribute(
                VertexBuffer.VertexAttribute.POSITION,
                0,
                VertexBuffer.AttributeType.FLOAT3,
                0,
                12
            )
            .build(engine)
        vertexBuffer.setBufferAt(engine, 0, vertexData.toFloatBuffer())

        val indexBuffer = IndexBuffer.Builder()
            .indexCount(indexData.size)
            .bufferType(IndexBuffer.Builder.IndexType.USHORT)
            .build(engine)
        indexBuffer.setBuffer(engine, indexData.toShortBuffer())

        return Pair(vertexBuffer, indexBuffer)
    }


    fun createOrbitMaterial(engine: Engine): MaterialInstance? {
        return try {
            // Đọc toàn bộ tệp vật liệu một cách chính xác
            val buffer = readAsset(context, "materials/orbit.filamat")
            // Tạo vật liệu từ buffer
            val material = Material.Builder()
                .payload(buffer, buffer.remaining())
                .build(engine)
            val materialInstance = material.createInstance()
            materialInstance.setParameter("baseColor", 1.0f, 1.0f, 1.0f)

            return materialInstance

        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("createOrbitMaterial", "Không thể đọc tệp orbit.filamat: ${e.message}")
            null
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("createOrbitMaterial", "Lỗi khi tạo vật liệu: ${e.message}")
            null
        }
    }



    fun addOrbitEntityToScene(
        engine: Engine,
        scene: Scene,
        vertexBuffer: VertexBuffer,
        indexBuffer: IndexBuffer,
        materialInstance: MaterialInstance
    ): Int {
        val entity = EntityManager.get().create()
      RenderableManager.Builder(1)
            .geometry(
                0,
                RenderableManager.PrimitiveType.TRIANGLES,
                vertexBuffer,
                indexBuffer,
                0,
                indexBuffer.indexCount
            )
            .material(0, materialInstance)
            .culling(false)
            .castShadows(false)
            .receiveShadows(false)
            .build(engine, entity)
        scene.addEntity(entity)
        return entity
    }


    fun FloatArray.toFloatBuffer(): java.nio.Buffer {
        val buffer = ByteBuffer.allocateDirect(this.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
        buffer.put(this).flip()
        return buffer
    }

    fun ShortArray.toShortBuffer(): java.nio.Buffer {
        val buffer = ByteBuffer.allocateDirect(this.size * 2).order(ByteOrder.nativeOrder()).asShortBuffer()
        buffer.put(this).flip()
        return buffer
    }


    fun applyOrbitInclination(engine: Engine, orbitEntity: Int, inclination: Float) {
        val transformManager = engine.transformManager
        val instance = transformManager.getInstance(orbitEntity)
        if (instance != 0) {
            val transformMatrix = FloatArray(16)
            Matrix.setIdentityM(transformMatrix, 0)
            Matrix.rotateM(transformMatrix, 0, inclination, 0.0f, 0.0f, 1.0f)
            transformManager.setTransform(instance, transformMatrix)
        }
    }


}