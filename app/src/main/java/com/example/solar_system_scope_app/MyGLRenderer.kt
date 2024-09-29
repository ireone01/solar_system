package com.example.solar_system_scope_app

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer : GLSurfaceView.Renderer {

    private lateinit var axis: Axis

    // Biến lưu trữ các góc xoay
    private var angleX: Float = 0f
    private var angleY: Float = 0f

    // Biến lưu trữ scaleFactor
    private var scaleFactor = 1.0f

    // Ma trận cho các phép biến đổi
    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val mvpMatrix = FloatArray(16)
    private val tempMatrix = FloatArray(16)

    init {
        // Khởi tạo ma trận mô hình thành ma trận đơn vị
        Matrix.setIdentityM(modelMatrix, 0)
    }

    fun setScaleFactor(scale: Float) {
        scaleFactor = scale
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        // Thiết lập màu nền
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // Bật tính năng depth test
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        // Tạo đối tượng trục Oxyz
        axis = Axis()
    }

    override fun onDrawFrame(gl: GL10) {
        // Xóa màn hình và buffer depth
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // Đặt lại ma trận mô hình
        Matrix.setIdentityM(modelMatrix, 0)

        // Áp dụng phép thu phóng
        Matrix.scaleM(modelMatrix, 0, scaleFactor, scaleFactor, scaleFactor)

        // Áp dụng phép xoay theo trục X và Y
        Matrix.rotateM(modelMatrix, 0, angleX, 1f, 0f, 0f)
        Matrix.rotateM(modelMatrix, 0, angleY, 0f, 1f, 0f)

        // Tính toán ma trận MVP = Projection * View * Model
        Matrix.multiplyMM(tempMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, tempMatrix, 0)

        // Vẽ trục với ma trận MVP
        axis.draw(mvpMatrix)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()

        // Thiết lập ma trận chiếu
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 10f)

        // Thiết lập ma trận view
        Matrix.setLookAtM(
            viewMatrix, 0,
            0f, 0f, -5f,   // Vị trí camera
            0f, 0f, 0f,    // Nhìn vào đâu
            0f, 1.0f, 0.0f // Hướng lên trên của camera
        )
    }

    fun setAngle(deltaX: Float, deltaY: Float) {
        angleX += deltaY // Điều chỉnh xoay theo trục X
        angleY += deltaX // Điều chỉnh xoay theo trục Y
    }
}
