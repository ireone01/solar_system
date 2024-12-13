package com.example.solar_system_scope_app.UI.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.solar_system_scope_app.model.DataManager
import com.example.solar_system_scope_app.FilamentManager
import com.example.solar_system_scope_app.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean


class LoadingActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var loadingTextView: TextView
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main +job)

    private val filamentInitialized = AtomicBoolean(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        progressBar = findViewById(R.id.progressBar)
        loadingTextView = findViewById(R.id.loadingTextView)
        val hiddenSurfaceView = findViewById<SurfaceView>(R.id.hiddenSurfaceView)
        
        hiddenSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                Log.d("LoadingActivity", "Hidden SurfaceView surfaceCreated")
                FilamentManager.initialize(this@LoadingActivity, holder.surface)
                filamentInitialized.set(true)
                checkAndNavigate()
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
            override fun surfaceDestroyed(holder: SurfaceHolder) {}
        })




        loadData()
    }

    private fun loadData(){
        scope.launch {
            try {
                loadingTextView.text = "Đang tải dữ liệu..."

                withContext(Dispatchers.IO){
                    DataManager.loadAllPlanets(this@LoadingActivity)
                }
                val requiredFiles = listOf(
                    "Sun.glb",
                    "Mercury.glb",
                    "Venus.glb",
                    "Earth.glb",
                    "Moon.glb",
                    "Mars.glb",
                    "Jupiter.glb",
                    "Saturn.glb",
                    "Uranus.glb",
                    "Neptune.glb"
                )
                val allLoaded = requiredFiles.all { file ->
                    DataManager.getPlanetBuffer(file) !=null
                }
                if(allLoaded) {
                   navigateToStartActivity()
                }else{
                    loadingTextView.text = "Error loading $allLoaded"
                }
            }catch (e: Exception){
                e.printStackTrace()
                loadingTextView.text = "Error Loading"
            }
        }
    }
    private fun checkAndNavigate(){
        if(filamentInitialized.get() && dataLoaded()){
            navigateToStartActivity()
        }
    }

    private fun dataLoaded(): Boolean {
        val requiredFiles = listOf(
            "Sun.glb",
            "Mercury.glb",
            "Venus.glb",
            "Earth.glb",
            "Moon.glb",
            "Mars.glb",
            "Jupiter.glb",
            "Saturn.glb",
            "Uranus.glb",
            "Neptune.glb"
        )
        return requiredFiles.all { file ->
            DataManager.getPlanetBuffer(file) != null
        }
    }

    private fun navigateToStartActivity(){
        val intent = Intent(this@LoadingActivity , StartActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}