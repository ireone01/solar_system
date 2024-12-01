package com.example.solar_system_scope_app


import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.google.android.filament.utils.Utils
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


    private lateinit var textYear: TextView
    private lateinit var textMonthDay: TextView
    private lateinit var textHourMinus: TextView

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


        textYear = findViewById(R.id.text_year)
        textMonthDay = findViewById(R.id.text_month_day)
        textHourMinus = findViewById(R.id.text_hour_minus)
        updateRealTime()
        handler.post(updateTimeRunnable)

        toggleOrbitsButton = findViewById(R.id.btn_orbit)
        toggleOrbitsButton.setOnClickListener{
            filamentView.filament?.let { filamentHelper ->
                val currentlyVisible = filamentHelper.areOrbitVisible()
                filamentHelper.setOrbitsVisible(!currentlyVisible)

             }
            toggleOrbitsButton.animate()
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(100)
                .withEndAction {
                    toggleOrbitsButton.animate()
                        .scaleX(1.0f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
                .start()
        }

        speedSeekBar = findViewById(R.id.speed_seekbar)
        speedTextView = findViewById(R.id.speed_textview)
        speedSeekBar.progress = 0
        speedSeekBar.max = (365.25*86400).toInt()
        speedSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                multiplier = progress/1f
                filamentView.filament!!.setOrbitSpeedMultiplier(multiplier)
                updateElapsedTime(multiplier)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })


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

                miniFilamentHelper.setClinkListener {
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


            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

            }

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

   private fun replaceFragmentWithPlanetDetail(planetName: String) {
        val fragmentManager = supportFragmentManager
        val fragmentContainer: View = findViewById(R.id.fragment_container)

        if (planetName.isNotEmpty()) {
            // Hiển thị fragment_container nếu nó đang ẩn
            if (fragmentContainer.visibility != View.VISIBLE) {
                fragmentContainer.visibility = View.VISIBLE
            }

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

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(updateTimeRunnable)
    }
}