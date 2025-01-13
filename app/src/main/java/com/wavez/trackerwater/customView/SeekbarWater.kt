package com.wavez.trackerwater.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.SeekBar

class SeekbarWater(context: Context, attrs: AttributeSet) : SeekBar(context, attrs) {

    private val paint = Paint().apply {
        color = Color.BLACK
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val thumbX = thumb.bounds.centerX().toFloat()
        val thumbY = thumb.bounds.centerY().toFloat()

        // Text to display
        val progressText = progress.toString()
        canvas.drawText(progressText, thumbX, thumbY - 20, paint)
    }
}