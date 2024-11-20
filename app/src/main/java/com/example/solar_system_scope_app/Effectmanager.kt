package com.example.solar_system_scope_app

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


    fun activateEffect(){
        filamentHelper.targetPlanet?.let { targetPlanet ->
            originalCameraDistance = filamentHelper.cameraDistance
            originalCameraRotationX = filamentHelper.cameraRotationX
            originalCameraRotationY = filamentHelper.cameraRotationY

            originalScales = filamentHelper.getPlanets().associateWith { it.scale }

            filamentHelper.removeOrbits()

            filamentHelper.getPlanets().forEach{ planet ->
                if(planet != targetPlanet) {
                    filamentHelper.scalePlanet(planet , planet.scale / 1000f)
                }

            }

            val newCameraRotationX = 0f
            val newCameraRotationY = 90f
            val newCameradistance = 10f

            scope.launch {
                animateCamera(startDistance = filamentHelper.cameraDistance,
                    endDistance = newCameradistance,
                    startRotationX =  filamentHelper.cameraRotationX,
                    endRotationX =  newCameraRotationX,
                    startRotationY =  filamentHelper.cameraRotationY,
                    endRotationY =  newCameraRotationY,
                    duration = 1000L
                    )
            }

        }

    }
    fun deactivateEffect() {
        filamentHelper.reloadOrbits()

        originalScales.forEach{ (planet, originalScale) ->
            filamentHelper.scalePlanet(planet , originalScale)
        }
        scope.launch {
            animateCamera(
                startDistance = filamentHelper.cameraDistance,
                endDistance = originalCameraDistance,
                startRotationX = filamentHelper.cameraRotationX,
                endRotationX = originalCameraRotationX,
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