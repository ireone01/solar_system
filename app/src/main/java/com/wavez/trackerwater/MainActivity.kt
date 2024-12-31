package com.wavez.trackerwater

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.databinding.ActivityMainBinding

class MainActivity: BaseActivity<ActivityMainBinding>() {

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
    }
}
