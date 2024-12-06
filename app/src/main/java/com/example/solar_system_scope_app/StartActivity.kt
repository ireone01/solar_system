package com.example.solar_system_scope_app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity(){
    private lateinit var videoView: VideoView
    private lateinit var startButton: Button
    private lateinit var bounceAnimation: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)

        videoView = findViewById(R.id.backgroundVideoView)
        val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.video_start}")
        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            videoView.start()
        }

         startButton  = findViewById(R.id.btn_start)
         bounceAnimation  = AnimationUtils.loadAnimation(this, R.anim.bounce)
        startButton.startAnimation(bounceAnimation)

        startButton.setOnClickListener{
            startButton.animate()
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(100)
                .withEndAction {
                    startButton.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
                .start()
            val intent = Intent(this@StartActivity , MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
    }

}