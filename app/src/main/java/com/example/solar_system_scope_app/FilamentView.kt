package com.example.solar_system_scope_app

import android.content.Context
import android.util.Log
import android.view.Choreographer
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class FilamentView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private var filament: FilamentHelper? = null

    private val choreographer = Choreographer.getInstance()

    private var lastX = 0f
    private var lastY = 0f

    private var lastDistance = 0f
    private var isPinching = false


    private val frameCallback = object : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            Log.d("FilamentView", "doFrame called with frameTimeNanos: $frameTimeNanos")
            filament?.render()
            choreographer.postFrameCallback(this)
        }
    }

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d("FilamentView", "surfaceCreated called")
        filament = FilamentHelper(context, holder.surface)
        filament?.loadGlb("ringed_gas_giant_planet.glb")
        choreographer.postFrameCallback(frameCallback)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d("FilamentView", "surfaceChanged called: width=$width, height=$height")
        filament?.let {

            choreographer.removeFrameCallback(frameCallback)

            it.destroySwapChain()
            it.createSwapChain(holder.surface)
            it.resize(width, height)

            choreographer.postFrameCallback(frameCallback)
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d("FilamentView", "surfaceDestroyed called")
        choreographer.removeFrameCallback(frameCallback)
        filament?.destroy()
        filament = null
    }
    private fun calculateDistance(event: MotionEvent):Float {
        val dx = event.getX(1) - event.getX(0)
        val dy = event.getY(1) - event.getY(0)
        return Math.sqrt((dx * dx + dy * dy ).toDouble()).toFloat()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
                isPinching = false
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                if(event.pointerCount == 2) {
                    lastDistance = calculateDistance(event)
                    isPinching = true
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if(isPinching && event.pointerCount == 2) {
                    val newDistance = calculateDistance(event)
                    val scaleFactor = newDistance / lastDistance

                    // Giới hạn scaleFactor để ngăn nhảy đột ngột
                    val minScaleFactor = 0.95f
                    val maxScaleFactor = 1.05f
                    val clampedScaleFactor = Math.max(minScaleFactor, Math.min(scaleFactor, maxScaleFactor))

                    filament?.scaleModel(clampedScaleFactor)
                    lastDistance = newDistance
                } else if(!isPinching) {
                    val deltaX = event.x - lastX
                    val deltaY = event.y - lastY
                    filament?.rotateModel(deltaX, deltaY)
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

}
