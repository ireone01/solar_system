package com.wavez.trackerwater.feature.fragment.fragmentHistory.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lingvo.base_common.ui.BaseBottomSheetFragment
import com.wavez.trackerwater.data.model.HistoryModel
import com.wavez.trackerwater.databinding.DialogAddRecordBinding
import com.wavez.trackerwater.feature.fragment.fragmentHistory.dialog.InsertOrUpdateBottomDialog.AddRecordListener

class UpdateDialog : BaseBottomSheetFragment<DialogAddRecordBinding>() {

    companion object {
        fun newInstance(historyModel: HistoryModel) = InsertOrUpdateBottomDialog()
    }

    override fun initializeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogAddRecordBinding {
        return DialogAddRecordBinding.inflate(layoutInflater)
    }

    private var listener: AddRecordListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddRecordListener

        } catch (e: Exception) {
            try {
                listener = parentFragment as AddRecordListener
            } catch (e: Exception) {
            }

        }

    }

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
    }

    override fun initListener() {
        super.initListener()
//        binding.edtAmount.text = historyModel.amountHistory
    }

    override fun initObserver() {
        super.initObserver()
    }
}