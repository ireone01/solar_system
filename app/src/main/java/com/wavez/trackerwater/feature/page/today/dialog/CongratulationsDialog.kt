package com.wavez.trackerwater.feature.page.today.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseDialogFragment
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.DialogCongratulationsBinding

class CongratulationsDialog: BaseDialogFragment<DialogCongratulationsBinding>() {

    companion object {
        private const val PROGRESS_VALUE = "PROGRESS_VALUE"

        fun newInstance(progressValue: Int): CongratulationsDialog {
            val dialog = CongratulationsDialog()
            val args = Bundle()
            args.putInt(PROGRESS_VALUE, progressValue)
            dialog.arguments = args
            return dialog
        }
    }

    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogCongratulationsBinding {
        return DialogCongratulationsBinding.inflate(layoutInflater)
    }

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
    }

    override fun initListener() {
        super.initListener()

        val progressValue = arguments?.getInt(PROGRESS_VALUE) ?: 0

        if (progressValue > 100) {
            binding.tvCongratulationsContent.text = getText(R.string.congratulations_content_2)
            binding.ivCongratulations.setImageResource(R.drawable.img_congratulations_100)
        } else if (progressValue in 1..99) {
            binding.tvCongratulationsContent.text = getText(R.string.congratulations_content_1)
            binding.ivCongratulations.setImageResource(R.drawable.img_congratulations_0)
        }

        binding.btnGreat.setOnClickListener { dismiss() }
    }

    override fun initObserver() {
        super.initObserver()
    }
}
