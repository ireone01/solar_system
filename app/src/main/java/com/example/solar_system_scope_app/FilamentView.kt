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
import com.google.android.filament.utils.angle
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


//    private val engine = FilamentManager.engine
//    private val scene = engine!!.createScene()
    private lateinit var gestureDetector: GestureDetector
    private lateinit var gestureHandler: GestureHandler
    private var planetSelectionListener: PlanetSelectionListener? = null

    private var miniFilamentHelper: MiniFilamentHelper? = null
    private lateinit var effectmanager: Effectmanager
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



    private fun initializePlanets() {
        scope.launch{
            try {
                val filamentInstance = filament ?: return@launch
                val sunBuffer = DataManager.getPlanetBuffer(  "Sun.glb")
                val mercuryBuffer = DataManager.getPlanetBuffer( "Mercury.glb")
                val venusBuffer = DataManager.getPlanetBuffer( "Venus.glb")
                val earthBuffer = DataManager.getPlanetBuffer( "Earth.glb")
                val moonBuffer = DataManager.getPlanetBuffer( "Moon.glb")
                val marsBuffer = DataManager.getPlanetBuffer( "Mars.glb")
                val jupiterBuffer = DataManager.getPlanetBuffer( "Jupiter.glb")
                val saturnBuffer = DataManager.getPlanetBuffer( "Saturn.glb")
                val uranusBuffer = DataManager.getPlanetBuffer( "Uranus.glb")
                val neptuneBuffer = DataManager.getPlanetBuffer( "Neptune.glb")


                if (sunBuffer == null || mercuryBuffer == null || venusBuffer == null ||
                    earthBuffer == null || moonBuffer == null || marsBuffer == null ||
                    jupiterBuffer == null || saturnBuffer == null || uranusBuffer == null ||
                    neptuneBuffer == null) {
                    Log.e("initializePlanets", "Một hoặc nhiều buffer mô hình trống.")
                    return@launch
                }


                    // Tải nền
                    filamentInstance.loadBackground("sky_background.glb")

                    // Thêm các hành tinh
                    sun812 = filamentInstance.addPlanet(
                        fileName = "Sun.glb",
                        name = "Sun",
                        orbitRadiusA = 2f*0f,
                        eccentricity = 0f,
                        orbitSpeed = .1f*0f,
                        scale = 1.5f*0.1f,
                        inclination = 0f,
                        axisTilt = 0.0f,
                        rotation = 0.0f,
                        rotationSpeed = (1/86400f)*0.4f,
                        buffer = sunBuffer,
                    )

                    mecury812 = filamentInstance.addPlanet(
                        fileName = "Mercury.glb",
                        name = "Mercury",
                        orbitRadiusA = 2f*2.0f,
                        eccentricity = 0.2056f,
                        orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*0.5f,
                        scale = 1.2f*0.05f,

                        inclination = 7.0f,
                        axisTilt = 0.0f,
                        rotation = 0.0f,
                        rotationSpeed = (1/86400f)*.1f*1.0f,
                        buffer = mercuryBuffer
                    )



                    venus812 = filamentInstance.addPlanet(
                        fileName = "Venus.glb",
                        name = "Venus",
                        orbitRadiusA = 2f*3.7f,
                        eccentricity = 0.0067f,
                        orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*0.35f,
                        scale = 1.2f*0.005f,
                        inclination = 3.39f,
                        axisTilt = 177.4f,
                        rotation = 177.4f ,
                        rotationSpeed = (1/86400f)*.1f*-1.48f,
                        buffer = venusBuffer
                    )

                    earth812 = filamentInstance.addPlanet(
                        fileName = "Earth.glb",
                        name = "Earth",
                        orbitRadiusA = 2f*5.0f,
                        eccentricity = 0.0167f,
                        orbitSpeed =(1/86400f)*100f*(1/365.25f)*.1f*0.3f,
                        scale = 1.2f*0.00525f,
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
                        scale = 1.2f*12f,// khong duoc thay doi
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
                        orbitRadiusA = 2f*7.6f,
                        eccentricity = 0.0934f,
                        orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*0.33f,
                        scale = 1.2f*0.371f,
                        inclination = 1.85f,
                        axisTilt = 25.19f,
                        rotation = 25.19f,
                        rotationSpeed = (1/86400f)*.1f*1.02f,
                        buffer = marsBuffer
                    )

                    jupiter812 = filamentInstance.addPlanet(
                        fileName = "Jupiter.glb",
                        name = "Jupiter",
                        orbitRadiusA = 2f*11f,
                        eccentricity = 0.049f,
                        orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*2.5f * 0.084f,
                        scale = 1.2f*0.1f,
                        inclination = 1.31f,
                        axisTilt = -7.13f,
                        rotation = 13.13f,
                        rotationSpeed = (1/86400f)*.1f*1.41f,
                        buffer = jupiterBuffer
                    )

                    saturn812 = filamentInstance.addPlanet(
                        fileName = "Saturn.glb",
                        name = "Saturn",
                        orbitRadiusA = 2f*16f,
                        eccentricity = 0.056f,
                        orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.01f*2.5f * 0.034f,
                        scale = 1.2f*3f,
                        inclination = 2.49f,
                        axisTilt = 0.0f*23.73f,
                        rotation = 23.73f,
                        rotationSpeed = (1/86400f)*0.001f*.1f*0.1f*2.24f,
                        buffer = saturnBuffer
                    )

                    uranus812 = filamentInstance.addPlanet(
                        fileName = "Uranus.glb",
                        name = "Uranus",
                        orbitRadiusA = 2f*19.22f,
                        eccentricity = 0.046f,
                        orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*2.5f * 0.012f,
                        scale = 1.2f*0.001f,
                        inclination = 0.77f,
                        axisTilt = 0.0f*17.77f,
                        rotation = 17.77f,
                        rotationSpeed = (1/86400f)*0.001f*.1f*0.41f,
                        buffer = uranusBuffer
                    )

                    neptune812 = filamentInstance.addPlanet(
                        fileName = "Neptune.glb",
                        name = "Neptune",
                        orbitRadiusA = 2f*22f,
                        eccentricity = 0.010f,
                        orbitSpeed = (1/86400f)* 100f*(1/365.25f)*.1f*2.5f * 0.006f,
                        scale = 1.2f*0.007f,
                        inclination = 1.77f,
                        axisTilt = 0.0f*28.32f,
                        rotation = 0.0f*28.32f,
                        rotationSpeed = (1/86400f)*0.001f*.1f*0.48f,
                        buffer = neptuneBuffer
                    )

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
                val touchThreshold = 120f

                if (distance < touchThreshold) {
                    filament?.targetPlanet = planet

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
            miniFilamentHelper?.clearPlanetModel()
            infoPanel?.visibility = View.GONE
            planetNameTextView?.text = ""
            fragmentContainer.visibility = GONE


        }
    }

    override fun surfaceCreated(holder: SurfaceHolder){
        FilamentManager.initialize(context , holder.surface)
        filament = FilamentManager.filamentHelper

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
        FilamentManager.destroy()
        filament = null
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
    var height : Pair<Float,Float>? = null

    fun switchProjection() {
        filament?.let {
            val heightScreen = height!!.second/4f
            Log.e("heightScreen","$heightScreen")
            val targetOffsetX = if (it.currentCameraOffsetX == 0f) heightScreen else 0.0f
            it.startCameraOffsetTransition(targetOffsetX)
        }
    }


}