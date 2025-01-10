package com.wavez.trackerwater.feature.drink

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.data.model.IntakeModel
import com.wavez.trackerwater.databinding.ActivityDrinkBinding
import com.wavez.trackerwater.databinding.DialogHistoryBinding
import com.wavez.trackerwater.evenbus.DataUpdatedEvent
import com.wavez.trackerwater.feature.drink.adapter.IntakeAdapter
import com.wavez.trackerwater.feature.drink.dialog.HistoryDrinkDialog
import com.wavez.trackerwater.feature.drink.viewModel.DrinkViewModel
import com.wavez.trackerwater.util.TextUtils
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

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
            finish()
        }

        binding.btnDrink.setOnClickListener {
            val amount = binding.edtAmount.text.toString().toIntOrNull()
            if (amount != null && amount > 0) {
                drinkViewModel.insertHistory(amount)
                drinkViewModel.insertIntake(amount)
                Log.d("minh", "Sending event")
                finish()
                EventBus.getDefault().post(DataUpdatedEvent(TextUtils.INSERT))

            } else {
                Toast.makeText(this, "Input", Toast.LENGTH_SHORT).show()
            }

        }
        binding.ivHistory.setOnClickListener {
            HistoryDrinkDialog.newInstance()
                .show(supportFragmentManager, HistoryDrinkDialog::class.java.simpleName)
        }
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

    @SuppressLint("SetTextI18n")
    private fun onSelect(intake: IntakeModel) {
        binding.edtAmount.setText(intake.amountIntake.toString())

    }


}