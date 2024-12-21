package com.example.solar_system_scope_app.model

import android.content.Context
import android.media.MediaPlayer
import com.example.solar_system_scope_app.R

object AudioManager {
    private var mediaPlayer: MediaPlayer? = null
    fun start(context: Context){
        if(mediaPlayer ==null){
            mediaPlayer = MediaPlayer.create(context, R.raw.videoplayback).apply {
                isLooping = true
                setVolume(0.3f, 0.3f)
                start()
            }
        }else if(!mediaPlayer!!.isPlaying){
            mediaPlayer!!.start()
        }
    }
    fun stop() {
        mediaPlayer?.let {
            if(it.isPlaying){
                it.stop()
                it.release()
                mediaPlayer = null
            }
        }
    }
    fun pause(){
        mediaPlayer?.let {
            if(it.isPlaying){
                it.pause()
            }
        }
    }
    fun isPlaying():Boolean {
        return mediaPlayer?.isPlaying == true
    }
}