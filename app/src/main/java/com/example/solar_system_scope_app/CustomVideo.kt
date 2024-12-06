package com.example.solar_system_scope_app

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView


class CustomVideoView : VideoView {
    private var videoWidth = 0
    private var videoHeight = 0

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )


    fun setVideoDimensions(width: Int, height: Int) {
        this.videoWidth = width
        this.videoHeight = height
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (videoWidth == 0 || videoHeight == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        var viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        var viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        val videoProportion = videoWidth.toFloat() / videoHeight.toFloat()
        val screenProportion = viewWidth.toFloat() / viewHeight.toFloat()

        if (videoProportion > screenProportion) {
            viewHeight = (viewWidth / videoProportion).toInt()
        } else {
            viewWidth = (viewHeight * videoProportion).toInt()
        }

        setMeasuredDimension(viewWidth, viewHeight)
    }
}
