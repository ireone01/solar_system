package com.example.solar_system_scope_app

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Axis {

    private val vertexShaderCode =
        """
        uniform mat4 uMVPMatrix;
        attribute vec4 vPosition;
        void main() {
            gl_Position = uMVPMatrix * vPosition;
        }
        """.trimIndent()

    private val fragmentShaderCode =
        """
        precision mediump float;
        uniform vec4 vColor;
        void main() {
            gl_FragColor = vColor;
        }
        """.trimIndent()

    private val axisCoords = floatArrayOf(
        // Trục X (Đỏ)
        -0.5f, 0.0f, 0.0f,  // Điểm đầu
        0.5f, 0.0f, 0.0f,   // Điểm cuối

        // Trục Y (Xanh lá)
        0.0f, -0.5f, 0.0f,  // Điểm đầu
        0.0f, 0.5f, 0.0f,   // Điểm cuối

        // Trục Z (Xanh dương)
        0.0f, 0.0f, -0.5f,  // Điểm đầu
        0.0f, 0.0f, 0.5f    // Điểm cuối
    )

    private val vertexBuffer: FloatBuffer =
        // Chuẩn bị buffer để chứa tọa độ
        ByteBuffer.allocateDirect(axisCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(axisCoords)
                position(0)
            }
        }

    private val program: Int
    private var vpMatrixHandle: Int = 0

    init {
        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // Tạo chương trình OpenGL
        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }
    }

    fun draw(mvpMatrix: FloatArray) {
        GLES20.glUseProgram(program)

        GLES20.glLineWidth(5.0f) // Bạn có thể điều chỉnh độ dày của trục tại đây

        val positionHandle = GLES20.glGetAttribLocation(program, "vPosition")
        GLES20.glEnableVertexAttribArray(positionHandle)

        GLES20.glVertexAttribPointer(
            positionHandle,
            3,
            GLES20.GL_FLOAT,
            false,
            3 * 4,
            vertexBuffer
        )

        vpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix")
        GLES20.glUniformMatrix4fv(vpMatrixHandle, 1, false, mvpMatrix, 0)

        val colorHandle = GLES20.glGetUniformLocation(program, "vColor")

        // Vẽ trục X (màu đỏ)
        GLES20.glUniform4fv(colorHandle, 1, floatArrayOf(1.0f, 0.0f, 0.0f, 1.0f), 0)
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, 2)

        // Vẽ trục Y (màu xanh lá)
        GLES20.glUniform4fv(colorHandle, 1, floatArrayOf(0.0f, 1.0f, 0.0f, 1.0f), 0)
        GLES20.glDrawArrays(GLES20.GL_LINES, 2, 2)

        // Vẽ trục Z (màu xanh dương)
        GLES20.glUniform4fv(colorHandle, 1, floatArrayOf(0.0f, 0.0f, 1.0f, 1.0f), 0)
        GLES20.glDrawArrays(GLES20.GL_LINES, 4, 2)

        GLES20.glDisableVertexAttribArray(positionHandle)
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }
}
