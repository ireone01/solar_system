package com.wavez.trackerwater.feature.drink

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.data.model.DrinkModel
import com.wavez.trackerwater.data.model.HistoryModel
import com.wavez.trackerwater.databinding.ActivityDrinkBinding
import com.wavez.trackerwater.databinding.DialogHistoryBinding
import com.wavez.trackerwater.feature.fragment.adapter.DrinkAdapter
import com.wavez.trackerwater.feature.fragment.adapter.HistoryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DrinkActivity : BaseActivity<ActivityDrinkBinding>() {
    override fun createBinding(): ActivityDrinkBinding {
        return ActivityDrinkBinding.inflate(layoutInflater)
    }

    private val drinkViewModel by viewModels<DrinkViewModel>()
    private lateinit var adapter: HistoryAdapter

    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
    }

    override fun initListener() {
        super.initListener()
        binding.ivClose.setOnClickListener {
            finish()
        }
        binding.btnDrink.setOnClickListener {
            val amount = binding.edtAmount.text.toString().toFloatOrNull()
            if (amount != null && amount > 0) {
                drinkViewModel.insertDrink(amount)
                drinkViewModel.insertHistory(amount)
            } else {
                Toast.makeText(this, "Input", Toast.LENGTH_SHORT).show()
            }
        }
        binding.ivHistory.setOnClickListener { openDialogHistory() }
    }

    override fun initObserver() {
        super.initObserver()
    }

    fun openDialogHistory(){
        val dialogBinding = DialogHistoryBinding.inflate(layoutInflater)

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(dialogBinding.root)
        dialog.show()
        drinkViewModel.getAllData()
        adapter = HistoryAdapter(mutableListOf(), this::onSelect)

        dialogBinding.btnClose.setOnClickListener { dialog.dismiss() }
        dialogBinding.rcvHistory.adapter = adapter
        dialogBinding.rcvHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


    }

    private fun onSelect(history: HistoryModel) {
        Toast.makeText(this,""+ history.amountHistory, Toast.LENGTH_SHORT).show()
    }
}