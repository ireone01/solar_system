package com.example.solar_system_scope_app

import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class StartActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)

        val startButton : Button = findViewById(R.id.btn_start)
        val bounceAnimation : Animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        startButton.startAnimation(bounceAnimation)

        startButton.setOnClickListener{
            val intent = Intent(this@StartActivity , MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}