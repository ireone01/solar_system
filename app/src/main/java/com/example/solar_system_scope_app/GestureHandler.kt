package com.example.solar_system_scope_app

import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView.GONE
import android.view.View
import com.example.solar_system_scope_app.UI.activity.MainActivity
import com.example.solar_system_scope_app.model.Planet
import kotlin.math.pow
import kotlin.math.sqrt

class GestureHandler(private val filament: FilamentHelper?,
    private val onPlanetSelected :(Planet) -> Unit ,
    private val onNoPlanetSelected: () -> Unit
) {
    private var lastX = 0f
    private var lastY = 0f
    private var lastDistance = 0f
    private var isPinching = false

    fun handleTouch(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
                isPinching = false
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount == 2) {
                    lastDistance = calculateDistance(event)
                    isPinching = true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isPinching && event.pointerCount == 2) {
                    val newDistance = calculateDistance(event)
                    val scaleFactor = newDistance / lastDistance

                    // Limit scale factor to prevent jumpy zoom
                    val clampedScaleFactor = scaleFactor.coerceIn(0.95f, 1.05f)
                    filament?.zoomCamera(clampedScaleFactor)
                    lastDistance = newDistance
                } else if (!isPinching) {
                    val deltaX = event.x - lastX
                    val deltaY = event.y - lastY
                    filament?.rotateCamera(deltaX, deltaY)
                    lastX = event.x
                    lastY = event.y
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                isPinching = false
            }
        }
        return true
    }
    fun handleSingleTap(x: Float, y: Float, planets : List<Planet>) {
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
                val touchThreshold = 120f

                if (distance < touchThreshold) {

                    onPlanetSelected(planet)
                    return
                }
            }
        }

        onNoPlanetSelected()
    }


    private fun calculateDistance(event: MotionEvent): Float {
        val dx = event.getX(1) - event.getX(0)
        val dy = event.getY(1) - event.getY(0)
        return sqrt((dx * dx + dy * dy))
    }
}
