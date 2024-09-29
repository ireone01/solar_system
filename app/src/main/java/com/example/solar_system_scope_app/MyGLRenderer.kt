package com.example.solar_system_scope_app

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer : GLSurfaceView.Renderer {

    private lateinit var axis: Axis
    private val rotationMatrix = FloatArray(16)
    private var angleX: Float = 0f
    private var angleY: Float = 0f

    init {
        // Khởi tạo ma trận xoay mặc định
        Matrix.setIdentityM(rotationMatrix, 0)
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        // Thiết lập màu nền
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // Tạo đối tượng trục Oxyz
        axis = Axis()
    }

    override fun onDrawFrame(gl: GL10) {
        // Xóa màn hình
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // Tính toán ma trận xoay dựa trên góc xoay
        val scratch = FloatArray(16)
        val tempMatrix = FloatArray(16)

        // Xoay theo trục X
        Matrix.setRotateM(rotationMatrix, 0, angleX, 1.0f, 0.0f, 0.0f)
        // Xoay theo trục Y
        Matrix.setRotateM(tempMatrix, 0, angleY, 0.0f, 1.0f, 0.0f)

        // Nhân 2 ma trận lại để lấy ma trận xoay hoàn chỉnh
        Matrix.multiplyMM(scratch, 0, rotationMatrix, 0, tempMatrix, 0)

        // Áp dụng ma trận xoay trước khi vẽ trục
        axis.draw(scratch)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()

        // Thiết lập camera
        val projectionMatrix = FloatArray(16)
        val viewMatrix = FloatArray(16)

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // Tính toán ma trận kết quả (view * projection)
        val vpMatrix = FloatArray(16)
        Matrix.multiplyMM(vpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Truyền ma trận vp vào đối tượng axis để áp dụng xoay
        axis.setVPMatrix(vpMatrix)
    }

    fun setAngle(deltaX: Float, deltaY: Float) {
        angleX += deltaY // Điều chỉnh xoay theo trục X
        angleY += deltaX // Điều chỉnh xoay theo trục Y
    }
}
