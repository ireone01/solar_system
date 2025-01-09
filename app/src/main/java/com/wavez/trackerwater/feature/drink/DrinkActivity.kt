package com.wavez.trackerwater.feature.drink

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.data.model.HistoryModel
import com.wavez.trackerwater.data.model.IntakeModel
import com.wavez.trackerwater.databinding.ActivityDrinkBinding
import com.wavez.trackerwater.databinding.DialogHistoryBinding
import com.wavez.trackerwater.evenbus.DataUpdatedEvent
import com.wavez.trackerwater.feature.drink.adapter.HistoryDrinkAdapter
import com.wavez.trackerwater.feature.drink.adapter.IntakeAdapter
import com.wavez.trackerwater.feature.drink.viewModel.DrinkViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@AndroidEntryPoint
class DrinkActivity : BaseActivity<ActivityDrinkBinding>() {
    override fun createBinding(): ActivityDrinkBinding {
        return ActivityDrinkBinding.inflate(layoutInflater)
    }

    private val drinkViewModel by viewModels<DrinkViewModel>()
    private lateinit var adapter: IntakeAdapter

    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)

    }

    override fun initListener() {
        super.initListener()
        binding.ivClose.setOnClickListener {
//            setResult(RESULT_OK)
            finish()
        }

        binding.btnDrink.setOnClickListener {
            val amount = binding.edtAmount.text.toString().toIntOrNull()
            if (amount != null && amount > 0) {
                drinkViewModel.insertHistory(amount)
                drinkViewModel.insertIntake(amount)
//                setResult(RESULT_OK)
                Log.d("minh", "Sending event")
                finish()
                val updatedData = "New Data"
                EventBus.getDefault().post(DataUpdatedEvent(updatedData))

            } else {
                Toast.makeText(this, "Input", Toast.LENGTH_SHORT).show()
            }

        }
        binding.ivHistory.setOnClickListener { openDialogHistory() }
    }

    override fun initObserver() {
        super.initObserver()
        drinkViewModel.intakeList.observe(this) { list ->
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

        adapter = IntakeAdapter()
        adapter.onSelect = {
            onSelect(it)
            dialog.dismiss()
        }
        dialogBinding.rcvHistory.adapter = adapter
        dialogBinding.rcvHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        dialogBinding.btnClose.setOnClickListener { dialog.dismiss() }
        drinkViewModel.getAllIntake()

        dialog.show()

    }

    @SuppressLint("SetTextI18n")
    private fun onSelect(intake: IntakeModel) {
        binding.edtAmount.setText(intake.amountIntake.toString())

    }
//    override fun onStart() {
//        super.onStart()
//        EventBus.getDefault().register(this)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        EventBus.getDefault().unregister(this)
//    }

}