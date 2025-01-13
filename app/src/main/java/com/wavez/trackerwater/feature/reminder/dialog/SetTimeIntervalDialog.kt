package com.wavez.trackerwater.feature.reminder.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseBottomSheetFragment
import com.wavez.trackerwater.databinding.DialogSetTimeIntervalBinding

class SetTimeIntervalDialog: BaseBottomSheetFragment<DialogSetTimeIntervalBinding>() {

    companion object{
        fun newInstance(): SetTimeReminderDialog{
            return SetTimeReminderDialog()
        }
    }

    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogSetTimeIntervalBinding {
        return DialogSetTimeIntervalBinding.inflate(layoutInflater)
    }

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
    }

    override fun initListener() {
        super.initListener()
    }

    override fun initObserver() {
        super.initObserver()
    }
}