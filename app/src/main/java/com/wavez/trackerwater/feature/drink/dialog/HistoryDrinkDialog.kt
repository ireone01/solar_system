package com.wavez.trackerwater.feature.drink.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.lingvo.base_common.ui.BaseBottomSheetFragment
import com.wavez.trackerwater.data.model.DrinkModel
import com.wavez.trackerwater.databinding.DialogHistoryBinding
import com.wavez.trackerwater.feature.drink.adapter.HistoryDrinkAdapter

class HistoryDrinkDialog : BaseBottomSheetFragment<DialogHistoryBinding>() {

    companion object {
        fun newInstance() = HistoryDrinkDialog()
    }

    private lateinit var adapter: HistoryDrinkAdapter

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
        adapter = HistoryDrinkAdapter()
        binding.rcvHistory.adapter = adapter
        binding.rcvHistory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.btnClose.setOnClickListener { dismiss() }

        adapter.onSelect = {
            onSelect(it)
        }
    }

    interface IHistoryDrinkListener {
        fun onSelectAmount()
    }

    private fun onSelect(history: DrinkModel) {
        Toast.makeText(context, "" + history.amountDrink, Toast.LENGTH_SHORT).show()
    }
}