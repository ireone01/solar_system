package com.example.solar_system_scope_app


import android.graphics.Canvas
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

import com.google.android.filament.utils.Utils
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar
import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBarWrapper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() , PlanetSelectionListener{

    private lateinit var filamentView: FilamentView
    private lateinit var infoPanel: View
    lateinit var planetNameTextView: TextView
    private lateinit var miniPlanetView: SurfaceView
    private lateinit var miniFilamentHelper: MiniFilamentHelper
    private lateinit var toggleOrbitsButton: ImageButton


    private lateinit var speedSeekBar: SeekBar
    private lateinit var speedTextView: TextView
    private lateinit var barzoomBar : VerticalSeekBarWrapper
    private lateinit var zoomBar : VerticalSeekBar

    private lateinit var realTimeTV : ConstraintLayout
    private lateinit var textYear: TextView
    private lateinit var textMonthDay: TextView
    private lateinit var textHourMinus: TextView

    private lateinit var btn_TgT: Button
    private lateinit var btn_Setting : ImageButton

    private var multiplier: Float = 0.0f
    private var realTimeSeconds = 0L
    private val handler = Handler(Looper.getMainLooper())
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            realTimeSeconds += (1 * multiplier).toLong()
            updateRealTime()
            handler.postDelayed(this, 1000)
        }

    }

    private val secondsInMinute = 60
    private val secondsInHour = 3600
    private val secondsInDay = 86400
    private val secondsInMonth = 2592000
    private val secondsInYear = (365.25 * 86400).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Utils.init()
        System.loadLibrary("filament-jni")
        Log.d("MainActivityxxx", "oncreate ")
        filamentView = findViewById(R.id.solarSystemView)
        infoPanel = findViewById(R.id.infoPanel)
        planetNameTextView = findViewById(R.id.planetName)
        miniPlanetView = findViewById(R.id.miniPlanetView)
        showPlanetNames()
        filamentView.height = getScreenDimensions(this)

        realTimeTV = findViewById(R.id.realTimeTextView)
        textYear = findViewById(R.id.text_year)
        textMonthDay = findViewById(R.id.text_month_day)
        textHourMinus = findViewById(R.id.text_hour_minus)
        updateRealTime()
        handler.post(updateTimeRunnable)


        btn_Setting = findViewById(R.id.btn_setting)
        btn_Setting.setOnClickListener {
            val fragment = FragmentSetting()

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_setting,fragment)
                .addToBackStack(null)
                .commit()
        }
        speedSeekBar = findViewById(R.id.speed_seekbar)
        speedTextView = findViewById(R.id.speed_textview)

        speedSeekBar.progress = 1
        speedSeekBar.max = (365.25*86400).toInt()
        speedSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                multiplier = mapValue(progress)
                filamentView.filament!!.setOrbitSpeedMultiplier(multiplier)
                updateElapsedTime(multiplier)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
        barzoomBar = findViewById(R.id.vertical_seekbar)
        zoomBar = findViewById(R.id.vertical)
        zoomBar.progress = 50
        zoomBar.max = 100
        val centerProgress: Int = zoomBar.max/2
        var isSeeking = false
        var lastProgress = centerProgress
        zoomBar.setOnSeekBarChangeListener(object :OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser && isSeeking) {
                    val delta = progress - centerProgress
                    val scaleFactor = 1+(delta.toFloat()/centerProgress)*0.03f
                    filamentView.filament!!.zoomCamera(scaleFactor)
                    lastProgress = progress
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                isSeeking = true
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                isSeeking = false
                zoomBar.progress =centerProgress
            }

        })

        btn_TgT = findViewById(R.id.btn_TgT)
        btn_TgT.setOnClickListener{
            multiplier =1f
            speedSeekBar.progress = multiplier.toInt()
            filamentView.filament?.let { filamentHelper ->
                filamentHelper.setOrbitSpeedMultiplier(multiplier)
            }
            btn_TgT.animate()
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(100)
                .withEndAction {
                    btn_TgT.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
                .start()

            filamentView.filament?.let { filamentHelper ->
                for(planet in filamentHelper.planets){
                    planet.tempAngle = planet.angle
                    planet.tempRotation = planet.tempRotation
                }
                filamentHelper.render()
            }


            updateElapsedTime(multiplier)
            realTimeSeconds = 0L
            updateRealTime()
        }

        filamentView.setPlanetSelectionListener(this)
        filamentView.setInfoPanel(infoPanel , planetNameTextView)



        miniPlanetView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                Log.d("MainActivityxxx", "surfaceCreated của miniPlanetView được gọi")
                miniFilamentHelper = MiniFilamentHelper(this@MainActivity ,miniPlanetView)
                val width = miniPlanetView.width
                val height = miniPlanetView.height
                miniFilamentHelper.init(width,height)

                filamentView.setMiniFilamentHelper(miniFilamentHelper)


                    miniFilamentHelper.setClinkListener { clickedPlanetName ->
                        var displayedPlanetName  = planetNameTextView.text.toString()
                        if (clickedPlanetName == displayedPlanetName) {
                            if(filamentView.filament!!.currentCameraOffsetX == 0.0f ) {
                                filamentView.switchProjection()
                            }
                            replaceFragmentWithPlanetDetail(clickedPlanetName)

                            Log.d("MainActivityzzzzz", "Hành tinh nhấp ${clickedPlanetName}")

                        } else {

                            Log.d("MainActivityzzzzz", "Hành tinh nhấp không khớp với hành tinh hiển thị")
                        }
                }


            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                Log.d("MainActivityxxx", "surfaceDestroyed của miniPlanetView được gọi")
                miniFilamentHelper.destroy()
            }

        })
    }

    private fun updateRealTime(){
        val currentTime = Calendar.getInstance()


        val minutes = (realTimeSeconds / secondsInMinute).toInt()
        // Cập nhật thời gian thực trong Calendar
        currentTime.add(Calendar.MINUTE, minutes)



        val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val year = yearFormat.format(currentTime.time)

        val monthDayFormat = SimpleDateFormat("dd 'th.' MMM", Locale.getDefault())
        val monthDay = monthDayFormat.format(currentTime.time)

        val hourFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val time = hourFormat.format(currentTime.time)

        textYear.text = year
        textMonthDay.text = monthDay
        textHourMinus.text = time
    }


    private fun updateElapsedTime(multiplier: Float){
        val realTimeSeconds =  multiplier / 1.0f

        val minutes = (realTimeSeconds / secondsInMinute)
        val hours = (realTimeSeconds / secondsInHour)
        val days = (realTimeSeconds / secondsInDay)
        val months  = (realTimeSeconds / secondsInMonth)
        val years = (realTimeSeconds / secondsInYear).toInt()
        var timeDisplay = ""
        if(realTimeSeconds < secondsInMinute){
            timeDisplay = String.format("%.1f s/s", realTimeSeconds)
        }else if(realTimeSeconds < secondsInHour) {
            timeDisplay = String.format("%.1f m/s", minutes)
        }else if(realTimeSeconds < secondsInDay){
            timeDisplay = String.format("%.1f h/s", hours)
        }else if(realTimeSeconds < secondsInMonth){
            timeDisplay = String.format("%.1f d/s", days)
        }else if(realTimeSeconds < secondsInYear && months<=12){
            timeDisplay = String.format("%.1f month/s", months)
        }else{
            timeDisplay = String.format("%d year/s", years)
        }

        speedTextView.text = String.format("Speed: ${timeDisplay}")
    }



    override fun onPlanetSelected(planetName: String) {
        planetNameTextView.text = planetName
        replaceFragmentWithPlanetDetail(planetName)
    }

    fun viewGone(){
        realTimeTV.visibility = View.GONE
        barzoomBar.visibility = View.GONE
        speedSeekBar.visibility = View.GONE
        speedTextView.visibility = View.GONE
        btn_TgT.visibility = View.GONE
        btn_Setting.visibility = View.GONE
    }
    fun viewVisible(){
        realTimeTV.visibility = View.VISIBLE
        barzoomBar.visibility = View.VISIBLE
        speedSeekBar.visibility = View.VISIBLE
        speedTextView.visibility = View.VISIBLE
        btn_TgT.visibility = View.VISIBLE
        btn_Setting.visibility = View.VISIBLE
    }
   private fun replaceFragmentWithPlanetDetail(planetName: String) {
        val fragmentManager = supportFragmentManager
        val fragmentContainer: View = findViewById(R.id.fragment_container)

        if (planetName.isNotEmpty()) {
            if (fragmentContainer.visibility != View.VISIBLE) {
                fragmentContainer.visibility = View.VISIBLE

            }
            viewGone()
            val detailFragment = PlanetDetailFragment()
            val bundle = Bundle()
            bundle.putString("PLANET_NAME", planetName)
            detailFragment.arguments = bundle

            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailFragment, "PLANET_DETAIL_FRAGMENT")
                .commit()
        } else {
            // Xóa fragment khi không có hành tinh nào được chọn
            removePlanetDetailFragment()
            fragmentContainer.visibility = View.GONE
            viewVisible()
        }
        // Cập nhật tên hành tinh trong MainActivity
        planetNameTextView.text = planetName
    }
    fun removePlanetDetailFragment() {
        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment != null) {
            fragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }
            filamentView.filament!!.startCameraOffsetTransition(0f)

    }
    val planetTextViews  = mutableListOf<TextView>()

    val usedYPositions = mutableListOf<Int>()

    fun showPlanetNames() {

        filamentView.filament?.let { filamentHelper ->
            val layout = findViewById<RelativeLayout>(R.id.layout_name_planets)

            for(planet in filamentHelper.planets){
                val screenPosition = filamentHelper.getScreenPosition(planet)
                if(screenPosition != null && planet.parent==null) {
                    var textView: TextView? = planetTextViews.find { it.tag == planet.name }
                    if(textView == null){
                        textView = TextView(this)
                        textView.text = planet.name
                        textView.tag = planet.name
                        textView.setTextColor(Color.WHITE)
                        textView.setTextSize(16f)
                        textView.setTypeface(null, android.graphics.Typeface.BOLD)

                        val layoutParams = RelativeLayout.LayoutParams(
                          RelativeLayout.LayoutParams.WRAP_CONTENT,
                           70
                        )

                        var newYPosition = (screenPosition.y + 20).toInt()
                        if(planet.name == "Sun"){
                            newYPosition += 30
                        }
                        usedYPositions.add(newYPosition)

                        layoutParams.leftMargin = screenPosition.x.toInt()
                        layoutParams.topMargin = newYPosition

                        textView.layoutParams = layoutParams
                        textView.requestLayout()

                        layout.addView(textView)
                        planetTextViews.add(textView)
                        Log.d("PlanetNames", "Position of ${planet.name}: x = ${screenPosition.x}, y = ${screenPosition.y}")

                    }else{
                        val layoutParams = textView.layoutParams as RelativeLayout.LayoutParams


                        var newYPosition = (screenPosition.y + 20).toInt()


                        while (usedYPositions.contains(newYPosition)) {
                            newYPosition += 20
                        }
                        layoutParams.leftMargin = screenPosition.x.toInt()
                        layoutParams.topMargin = newYPosition
                        textView.layoutParams = layoutParams
                        textView.requestLayout()



                    }
                }
            }
        }

    }
    fun getScreenDimensions(activity: MainActivity): Pair<Float, Float> {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        val xdpi = displayMetrics.xdpi
        val ydpi = displayMetrics.ydpi

        // Chuyển đổi từ pixel sang cm
        val widthCm = (width/ xdpi) * 2.54f
        val heightCm = (height / ydpi) * 2.54f

        return Pair(widthCm, heightCm)
    }


    private fun mapValue(progress: Int): Float{
        return when {
            progress <=  (365.25*86400)/2 -> (progress*2/(365.25)).toFloat()
            else -> (progress- ((365.25*86400)/2).toFloat())*((364.25*2/365.25).toFloat())+86400
        }
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(updateTimeRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTimeRunnable)

        if (::miniFilamentHelper.isInitialized) {
            miniFilamentHelper.destroy()
        }

        FilamentManager.destroy()
    }

}