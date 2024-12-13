package com.example.solar_system_scope_app.model

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer

object DataManager {
    private val planetBuffers : MutableMap<String, ByteBuffer?> = mutableMapOf()

    fun setPlanetBuffer(fileName: String , buffer: ByteBuffer?){
        planetBuffers[fileName] = buffer
    }

    fun getPlanetBuffer(fileName: String) : ByteBuffer? {
        return planetBuffers[fileName]
    }


        suspend fun loadAllPlanets(context: Context) = withContext(Dispatchers.IO){
            try {
                val planets = listOf(
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

                for(planet in planets){
                    val buffer = readAsset(context,planet)
                    if(buffer !=null) {
                        setPlanetBuffer(planet, buffer)
                        Log.d("FilamentHelperbuffer", "Đã tải buffer cho $planet")

                    }else{
                        Log.e("FilamentHelperbuffer", "Không thể tải buffer cho $planet")

                    }
                }
            }catch (e:Exception){
                Log.e("FilamentHelper", "Error loading planet buffers: ${e.message}", e)
            }
        }
        fun readAsset(context: Context, fileName: String): ByteBuffer {
            context.assets.open(fileName).use { input ->
                val bytes = input.readBytes()
                val buffer = ByteBuffer.allocateDirect(bytes.size)
                buffer.put(bytes)
                buffer.flip()
                return buffer
            }
        }


    fun clearPlanetBuffers() {
        planetBuffers.clear()
    }
}