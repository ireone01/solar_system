package com.example.solar_system_scope_app

import android.util.Log
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Effectmanager(private val filamentHelper: FilamentHelper) {
    private var originalCameraRotationX: Float = 0f
    private var originalCameraRotationY: Float = 0f
    private var originalScales: Map<Planet , Float> = emptyMap()
    private var originalCameraDistance :  Float = 0f

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    fun activateEffect() {
        if (!access) {
            access = true
            Log.d("access!!!!!", "${access} thay doi ")

            filamentHelper.targetPlanet?.let { targetPlanet ->
                originalCameraDistance = filamentHelper.cameraDistance
                originalCameraRotationX = filamentHelper.cameraRotationX
                originalCameraRotationY = filamentHelper.cameraRotationY

                originalScales = filamentHelper.planets.associateWith { it.scale }

                filamentHelper.removeOrbits()

                val childPlanets = filamentHelper.getChildPlanets(targetPlanet)


                filamentHelper.planets.forEach { planet ->
                    if (planet != targetPlanet && !childPlanets.contains(planet) ) {
                        filamentHelper.scalePlanet(planet, planet.scale / 1000f)
                        planet.dirtyFlag = true
                    } else {
                        if (planet == targetPlanet) {
                            planet.orbitSpeed /= 1000f
                        } else {
                            planet.orbitSpeed /= 2f
                        }
                    }


                }
                val newCameraRotationX = 40f
                val newCameraRotationY = 150f
                val newCameradistance = 4f

                scope.launch {
                    animateCamera(
                        startDistance = filamentHelper.cameraDistance,
                        endDistance = newCameradistance,
                        startRotationX = filamentHelper.cameraRotationX,
                        endRotationX = newCameraRotationX,
                        startRotationY = filamentHelper.cameraRotationY,
                        endRotationY = newCameraRotationY,
                        duration = 1000L
                    )
                }

            }

        }
    }
    fun deactivateEffect() {


        filamentHelper.reloadOrbits()

        filamentHelper.planets.forEach{ planet ->

            if(planet.dirtyFlag) {
                filamentHelper.scalePlanet(planet, planet.scale * 1000f)
                planet.dirtyFlag = false
            }else{
                if(planet.parent == null) {
                    planet.orbitSpeed *= 1000f
                }else{
                    planet.orbitSpeed *= 2f
                }
            }
            }
            scope.launch {
            animateCamera(
                startDistance = filamentHelper.cameraDistance,
                endDistance = 25f,
                startRotationX = filamentHelper.cameraRotationX,
                endRotationX = 12f,
                startRotationY = filamentHelper.cameraRotationY,
                endRotationY = originalCameraRotationY,
                duration = 1000L
            )
        }

    }
    private suspend fun animateCamera(
        startDistance: Float ,
         endDistance: Float,
        startRotationX: Float,
        endRotationX: Float,
        startRotationY: Float,
        endRotationY: Float,
        duration: Long

    ){
        val frameRate = 60
        val totalFrames = (duration / 1000.0 * frameRate).toInt()
        for (frame in 0..totalFrames) {
            val progress = frame.toFloat() / totalFrames
            val easedProgress = easeInOutQuad(progress)

            // Tính toán giá trị mới
            filamentHelper.cameraDistance = lerp(startDistance, endDistance, easedProgress)
            filamentHelper.cameraRotationX = lerp(startRotationX, endRotationX, easedProgress)
            filamentHelper.cameraRotationY = lerp(startRotationY, endRotationY, easedProgress)

            // Cập nhật camera
            filamentHelper.updateCameraTransform()

            // Chờ đến frame tiếp theo
            delay((duration / totalFrames).toLong())
        }
    }
    // Hàm easing để tạo chuyển động mượt mà
    private fun easeInOutQuad(t: Float): Float {
        return if (t < 0.5f) {
            2f * t * t
        } else {
            -1f + (4f - 2f * t) * t
        }
    }

    // Hàm nội suy tuyến tính
    private fun lerp(start: Float, end: Float, fraction: Float): Float {
        return start + (end - start) * fraction
    }

    fun cleanup() {
        job.cancel()
    }
}