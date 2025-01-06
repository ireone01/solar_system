package com.wavez.trackerwater.feature.myprofile

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.lingvo.base_common.ui.BaseDialogFragment
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.FragmentMeDailyGoalBinding

class FragmentDialogDailyGoal : BaseDialogFragment<FragmentMeDailyGoalBinding>() {
    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMeDailyGoalBinding {
        return FragmentMeDailyGoalBinding.inflate(inflater,container,false)
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            window.setLayout(
                (resources.displayMetrics.widthPixels * 0.8).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window.setGravity(Gravity.CENTER)
            window.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setDimAmount(0.5f)
        return dialog
    }


}