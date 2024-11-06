package com.example.solar_system_scope_app


import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Choreographer
import android.view.GestureDetector


import android.view.MotionEvent
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import com.google.android.filament.Engine
import com.google.android.filament.EntityManager
import com.google.android.filament.LightManager
import com.google.android.filament.Scene
import com.google.android.filament.utils.Utils
import org.w3c.dom.Text
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

var currentTargetPosition = floatArrayOf(0.0f, 0.0f, 0.0f)
var previousTargetPosition = floatArrayOf(0.0f, 0.0f, 0.0f)
var targetTargetPosition = floatArrayOf(0.0f, 0.0f, 0.0f)
var transitionStartTime = 0L
val transitionDuration = 1000L
var isTransitioning = false
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

// can sua may thang ghe phia tren
class FilamentView @JvmOverloads constructor(context: Context,
                                             attrs: AttributeSet? = null)
    : SurfaceView(context,attrs), SurfaceHolder.Callback {

        var filament: FilamentHelper? = null


    private val choreographer = Choreographer.getInstance()




    private var infoPanel: View? = null
    private var planetNameTextView : TextView? = null
    private val planetLights = mutableMapOf<Planet, Int>()


    private val engine = Engine.create()
    private val scene = engine.createScene()
    private lateinit var gestureDetector: GestureDetector
    private lateinit var gestureHandler: GestureHandler


    private var miniFilamentHelper: MiniFilamentHelper? = null



    companion object {
        init {
            System.loadLibrary("filament-jni")
            Utils.init()
        }
    }

    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            Log.d("FilamentView", "doFrame called with frameTimeNanos: $frameTimeNanos")
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


    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d("FilamentView", "surfaceCreated called")

        filament = FilamentHelper(context, holder.surface)
        gestureHandler = GestureHandler(filament)
        initializePlanets()
        choreographer.postFrameCallback(frameCallback)
    }
    private fun initializePlanets() {
        val filament = this.filament ?: return
        filament.loadBackground("sky_background.glb")
        sun812 = filament.addPlanet("Sun.glb", "Sun", 0f, 0f, 0f, 0.1f, 0f, 0.0f, 0.4f)
        mecury812 = filament.addPlanet("Mercury.glb", "Mercury", 2.0f, 0.2056f, 0.5f, 0.05f, 7.0f, 0.0f, 1.0f)
        venus812 = filament.addPlanet("Venus.glb", "Venus", 3.7f, 0.0067f, 0.35f, 0.005f, 3.39f, 177.4f, -1.48f)
        earth812 = filament.addPlanet("Earth.glb", "Earth", 5.0f, 0.0167f, 0.3f, 0.00525f, 0.00005f, 23.44f, 1.0f)
        moon812 = filament.addPlanet("Moon.glb", "Moon", 130.9f, 0.0549f, 2.5f, 12f, 5.14f, 6.68f, 13.36f, parent = earth812)
        mars812 = filament.addPlanet("Mars.glb", "Mars", 7.6f, 0.0934f, 0.33f, 0.371f, 1.85f, 25.19f, 1.02f)
        jupiter812 = filament.addPlanet("Jupiter.glb", "Jupiter", 11f, 0.049f, 2.5f * 0.084f, 0.1f, 1.31f, 3.13f, 2.41f)
        saturn812 = filament.addPlanet("Saturn.glb", "Saturn", 16f, 0.056f, 2.5f * 0.034f, 3f, 2.49f, 26.73f, 2.24f)
        uranus812 = filament.addPlanet("Uranus.glb", "Uranus", 19.22f, 0.046f, 2.5f * 0.012f, 0.001f, 0.77f, 97.77f, 1.41f)
        neptune812 = filament.addPlanet("Neptune.glb", "Neptune", 22f, 0.010f, 2.5f * 0.006f, 0.007f, 1.77f, 28.32f, 1.48f)
    }

    private fun handleSingleTap(x: Float, y: Float) {
        val planets = listOf(sun812, earth812, moon812, mecury812, saturn812, mars812, jupiter812, uranus812, neptune812, venus812)


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
                    targetPlanet = planet


                    post {
                        planetNameTextView?.text = planet.name
                        infoPanel?.visibility = View.VISIBLE
                        miniFilamentHelper?.loadPlanetModel(planet)
                    }
                    return
                }
            }
        }
        targetPlanet = sun812
        post{
            infoPanel?.visibility = View.GONE
            planetNameTextView?.text = ""
            miniFilamentHelper?.clearPlanetModel()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d("FilamentView", "surfaceChanged called: width=$width, height=$height")
        filament?.resize(width,height)
        filament?.let {


            choreographer.removeFrameCallback(frameCallback)


            it.destroySwapChain()
            it.createSwapChain(holder.surface)
            it.resize(width, height)
            it.updateScreenSize(width, height)
            choreographer.postFrameCallback(frameCallback)
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d("FilamentView", "surfaceDestroyed called")
        choreographer.removeFrameCallback(frameCallback)
        filament?.destroy()
        filament = null
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        gestureHandler.handleTouch(event)
        return true
    }
}