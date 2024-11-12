package com.example.solar_system_scope_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.filament.utils.Utils

class MainActivity : AppCompatActivity() {

    private lateinit var filamentView: FilamentView
    private lateinit var infoPanel: View
    private lateinit var planetNameTextView: TextView
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


        filamentView.setInfoPanel(infoPanel , planetNameTextView)

        miniPlanetView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                Log.d("MainActivityxxx", "surfaceCreated của miniPlanetView được gọi")
                miniFilamentHelper = MiniFilamentHelper(this@MainActivity ,miniPlanetView)
                val width = miniPlanetView.width
                val height = miniPlanetView.height
                miniFilamentHelper.init(width,height)

                filamentView.setMiniFilamentHelper(miniFilamentHelper)
                miniFilamentHelper.setClinkListener { planetName ->
                    var namePlanet = planetNameTextView.text
                    val fragmentContainer : View = findViewById(R.id.fragment_container)
                    fragmentContainer.visibility = View.VISIBLE
                    // Tạo một đối tượng Fragment mới
                    val detailFragment = PlanetDetailFragment()

                    // Truyền dữ liệu về hành tinh
                    val bundle = Bundle()
                    bundle.putString("PLANET_NAME", namePlanet.toString())
                    detailFragment.arguments = bundle

                    // Thay thế Fragment hiện tại bằng PlanetDetailFragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, detailFragment) // `fragment_container` là id của ViewGroup nơi bạn muốn thay thế Fragment
                        .addToBackStack(null) // Cho phép quay lại Fragment trước đó
                        .commit()
                    planetNameTextView.text = ""
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


}