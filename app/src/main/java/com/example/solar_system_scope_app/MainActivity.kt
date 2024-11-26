package com.example.solar_system_scope_app


import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.filament.utils.Utils

class MainActivity : AppCompatActivity() , PlanetSelectionListener{

    private lateinit var filamentView: FilamentView
    private lateinit var infoPanel: View
    lateinit var planetNameTextView: TextView
    private lateinit var miniPlanetView: SurfaceView
    private lateinit var miniFilamentHelper: MiniFilamentHelper

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
}