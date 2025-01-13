package com.wavez.trackerwater.feature.reminder.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseBottomSheetFragment
import com.wavez.trackerwater.databinding.DialogReminderModeBinding

class ReminderModeDialog: BaseBottomSheetFragment<DialogReminderModeBinding>() {

    companion object{
        fun newInstance(): ReminderModeDialog{
            return ReminderModeDialog()
        }

    }

    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogReminderModeBinding {
        return DialogReminderModeBinding.inflate(layoutInflater)
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