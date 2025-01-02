package com.wavez.trackerwater

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.databinding.ActivityMainBinding

class MainActivity: BaseActivity<ActivityMainBinding>() {
    private lateinit var  handler: Handler
    private var progress : Int = 0
    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
    override fun createBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {
        super.initListener()
//
//        handler = Handler(Looper.getMainLooper())
//        handler.postDelayed(object : Runnable{
//            override fun run() {
//                progress +=5
//                if(progress >100) progress = 0
//                val binding = findViewById<WaveLoadingView>(R.id.waveLoadingView)
//                binding.setProgressValue(progress)
//                handler.postDelayed(this ,1000)
//            }
//
//        },1000)

    }
}
