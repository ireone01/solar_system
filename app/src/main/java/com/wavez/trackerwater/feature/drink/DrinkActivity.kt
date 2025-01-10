package com.wavez.trackerwater.feature.drink

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.R
import com.wavez.trackerwater.data.model.IntakeDrink
import com.wavez.trackerwater.data.sharedPref.SharedPref
import com.wavez.trackerwater.databinding.ActivityDrinkBinding
import com.wavez.trackerwater.evenbus.DataUpdatedEvent
import com.wavez.trackerwater.extension.hideKeyboardFrom
import com.wavez.trackerwater.extension.invisible
import com.wavez.trackerwater.extension.showKeyboardFrom
import com.wavez.trackerwater.extension.visible
import com.wavez.trackerwater.feature.drink.dialog.HistoryDrinkDialog
import com.wavez.trackerwater.feature.drink.viewModel.DrinkViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@AndroidEntryPoint
class DrinkActivity : BaseActivity<ActivityDrinkBinding>(), HistoryDrinkDialog.IHistoryDrinkListener {

    companion object {
        const val MIN_CUP_DRINK = 10
        const val MAX_CUP_DRINK = 400
        const val MIN_BOTTLE_DRINK = 100
        const val MAX_BOTTLE_DRINK = 3000
    }

    override fun createBinding(): ActivityDrinkBinding {
        return ActivityDrinkBinding.inflate(layoutInflater)
    }

    private val drinkViewModel by viewModels<DrinkViewModel>()

    @Inject
    lateinit var sharedPref: SharedPref

    private var isTypeDrinkCup: Boolean = true

    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)
        isTypeDrinkCup = sharedPref.isTypeDrinkCup
        syncTypeDrink()

    }

    @SuppressLint("SetTextI18n")
    override fun initListener() {
        super.initListener()
        binding.ivClose.setOnClickListener {
            finish()
        }

        binding.btnDrink.setOnClickListener {
            val amount = binding.edtAmount.text.toString().toIntOrNull()
            if (amount != null) {
                drinkViewModel.insertHistory(amount)
                drinkViewModel.insertIntake(amount)
                EventBus.getDefault().post(DataUpdatedEvent())
                finish()
            }

        }

        binding.seekBarWater.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val actualValue = if (isTypeDrinkCup) {
                    progress + MIN_CUP_DRINK
                } else {
                    progress + MIN_BOTTLE_DRINK
                }
                binding.edtAmount.setText(actualValue.toString())
                binding.waveLoadingView.progressValue = calculateWaveProgress(actualValue)
                if (binding.waveLoadingView.progressValue == 100) {
                    binding.viewState.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@DrinkActivity, R.color.danger_500))
                    binding.tvNote.visible()
                } else {
                    binding.viewState.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@DrinkActivity, R.color.primary_base_em))
                    binding.tvNote.invisible()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.edtAmount.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                syncDrinkWater()
                hideKeyboardFrom(this, v)
                true
            } else {
                false
            }
        }

        binding.ivHistory.setOnClickListener {
            HistoryDrinkDialog.newInstance()
                .show(supportFragmentManager, HistoryDrinkDialog::class.java.simpleName)
        }

        binding.btnEdit.setOnClickListener {
            binding.edtAmount.requestFocus()
            binding.edtAmount.selectAll()
            showKeyboardFrom(this, binding.edtAmount)
        }

        binding.ivSwitchType.setOnClickListener {
            isTypeDrinkCup = !isTypeDrinkCup
            sharedPref.isTypeDrinkCup = isTypeDrinkCup
            syncTypeDrink()

        }
    }

    private fun syncDrinkWater() {
        val input = binding.edtAmount.text.toString().toIntOrNull()
        val min = if (isTypeDrinkCup) MIN_CUP_DRINK else MIN_BOTTLE_DRINK
        val max = if (isTypeDrinkCup) MAX_CUP_DRINK else MAX_BOTTLE_DRINK

        if (input != null) {
            val clampedValue = input.coerceIn(min, max)

            binding.edtAmount.setText(clampedValue.toString())
            binding.edtAmount.setSelection(clampedValue.toString().length)

            binding.seekBarWater.progress = clampedValue - min

            binding.waveLoadingView.progressValue = calculateWaveProgress(clampedValue)
            if (input < min) {
                binding.edtAmount.setText(min.toString())
                binding.edtAmount.setSelection(min.toString().length)
            }

        } else {
            binding.edtAmount.setText(min.toString())
            binding.edtAmount.setSelection(min.toString().length)
            binding.seekBarWater.progress = 0
            binding.waveLoadingView.progressValue = 0
        }
    }

    private fun syncTypeDrink() {
        binding.ivGlass.setImageResource(if (isTypeDrinkCup) R.drawable.ic_cup_drink else R.drawable.ic_bottle_drink)
        if (isTypeDrinkCup) {
            binding.seekBarWater.max = MAX_CUP_DRINK - MIN_CUP_DRINK
            binding.edtAmount.setText(MIN_CUP_DRINK.toString())
        } else {
            binding.seekBarWater.max = MAX_BOTTLE_DRINK - MIN_BOTTLE_DRINK
            binding.edtAmount.setText(MIN_BOTTLE_DRINK.toString())
        }

        binding.waveLoadingView.progressValue = 0
        binding.seekBarWater.progress = 0
    }

    private fun calculateWaveProgress(value: Int): Int {
        return if (isTypeDrinkCup) {
            ((value - MIN_CUP_DRINK) * 100) / (MAX_CUP_DRINK - MIN_CUP_DRINK)
        } else {
            ((value - MIN_BOTTLE_DRINK) * 100) / (MAX_BOTTLE_DRINK - MIN_BOTTLE_DRINK)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onSelectAmount(intake: IntakeDrink) {
        binding.edtAmount.setText(intake.amountIntake.toString())
        syncDrinkWater()
    }
}