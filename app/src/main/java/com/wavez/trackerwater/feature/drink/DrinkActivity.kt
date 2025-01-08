package com.wavez.trackerwater.feature.drink

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.data.model.DrinkModel
import com.wavez.trackerwater.databinding.ActivityDrinkBinding
import com.wavez.trackerwater.databinding.DialogHistoryBinding
import com.wavez.trackerwater.feature.drink.adapter.HistoryDrinkAdapter
import com.wavez.trackerwater.feature.drink.viewModel.DrinkViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DrinkActivity : BaseActivity<ActivityDrinkBinding>() {
    override fun createBinding(): ActivityDrinkBinding {
        return ActivityDrinkBinding.inflate(layoutInflater)
    }

    private val drinkViewModel by viewModels<DrinkViewModel>()
    private lateinit var adapter: HistoryDrinkAdapter

    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
    }

    override fun initListener() {
        super.initListener()
        binding.ivClose.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }

        binding.btnDrink.setOnClickListener {
            val amount = binding.edtAmount.text.toString().toIntOrNull()
            if (amount != null && amount > 0) {
                drinkViewModel.insertDrink(amount)
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "Input", Toast.LENGTH_SHORT).show()
            }
        }
        binding.ivHistory.setOnClickListener { openDialogHistory() }
    }

    override fun initObserver() {
        super.initObserver()
        drinkViewModel.historyList.observe(this) { list ->
            if (list.isNullOrEmpty()) {
                Toast.makeText(this, "No history available", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setData(list)
            }
        }
    }


    fun openDialogHistory() {
        val dialogBinding = DialogHistoryBinding.inflate(layoutInflater)

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(dialogBinding.root)

        adapter = HistoryDrinkAdapter()
        dialogBinding.rcvHistory.adapter = adapter
        dialogBinding.rcvHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        dialogBinding.btnClose.setOnClickListener { dialog.dismiss() }
        drinkViewModel.getAllData()

        dialog.show()

    }

    private fun onSelect(history: DrinkModel) {
        Toast.makeText(this, "" + history.amountDrink, Toast.LENGTH_SHORT).show()
    }
}