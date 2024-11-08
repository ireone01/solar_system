    package com.example.solar_system_scope_app

    import android.annotation.SuppressLint
    import android.content.Context
    import android.graphics.PointF
    import android.opengl.Matrix
    import android.os.Handler
    import android.os.Looper
    import android.util.Log
    import android.view.Surface
    import androidx.core.graphics.rotationMatrix
    import androidx.core.graphics.translationMatrix
    import androidx.core.os.HandlerCompat.postDelayed
    import com.google.android.filament.*
    import com.google.android.filament.filamat.MaterialBuilder
    import com.google.android.filament.gltfio.*
    import com.google.android.filament.utils.Float4
    import com.google.android.filament.utils.Ray
    import java.io.IOException
    import java.nio.ByteBuffer
    import java.nio.ByteOrder
    import kotlin.math.abs
    import kotlin.random.Random

    class FilamentHelper(private val context: Context, private var surface: Surface) {

        val engine: Engine = Engine.create()
        private var swapChain: SwapChain = engine.createSwapChain(surface)
        private val renderer: Renderer = engine.createRenderer()
        val scene: Scene = engine.createScene()
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

        private var cameraRotationX = 0f
        private var cameraRotationY = 0f
        private var cameraDistance = 10f
        private val minDistance = 1f
        private val maxDistance = 80f

        private val planets = mutableListOf<Planet>()

        private var screenWidth: Float = 0f
        private var screenHeight: Float = 0f




        private val backgroundLoader : BackgroundLoader

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

            view.isPostProcessingEnabled =true

            val bloomOptions = View.BloomOptions().apply {
                enabled = true
                strength = 2f       // Điều chỉnh cường độ của hiệu ứng Bloom
                resolution = 1080      // Độ phân giải của hiệu ứng Bloom
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
                .intensity(10000000f)
                .position(0.0f, 2f, 0.0f)
                .falloff(100000000.0f)
                .build(engine, sunLightEntity)
            scene.addEntity(sunLightEntity)

            val positions1 = mutableListOf(
                Triple(25f, 0f, 0f),
                Triple(-25f, 0f, 0f),
                Triple(0f, 0f, 25f),
                Triple(0f, 0f, -25f),
            )
            for((x,y,z) in positions1){
            val sunLightEntity1 = EntityManager.get().create()
            LightManager.Builder(LightManager.Type.POINT)
                .color(1.0f, 1.0f, 0.9f)
                .intensity(10000000f)
                .position(x, y, z)
                .falloff(1000000.0f)
                .build(engine, sunLightEntity1)
            scene.addEntity(sunLightEntity1)

           }
            val positions = mutableListOf(
                Triple(1.4f, 1.4f, 1.4f),
                Triple(-1.4f, 1.4f, 1.4f),
                Triple(1.4f, -1.4f, 1.4f),
                Triple(-1.4f, -1.4f, 1.4f),
                Triple(1.4f, 1.4f, -1.4f),
                Triple(-1.4f, 1.4f, -1.4f),
                Triple(1.4f, -1.4f, -1.4f),
                Triple(-1.4f, -1.4f, -1.4f),

            )


            for ((x, y, z) in positions) {
                val lightEntity = EntityManager.get().create()
                LightManager.Builder(LightManager.Type.SPOT)
                    .color(1.0f, 1.0f, 0.8f)
                    .intensity(12000000f) // Cường độ thấp hơn để hỗ trợ ánh sáng chính
                    .position(x, y, z) // Vị trí dựa trên tọa độ x, y, z từ mảng positions
                    .direction(-x, -y , -z)
                    .falloff(5000.0f) // Giảm falloff để ánh sáng không quá xa
                    .build(engine, lightEntity)
                scene.addEntity(lightEntity)


            }



            backgroundLoader = BackgroundLoader(context, engine, scene, assetLoader, resourceLoader)


            updateCameraTransform()

        }

        fun init(surface: Surface){
            swapChain = engine.createSwapChain(surface)
            view.scene = scene
            view.camera = camera
        }

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
            Log.d("FilamentHelper", "resize: width=$width, height=$height")
            view.viewport = Viewport(0, 0, width, height)
            val aspect = width.toDouble() / height
            camera.setProjection(45.0, aspect, 0.1, 1000.0, Camera.Fov.VERTICAL)
        }

        fun updatePlanetTransforms() {
            val currentTime = System.currentTimeMillis()
            val deltaTime = currentTime - lastUpdateTime

            // Giới hạn cập nhật để tránh tính toán quá nhiều (ví dụ: cập nhật mỗi 16ms ~ 60fps)
            if (deltaTime < 16) return

            lastUpdateTime = currentTime

            for (planet in planets) {
                // Nếu cờ dirtyFlag bật hoặc deltaTime đã đủ, thực hiện cập nhật
                if (planet.dirtyFlag) {
                    val transformManager = engine.transformManager
                    val rootEntity = planet.asset.root
                    val instance = transformManager.getInstance(rootEntity)
                    if (instance != 0) {
                        val transformMatrix = FloatArray(16)
                        Matrix.setIdentityM(transformMatrix, 0)

                        // Tính toán vị trí mới dựa trên quỹ đạo
                        val angleInRadians = Math.toRadians(planet.angle.toDouble())
                        val x: Float
                        val z: Float
                        val y = 0.0f

                        if (planet.parent == null) {
                            val c = planet.orbitRadiusA * planet.eccentricity
                            x = (planet.orbitRadiusA * Math.cos(angleInRadians) - c).toFloat()
                            z = (planet.orbitRadiusB * Math.sin(angleInRadians)).toFloat()
                        } else {
                            x = (planet.orbitRadiusA * Math.cos(angleInRadians)).toFloat()
                            z = (planet.orbitRadiusB * Math.sin(angleInRadians)).toFloat()
                        }

                        Matrix.translateM(transformMatrix, 0, x, y, z)

                        // Áp dụng nghiêng quỹ đạo
                        if (planet.inclination != 0.0f) {
                            val inclinationMatrix = FloatArray(16)
                            Matrix.setIdentityM(inclinationMatrix, 0)
                            Matrix.rotateM(inclinationMatrix, 0, planet.inclination, 0.0f, 0.0f, 1.0f)
                            Matrix.multiplyMM(transformMatrix, 0, inclinationMatrix, 0, transformMatrix, 0)
                        }

                        // Tính toán ma trận tự quay quanh trục Y
                        val rotationMatrix = FloatArray(16)
                        Matrix.setIdentityM(rotationMatrix, 0)
                        Matrix.rotateM(rotationMatrix, 0, planet.rotation, 0.0f, 1.0f, 0.0f)

                        // Tạo ma trận scale
                        val scaleMatrix = FloatArray(16)
                        Matrix.setIdentityM(scaleMatrix, 0)
                        Matrix.scaleM(scaleMatrix, 0, planet.scale, planet.scale, planet.scale)

                        // Kết hợp các ma trận: rotation * scale
                        val modelMatrix = FloatArray(16)
                        Matrix.multiplyMM(modelMatrix, 0, rotationMatrix, 0, scaleMatrix, 0)

                        // Kết hợp với ma trận dịch chuyển
                        Matrix.multiplyMM(transformMatrix, 0, transformMatrix, 0, modelMatrix, 0)

                        // Thiết lập biến đổi cho hành tinh
                        transformManager.setTransform(instance, transformMatrix)

                        // Đặt lại cờ sau khi cập nhật
                        planet.dirtyFlag = false
                    }
                }
            }
        }



        fun render() {
            if (swapChain == null) {
                Log.e("FilamentHelper", "SwapChain is null, cannot render.")
                return
            }

            try {
                val frametime = System.nanoTime()
                Log.d("FilamentHelper", "Bắt đầu render, frameTimeNanos: $frametime")

                // Cập nhật và xoay các hành tinh trước khi bắt đầu frame
                for (planet in planets) {
                    // Cập nhật góc quỹ đạo và tự quay
                    planet.angle += planet.orbitSpeed
                    planet.rotation += planet.rotationSpeed

                    val transformManager = engine.transformManager
                    val rootEntity = planet.asset.root
                    val instance = transformManager.getInstance(rootEntity)
                    if (instance != 0) {
                        // Tính toán transform cục bộ
                        val transformMatrix = FloatArray(16)
                        Matrix.setIdentityM(transformMatrix, 0)

                        // Tính toán vị trí trên quỹ đạo
                        val angleInRadians = Math.toRadians(planet.angle.toDouble())
                        val x: Float
                        val z: Float
                        val y = 0.0f

                        if (planet.parent == null) {
                            val c = planet.orbitRadiusA * planet.eccentricity
                            x = (planet.orbitRadiusA * Math.cos(angleInRadians) - c).toFloat()
                            z = (planet.orbitRadiusB * Math.sin(angleInRadians)).toFloat()
                        } else {
                            x = (planet.orbitRadiusA * Math.cos(angleInRadians)).toFloat()
                            z = (planet.orbitRadiusB * Math.sin(angleInRadians)).toFloat()
                        }

                        Matrix.translateM(transformMatrix, 0, x, y, z)

                        // Áp dụng nghiêng quỹ đạo
                        if (planet.inclination != 0.0f) {
                            val inclinationMatrix = FloatArray(16)
                            Matrix.setIdentityM(inclinationMatrix, 0)
                            Matrix.rotateM(inclinationMatrix, 0, planet.inclination, 0.0f, 0.0f, 1.0f)
                            Matrix.multiplyMM(transformMatrix, 0, inclinationMatrix, 0, transformMatrix, 0)
                        }

                        // Tạo ma trận tự quay quanh trục Y
                        val rotationMatrix = FloatArray(16)
                        Matrix.setIdentityM(rotationMatrix, 0)
                        Matrix.rotateM(rotationMatrix, 0, planet.rotation, 0.0f, 1.0f, 0.0f)

                        // Tạo ma trận scale
                        val scaleMatrix = FloatArray(16)
                        Matrix.setIdentityM(scaleMatrix, 0)
                        Matrix.scaleM(scaleMatrix, 0, planet.scale, planet.scale, planet.scale)

                        // Kết hợp các ma trận: rotation * scale
                        val modelMatrix = FloatArray(16)
                        Matrix.multiplyMM(modelMatrix, 0, rotationMatrix, 0, scaleMatrix, 0)

                        // Kết hợp với ma trận dịch chuyển
                        Matrix.multiplyMM(transformMatrix, 0, transformMatrix, 0, modelMatrix, 0)

                        // Thiết lập biến đổi cho hành tinh
                        transformManager.setTransform(instance, transformMatrix)

                        updateCameraTransform()
                    }
                }

                if (renderer.beginFrame(swapChain, frametime)) {
                    renderer.render(view)
                    renderer.endFrame()
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

        fun updateCameraTransform() {
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - transitionStartTime

            if (isTransitioning) {
                if (elapsedTime < transitionDuration) {
                    val t = elapsedTime.toFloat() / transitionDuration.toFloat()
                    val easedT = easeInOutQuad(t)

                    // Nội suy vị trí mục tiêu hiện tại
                    currentTargetPosition[0] = previousTargetPosition[0] * (1 - easedT) + targetTargetPosition[0] * easedT
                    currentTargetPosition[1] = previousTargetPosition[1] * (1 - easedT) + targetTargetPosition[1] * easedT
                    currentTargetPosition[2] = previousTargetPosition[2] * (1 - easedT) + targetTargetPosition[2] * easedT
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

            // Tính toán vị trí camera dựa trên currentTargetPosition
            val radX = Math.toRadians(cameraRotationX.toDouble())
            val radY = Math.toRadians(cameraRotationY.toDouble())

            val camX = (cameraDistance * Math.cos(radX) * Math.sin(radY)).toFloat() + currentTargetPosition[0]
            val camY = (cameraDistance * Math.sin(radX)).toFloat() + currentTargetPosition[1]
            val camZ = (cameraDistance * Math.cos(radX) * Math.cos(radY)).toFloat() + currentTargetPosition[2]

            // Cập nhật hướng nhìn của camera
            camera.lookAt(
                camX.toDouble(), camY.toDouble(), camZ.toDouble(),
                currentTargetPosition[0].toDouble(), currentTargetPosition[1].toDouble(), currentTargetPosition[2].toDouble(),
                0.0, 1.0, 0.0  // Hướng lên trên
            )
        }


        private val renderHandler  = Handler(Looper.getMainLooper())
        @SuppressLint("SuspiciousIndentation")
        fun addPlanet(fileName: String,
                      name: String,
                      orbitRadiusA: Float,
                      eccentricity:Float,
                      orbitSpeed: Float,
                      scale: Float,
                      inclination: Float,
                      axisTilt: Float,
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
                angle = 0.0f,
                orbitRadiusA = orbitRadiusA,
                orbitRadiusB = orbitRadiusB,
                eccentricity = eccentricity,
                orbitSpeed = orbitSpeed,
                scale = scale,
                inclination = inclination,
                rotation = 0.0f ,
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
                    Log.d("addPlanetzzzz", "Thiết lập parent của $name là ${planet.parent.name}")
                } else {
                    Log.e("addPlanet", "Không thể thiết lập parent cho $name")
                }
            }
            planets.add(planet)

            if(parent == null){
            val (vertexData, indexData) = createOrbitRing(orbitRadiusA, orbitRadiusB,eccentricity, thickness = 0.02f, verticalThickness = 0.01f)
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

                applyOrbitInclination(engine , orbitEntity , inclination)

                Log.d("addPlanet", "Đã thêm quỹ đạo cho hành tinh $name")
            }else{
                Log.e("addPlanet", "orbitMaterialInstance là null")
            }

                } else {
                // Nếu hành tinh có parent, bạn có thể thêm quỹ đạo quanh hành tinh cha nếu muốn
                val (vertexData, indexData) = createOrbitRing(
                    orbitRadiusA,
                    orbitRadiusB,
                    eccentricity,
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
                        Log.d("addPlanet", "Thiết lập parent của quỹ đạo $name là ${parent.name}")
                    }

                    Log.d("addPlanet", "Đã thêm quỹ đạo cho hành tinh $name")
                } else {
                    Log.e("addPlanet", "orbitMaterialInstance là null")
                }
            }


            return planet
        }





        fun createOrbitRing(
            orbitRadiusA: Float,
            orbitRadiusB: Float,
            eccentricity: Float,
            segments: Int = 40,
            thickness: Float = 0.01f,
            verticalThickness: Float = 0.01f // Độ dày theo trục Y
        ): Pair<FloatArray, ShortArray> {
            val vertexCount = (segments + 1) * 4 // Mỗi segment có 4 đỉnh (top outer, top inner, bottom outer, bottom inner)
            val vertexData = FloatArray(vertexCount * 3) // Mỗi đỉnh có 3 giá trị (x, y, z)
            val indexCount = segments * 18
            val indexData = ShortArray(indexCount)

            val c = orbitRadiusA * eccentricity // Khoảng cách từ tâm elip đến tiêu điểm

            var vertexIndex = 0
            var indexIndex = 0

            val halfHeight = verticalThickness / 2

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

            return Pair(vertexData, indexData)
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
                Log.d("createOrbitMaterial", "Vật liệu được tạo thành công")

                val materialInstance = material.createInstance()
                materialInstance.setParameter("baseColor", 1.0f, 1.0f, 1.0f, 1.0f)
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
            Log.d("addOrbitEntityToScene", "Entity được tạo: $entity")
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
            Log.d("addOrbitEntityToScene", "Entity được thêm vào scene")

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



