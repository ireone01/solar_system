package com.wavez.trackerwater.feature.reminder.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseBottomSheetFragment
import com.wavez.trackerwater.databinding.DialogSetTimeRemindBinding

class SetTimeReminderDialog : BaseBottomSheetFragment<DialogSetTimeRemindBinding>() {
    companion object {
        fun newInstance(): SetTimeReminderDialog {
            return SetTimeReminderDialog()
        }
    }

    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogSetTimeRemindBinding {
        return DialogSetTimeRemindBinding.inflate(layoutInflater)
    }

}