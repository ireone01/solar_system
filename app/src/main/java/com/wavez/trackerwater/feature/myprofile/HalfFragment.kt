package com.wavez.trackerwater.feature.myprofile

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.lingvo.base_common.ui.BaseFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.FragmentMeBinding

class HalfFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_me_half,container,false)
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window.setGravity(Gravity.BOTTOM)
            window.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setDimAmount(0.5f)
        return dialog
    }
}