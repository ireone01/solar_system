package com.example.solar_system_scope_app

import android.view.MotionEvent
import kotlin.math.sqrt

class GestureHandler(private val filament: FilamentHelper?) {
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

    private fun calculateDistance(event: MotionEvent): Float {
        val dx = event.getX(1) - event.getX(0)
        val dy = event.getY(1) - event.getY(0)
        return sqrt((dx * dx + dy * dy))
    }
}
