package com.wavez.trackerwater

import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.databinding.ActivityMeBinding

class ActivityMe : BaseActivity<ActivityMeBinding>() {
    override fun createBinding(): ActivityMeBinding {
        return ActivityMeBinding.inflate(layoutInflater)
    }

}