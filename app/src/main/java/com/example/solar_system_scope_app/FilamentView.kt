package com.example.solar_system_scope_app


import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Choreographer
import android.view.GestureDetector


import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import com.google.android.filament.Engine
import com.google.android.filament.EntityManager
import com.google.android.filament.LightManager
import com.google.android.filament.Scene
import com.google.android.filament.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.nio.Buffer
import java.nio.ByteBuffer
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
var access : Boolean = false
var orbitSpeedMultiplier: Float = 1.0f


// can sua may thang ghe phia tren
class FilamentView @JvmOverloads constructor(context: Context,
                                             attrs: AttributeSet? = null)
    : SurfaceView(context,attrs), SurfaceHolder.Callback {

    var filament: FilamentHelper? = null


    private val choreographer = Choreographer.getInstance()


    private  var count =0

    private var infoPanel: View? = null
    private var planetNameTextView : TextView? = null
    private val planetLights = mutableMapOf<Planet, Int>()


    private val engine = Engine.create()
    private val scene = engine.createScene()
    private lateinit var gestureDetector: GestureDetector
    private lateinit var gestureHandler: GestureHandler
    private var planetSelectionListener: PlanetSelectionListener? = null

    private var miniFilamentHelper: MiniFilamentHelper? = null
    private lateinit var effectmanager: Effectmanager

    fun setPlanetSelectionListener(listener: PlanetSelectionListener){
        this.planetSelectionListener = listener
    }
    fun getFilamentHelper(): FilamentHelper? {
        return filament
    }


    companion object {
        init {
            System.loadLibrary("filament-jni")
            Utils.init()
        }
    }

    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            filament?.updateCameraTransform()
            filament?.render()
            choreographer.postFrameCallback(this)
        }
    }


    init {
        holder.addCallback(this)


        gestureDetector = GestureDetector(context, GestureListener())
    }


    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            e?.let {
                val x = it.x
                val y = it.y
                handleSingleTap(x, y)
            }
            return super.onSingleTapConfirmed(e)
        }
    }


    fun setMiniFilamentHelper(helper: MiniFilamentHelper){
        this.miniFilamentHelper = helper
    }


    fun setInfoPanel(infoPanel: View , planetNameTextView: TextView){
        this.infoPanel = infoPanel
        this.planetNameTextView = planetNameTextView
    }

    private val job = Job()
    private val scope = CoroutineScope((Dispatchers.Main + job))
    private val bufferCache = mutableMapOf<String, ByteBuffer?>()


    private suspend fun getCachedBuffer(fileName: String): ByteBuffer? {
        return bufferCache[fileName] ?: withContext(Dispatchers.IO) {
            val buffer = filament?.readAsset(context, fileName)
            bufferCache[fileName] = buffer
            buffer
        }
    }
    private fun initializePlanets() {
        scope.launch{
            try {
                val sunBufferDeferred = async(Dispatchers.IO) { getCachedBuffer(  "Sun.glb")}
                val mercuryBufferDeferred = async(Dispatchers.IO) { getCachedBuffer( "Mercury.glb") }
                val venusBufferDeferred = async(Dispatchers.IO) { getCachedBuffer( "Venus.glb") }
                val earthBufferDeferred = async(Dispatchers.IO) { getCachedBuffer( "Earth.glb") }
                val moonBufferDeferred = async(Dispatchers.IO) { getCachedBuffer( "Moon.glb") }
                val marsBufferDeferred = async(Dispatchers.IO) { getCachedBuffer( "Mars.glb") }
                val jupiterBufferDeferred = async(Dispatchers.IO) { getCachedBuffer( "Jupiter.glb") }
                val saturnBufferDeferred = async(Dispatchers.IO) { getCachedBuffer( "Saturn.glb") }
                val uranusBufferDeferred = async(Dispatchers.IO) { getCachedBuffer( "Uranus.glb") }
                val neptuneBufferDeferred = async(Dispatchers.IO) { getCachedBuffer( "Neptune.glb") }


                val sunBuffer = sunBufferDeferred.await()
                val mercuryBuffer = mercuryBufferDeferred.await()
                val venusBuffer = venusBufferDeferred.await()
                val earthBuffer = earthBufferDeferred.await()
                val moonBuffer = moonBufferDeferred.await()
                val marsBuffer = marsBufferDeferred.await()
                val jupiterBuffer = jupiterBufferDeferred.await()
                val saturnBuffer = saturnBufferDeferred.await()
                val uranusBuffer = uranusBufferDeferred.await()
                val neptuneBuffer = neptuneBufferDeferred.await()

                if (sunBuffer == null || mercuryBuffer == null || venusBuffer == null ||
                    earthBuffer == null || moonBuffer == null || marsBuffer == null ||
                    jupiterBuffer == null || saturnBuffer == null || uranusBuffer == null ||
                    neptuneBuffer == null) {
                    Log.e("initializePlanets", "Một hoặc nhiều buffer mô hình trống.")
                    return@launch
                }
                filament?.let { filamentInstance ->
                    // Tải nền
                    filamentInstance.loadBackground("sky_background.glb")

                    // Thêm các hành tinh
                    sun812 = filamentInstance.addPlanet(
                        fileName = "Sun.glb",
                        name = "Sun",
                        orbitRadiusA = 0.5f*2f*0f,
                        eccentricity = 0f,
                        orbitSpeed = .1f*0f,
                        scale = 0.1f,
                        inclination = 0f,
                        axisTilt = 0.0f,
                        rotation = 0.0f,
                        rotationSpeed = (1/86400f)*0.4f,
                        buffer = sunBuffer
                    )

                    mecury812 = filamentInstance.addPlanet(
                        fileName = "Mercury.glb",
                        name = "Mercury",
                        orbitRadiusA = 0.5f*2f*2.0f,
                        eccentricity = 0.2056f,
                        orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*0.5f,
                        scale = 0.05f,
                        inclination = 7.0f,
                        axisTilt = 0.0f,
                        rotation = 0.0f,
                        rotationSpeed = (1/86400f)*.1f*1.0f,
                        buffer = mercuryBuffer
                    )



                    venus812 = filamentInstance.addPlanet(
                        fileName = "Venus.glb",
                        name = "Venus",
                        orbitRadiusA = 0.5f*2f*3.7f,
                        eccentricity = 0.0067f,
                        orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*0.35f,
                        scale = 0.005f,
                        inclination = 3.39f,
                        axisTilt = 177.4f,
                        rotation = 177.4f ,
                        rotationSpeed = (1/86400f)*.1f*-1.48f,
                        buffer = venusBuffer
                    )

                    earth812 = filamentInstance.addPlanet(
                        fileName = "Earth.glb",
                        name = "Earth",
                        orbitRadiusA = 0.5f*2f*5.0f,
                        eccentricity = 0.0167f,
                        orbitSpeed =(1/86400f)*100f*(1/365.25f)*.1f*0.3f,
                        scale = 0.00525f,
                        inclination = 0.00005f,
                        axisTilt = 23.44f,
                        rotation = 23.44f,
                        rotationSpeed = (1/86400f)*2.8f,
                        buffer = earthBuffer
                    )

                    moon812 = filamentInstance.addPlanet(
                        fileName = "Moon.glb",
                        name = "Moon",
                        orbitRadiusA = 130.9f, // khong duoc thay doi neu doi phai doi cung voi scale trai dat
                        eccentricity = 0.0549f,
                        orbitSpeed = (1/86400f)*.5f*2.5f,
                        scale = 12f,// khong duoc thay doi
                        inclination = 5.14f,
                        axisTilt = 6.68f,
                        rotation = 6.68f,
                        rotationSpeed = (1/86400f)*.6f*13.36f,
                        parent = earth812,
                        buffer = moonBuffer
                    )

                    mars812 = filamentInstance.addPlanet(
                        fileName = "Mars.glb",
                        name = "Mars",
                        orbitRadiusA = 0.5f*2f*7.6f,
                        eccentricity = 0.0934f,
                        orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*0.33f,
                        scale = 0.371f,
                        inclination = 1.85f,
                        axisTilt = 25.19f,
                        rotation = 25.19f,
                        rotationSpeed = (1/86400f)*.1f*1.02f,
                        buffer = marsBuffer
                    )

                    jupiter812 = filamentInstance.addPlanet(
                        fileName = "Jupiter.glb",
                        name = "Jupiter",
                        orbitRadiusA = 0.5f*2f*11f,
                        eccentricity = 0.049f,
                        orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*2.5f * 0.084f,
                        scale = 0.1f,
                        inclination = 1.31f,
                        axisTilt = -7.13f,
                        rotation = 13.13f,
                        rotationSpeed = (1/86400f)*.1f*1.41f,
                        buffer = jupiterBuffer
                    )

                    saturn812 = filamentInstance.addPlanet(
                        fileName = "Saturn.glb",
                        name = "Saturn",
                        orbitRadiusA = 0.5f*2f*16f,
                        eccentricity = 0.056f,
                        orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.01f*2.5f * 0.034f,
                        scale = 3f,
                        inclination = 2.49f,
                        axisTilt = 0.0f*23.73f,
                        rotation = 23.73f,
                        rotationSpeed = (1/86400f)*0.001f*.1f*0.1f*2.24f,
                        buffer = saturnBuffer
                    )

                    uranus812 = filamentInstance.addPlanet(
                        fileName = "Uranus.glb",
                        name = "Uranus",
                        orbitRadiusA = 0.5f*2f*19.22f,
                        eccentricity = 0.046f,
                        orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*2.5f * 0.012f,
                        scale = 0.001f,
                        inclination = 0.77f,
                        axisTilt = 0.0f*17.77f,
                        rotation = 17.77f,
                        rotationSpeed = (1/86400f)*0.001f*.1f*0.41f,
                        buffer = uranusBuffer
                    )

                    neptune812 = filamentInstance.addPlanet(
                        fileName = "Neptune.glb",
                        name = "Neptune",
                        orbitRadiusA = 0.5f*2f*22f,
                        eccentricity = 0.010f,
                        orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*2.5f * 0.006f,
                        scale = 0.007f,
                        inclination = 1.77f,
                        axisTilt = 0.0f*28.32f,
                        rotation = 0.0f*28.32f,
                        rotationSpeed = (1/86400f)*0.001f*.1f*0.48f,
                        buffer = neptuneBuffer
                    )
                }
            }catch (e : Exception){
                Log.e("initializePlanets", "Error initializing planets: ${e.message}", e)

            }
        }
    }



    private fun handleSingleTap(x: Float, y: Float) {
        val planets = listOf(sun812, earth812, moon812, mecury812, saturn812, mars812, jupiter812, uranus812, neptune812, venus812)
        val fragmentContainer : View = (context as MainActivity).findViewById(R.id.fragment_container)

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
                    filament?.targetPlanet = planet
//                    if(count !=0) {
//                        planetSelectionListener?.onPlanetSelected(planet.name)
//                    }
                    count++
                    post {
                        planetNameTextView?.text = planet.name
                        infoPanel?.visibility = View.VISIBLE
                        miniFilamentHelper?.loadPlanetModel(planet)
                    }
                    return
                }
            }
        }

        filament?.targetPlanet = sun812
        planetSelectionListener?.onPlanetSelected("")

        count =0
        post{
            if(access) {
                effectmanager = Effectmanager(filament!!)
                effectmanager.deactivateEffect()
                access = false
                Log.d("access!!!!!" , "${access} thay doi ")
            }
            infoPanel?.visibility = View.GONE
            planetNameTextView?.text = ""
            miniFilamentHelper?.clearPlanetModel()
            fragmentContainer.visibility = GONE

        }
    }
    override fun surfaceCreated(holder: SurfaceHolder){
        filament = FilamentHelper(context , holder.surface)

        gestureHandler = GestureHandler(filament)
        initializePlanets()
        startRendering()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        filament?.resize(width,height)
        filament?.let {
            choreographer.removeFrameCallback(frameCallback)
            it.destroySwapChain()
            it.createSwapChain(holder.surface)
            it.resize(width, height)
            it.updateScreenSize(width, height)
        }
        choreographer.postFrameCallback(frameCallback)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopRendering()
        filament?.destroy()
        filament = null
        bufferCache.clear()
        job.cancel()
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        return gestureDetector.onTouchEvent(event) || gestureHandler.handleTouch(event)
    }

    private fun startRendering(){
        choreographer.postFrameCallback(frameCallback)
    }
    private fun stopRendering(){
        choreographer.removeFrameCallback(frameCallback)
    }
    fun switchProjection() {
        filament?.let {
            val targetOffsetX = if (it.currentCameraOffsetX == 0f) 1.0f else 0.0f
            it.startCameraOffsetTransition(targetOffsetX)
        }
    }

}