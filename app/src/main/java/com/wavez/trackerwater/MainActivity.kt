package com.wavez.trackerwater

import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.databinding.ActivityMainBinding

class MainActivity: BaseActivity<ActivityMainBinding>() {
    override fun createBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }
}