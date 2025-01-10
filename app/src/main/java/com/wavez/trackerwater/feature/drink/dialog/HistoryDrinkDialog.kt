package com.wavez.trackerwater.feature.drink.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.lingvo.base_common.ui.BaseBottomSheetFragment
import com.wavez.trackerwater.data.model.IntakeDrink
import com.wavez.trackerwater.databinding.DialogHistoryBinding
import com.wavez.trackerwater.feature.drink.adapter.IntakeAdapter
import com.wavez.trackerwater.feature.drink.viewModel.DrinkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryDrinkDialog : BaseBottomSheetFragment<DialogHistoryBinding>() {

    companion object {
        fun newInstance() = HistoryDrinkDialog()
    }

    private lateinit var adapter: IntakeAdapter

    private val activityViewModel by activityViewModels<DrinkViewModel>()

    override fun initializeBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): DialogHistoryBinding {
        return DialogHistoryBinding.inflate(layoutInflater)
    }

    private var listener: IHistoryDrinkListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as IHistoryDrinkListener
        } catch (e: Exception) {
            try {
                listener = parentFragment as IHistoryDrinkListener
            } catch (e: Exception) {

            }
        }
    }

    override fun initConfig(view: View, savedInstanceState: Bundle?) {
        super.initConfig(view, savedInstanceState)
        adapter = IntakeAdapter()
        binding.rcvHistory.adapter = adapter

        adapter.onSelect = {
            listener?.onSelectAmount(it)
            dismiss()
        }
    }

    override fun initObserver() {
        super.initObserver()
        activityViewModel.intakeList.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
    }

    override fun initListener() {
        super.initListener()
        binding.btnClose.setOnClickListener {
            dismiss()
        }

    }

    interface IHistoryDrinkListener {
        fun onSelectAmount(intake: IntakeDrink)
    }
}