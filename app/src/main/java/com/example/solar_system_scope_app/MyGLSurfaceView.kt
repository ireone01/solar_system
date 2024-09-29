package com.example.solar_system_scope_app

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.GestureDetector
import android.view.MotionEvent
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: MyGLRenderer
    private val gestureDetector: GestureDetector

    init {
        // Thiết lập phiên bản OpenGL ES 2.0
        setEGLContextClientVersion(2)

        renderer = MyGLRenderer()
        // Đặt Renderer để vẽ trên GLSurfaceView
        setRenderer(renderer)

        // GestureDetector để phát hiện thao tác vuốt
        gestureDetector = GestureDetector(context, GestureListener())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            gestureDetector.onTouchEvent(it)
        }
        return true
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        private var previousX: Float = 0f
        private var previousY: Float = 0f

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            val deltaX = e2?.x?.minus(previousX) ?: 0f
            val deltaY = e2?.y?.minus(previousY) ?: 0f
            renderer.setAngle(deltaX / 2f, deltaY / 2f) // Cập nhật góc xoay

            previousX = e2?.x ?: 0f
            previousY = e2?.y ?: 0f
            return true
        }
    }
}
