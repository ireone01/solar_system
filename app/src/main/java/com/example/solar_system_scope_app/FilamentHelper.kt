    package com.example.solar_system_scope_app

    import android.annotation.SuppressLint
    import android.content.Context
    import android.opengl.Matrix
    import android.util.Log
    import android.view.Surface
    import androidx.core.graphics.rotationMatrix
    import androidx.core.graphics.translationMatrix
    import com.google.android.filament.*
    import com.google.android.filament.filamat.MaterialBuilder
    import com.google.android.filament.gltfio.*
    import com.google.android.filament.utils.Float4
    import java.io.IOException
    import java.nio.ByteBuffer
    import java.nio.ByteOrder

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

        private var cameraRotationX = 0f
        private var cameraRotationY = 0f
        private var cameraDistance = 10f
        private val minDistance = 1f
        private val maxDistance = 80f

        private val planets = mutableListOf<Planet>()


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
                strength = 4.0f       // Điều chỉnh cường độ của hiệu ứng Bloom
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
                .color(1.0f , 1.0f , 1.0f)
                .intensity(10000000.0f)
                .position(0.0f, 0.0f , 0.0f )
                .falloff(100.0f)
                .build(engine , sunLightEntity)
            scene.addEntity(sunLightEntity)

            backgroundLoader = BackgroundLoader(context, engine, scene, assetLoader, resourceLoader)

            updateCameraTransform()

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

        fun loadBackground(fileName: String) {
            backgroundLoader.loadBackground(fileName)
        }



        fun resize(width: Int, height: Int) {
            Log.d("FilamentHelper", "resize: width=$width, height=$height")
            view.viewport = Viewport(0, 0, width, height)
            val aspect = width.toDouble() / height
            camera.setProjection(45.0, aspect, 0.1, 1000.0, Camera.Fov.VERTICAL)
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

                        // Dịch chuyển hành tinh tới vị trí
                        Matrix.translateM(transformMatrix, 0, x, y, z)

                        // Áp dụng nghiêng quỹ đạo nếu cần
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

                        // In log để kiểm tra
                        if (planet.name == "Moon" || planet.name == "Earth") {
                            Log.d("FilamentHelper", "${planet.name} Transform Matrix: ${transformMatrix.contentToString()}")
                        }
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


       public fun followPlanet(planet: Planet) {
            val transformManager = engine.transformManager
            val instance = transformManager.getInstance(planet.asset.root)

            if (instance != 0) {
                val transformMatrix = FloatArray(16)
                transformManager.getWorldTransform(instance, transformMatrix)

                // Lấy vị trí của hành tinh trong không gian thế giới (World space)
                val planetPosition = FloatArray(4)
                Matrix.multiplyMV(planetPosition, 0, transformMatrix, 0, floatArrayOf(0f, 0f, 0f, 1f), 0)

                // Tính toán vị trí camera (giữ khoảng cách cố định với hành tinh)
                val cameraDistance = 2f  // Điều chỉnh khoảng cách này theo nhu cầu của bạn
                val cameraX = planetPosition[0] + cameraDistance * Math.cos(Math.toRadians(cameraRotationY.toDouble())).toFloat()
                val cameraY = planetPosition[1] + cameraDistance * Math.sin(Math.toRadians(cameraRotationX.toDouble())).toFloat()
                val cameraZ = planetPosition[2] + cameraDistance * Math.cos(Math.toRadians(cameraRotationX.toDouble())).toFloat()

                // Cập nhật vị trí camera để theo dõi hành tinh
                camera.lookAt(
                    cameraX.toDouble(), cameraY.toDouble(), cameraZ.toDouble(),  // Vị trí camera
                    planetPosition[0].toDouble(), planetPosition[1].toDouble(), planetPosition[2].toDouble(),  // Nhìn vào vị trí của hành tinh
                    0.0, 1.0, 0.0  // Hướng lên trên
                )
                Log.d("FollowPlanet","đã tìm thấy tại ${cameraX} , ${cameraY} , ${cameraZ}")
            } else {
                Log.e("FollowPlanet", "Không thể lấy instance cho hành tinh ${planet.name}")
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

        private fun readAsset(context: Context, fileName: String): ByteArray {
            context.assets.open(fileName).use { input ->
                return input.readBytes()
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

        private fun updateCameraTransform() {
            // Tính toán vị trí camera dựa trên góc xoay và khoảng cách
            val radX = Math.toRadians(cameraRotationX.toDouble())
            val radY = Math.toRadians(cameraRotationY.toDouble())

            val x = (cameraDistance * Math.cos(radX) * Math.sin(radY)).toFloat()
            val y = (cameraDistance * Math.sin(radX)).toFloat()
            val z = (cameraDistance * Math.cos(radX) * Math.cos(radY)).toFloat()

            camera.lookAt(
                x.toDouble(), y.toDouble(), z.toDouble(),          // Vị trí camera
                0.0, 0.0, 0.0,    // Nhìn vào gốc tọa độ
                0.0, 1.0, 0.0     // Hướng lên trên
            )
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
                      rotationSpeed: Float,
                      parent :  Planet? = null) : Planet {
            val buffer = readAsset(context, fileName)
            val planetAsset = assetLoader.createAsset(ByteBuffer.wrap(buffer))
            val  orbitRadiusB = orbitRadiusA * Math.sqrt((1 - eccentricity * eccentricity).toDouble()).toFloat()
            if (planetAsset == null) {
                Log.e("FilamentHelper01", "Không thể tạo asset từ tệp $fileName")
                throw IllegalStateException("Không thể tải mô hình GLTF.")
            }

            resourceLoader.loadResources(planetAsset)
            scene.addEntities(planetAsset.entities)

            val planet = Planet(
                name = name,
                asset = planetAsset,
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
            val (vertexData, indexData) = createOrbitRing(orbitRadiusA, orbitRadiusB,eccentricity, thickness = 0.02f, verticalThickness = 0.009f)
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
            segments: Int = 100,
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
                val bytes = readAsset(context, "materials/orbit.filamat")
                Log.d("createOrbitMaterial", "Kích thước của orbit.filamat: ${bytes.size} bytes")

                val buffer = ByteBuffer.allocateDirect(bytes.size)
                    .order(ByteOrder.nativeOrder())
                    .put(bytes)
                    .flip()

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



