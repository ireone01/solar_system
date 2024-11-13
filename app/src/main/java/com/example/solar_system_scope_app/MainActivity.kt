package com.example.solar_system_scope_app


import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.filament.utils.Utils

class MainActivity : AppCompatActivity() , PlanetSelectionListener{

    private lateinit var filamentView: FilamentView
    private lateinit var infoPanel: View
    lateinit var planetNameTextView: TextView
    private lateinit var miniPlanetView: SurfaceView
    private lateinit var miniFilamentHelper: MiniFilamentHelper
    private var namePlanet : String? = null

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
                miniFilamentHelper.setClinkListener { planetName ->
                    var namePlanet = planetNameTextView.text
                   replaceFragmentWithPlanetDetail(namePlanet.toString())
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
        replaceFragmentWithPlanetDetail(planetName)
    }

    override fun onPlanetDataChanged(planetName: String) {
        Log.d("MainActivityxxx", "onPlanetDataChanged called with planet: $planetName")
        replaceFragmentWithPlanetDetail(planetName)
    }

    fun replaceFragmentWithPlanetDetail(planetName: String) {
        Log.d("MainActivityxxx", "replaceFragmentWithPlanetDetail called with planet: $planetName")
        val fragmentManager = supportFragmentManager
        val fragmentContainer: View = findViewById(R.id.fragment_container)

        if (planetName.isNotEmpty()) {
            // Hiển thị fragment_container nếu nó đang ẩn
            if (fragmentContainer.visibility != View.VISIBLE) {
                fragmentContainer.visibility = View.VISIBLE
            }

            // Tìm fragment hiện tại theo tag
            val existingFragment = fragmentManager.findFragmentByTag("PLANET_DETAIL_FRAGMENT")

            if (existingFragment is PlanetDetailFragment) {
                // Nếu fragment đã tồn tại và đang hiển thị, cập nhật dữ liệu
                existingFragment.updatePlanetName(planetName)
                Log.d("MainActivityxxx", "Updated existing PlanetDetailFragment with planet: $planetName")
            } else {
                // Nếu fragment chưa tồn tại, tạo và hiển thị fragment mới
                val detailFragment = PlanetDetailFragment()
                val bundle = Bundle()
                bundle.putString("PLANET_NAME", planetName)
                detailFragment.arguments = bundle

                fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, detailFragment, "PLANET_DETAIL_FRAGMENT")
                    .addToBackStack(null)
                    .commit()
                Log.d("MainActivityxxx", "Replaced fragment_container with new PlanetDetailFragment for planet: $planetName")
            }

            // Cập nhật tên hành tinh trong MainActivity
            planetNameTextView.text = planetName
        } else {
            // Nếu không có hành tinh nào được chọn, ẩn fragment_container
            fragmentContainer.visibility = View.GONE
            planetNameTextView.text = ""
        }
    }
}