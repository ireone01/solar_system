package com.example.solar_system_scope_app

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import kotlin.math.max
import kotlin.math.min

class MyGLSurfaceView(context: Context) : GLSurfaceView(context) ,
    ScaleGestureDetector.OnScaleGestureListener {

    private val renderer: MyGLRenderer
    private val gestureDetector: GestureDetector
    private val scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f

    init {
        // Thiết lập phiên bản OpenGL ES 2.0
        setEGLContextClientVersion(2)

        renderer = MyGLRenderer()
        // Đặt Renderer để vẽ trên GLSurfaceView
        setRenderer(renderer)

        // GestureDetector để phát hiện thao tác vuốt
        gestureDetector = GestureDetector(context, GestureListener())
        scaleGestureDetector = ScaleGestureDetector(context,this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        gestureDetector.onTouchEvent(event)
        scaleGestureDetector.onTouchEvent(event)
        return true
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {


        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            renderer.setAngle(-distanceX / 2f , -distanceY / 2f)
            requestRender()
            return true
        }
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        scaleFactor *= detector.scaleFactor

        scaleFactor = max(0.1f, min(scaleFactor,5.0f))

        renderer.setScaleFactor(scaleFactor)
        requestRender()
        return true
    }

    override fun onScaleBegin(p0: ScaleGestureDetector): Boolean {
      return true
    }

    override fun onScaleEnd(p0: ScaleGestureDetector) {

    }
}
