package com.wavez.trackerwater.feature.drink.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavez.trackerwater.data.model.HistoryModel
import com.wavez.trackerwater.data.model.IntakeModel
import com.wavez.trackerwater.data.repository.history.HistoryRepository
import com.wavez.trackerwater.data.repository.intake.IntakeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrinkViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val intakeRepository: IntakeRepository
) : ViewModel() {
    val historyList = MutableLiveData<List<HistoryModel>>()
    val intakeList = MutableLiveData<List<IntakeModel>>()

    val TAG = "minh"

    fun getAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            historyList.postValue(historyRepository.getAll())
        }
    }


    fun insertHistory(amount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newHistory = HistoryModel(
                amountHistory = amount,
                dateHistory = System.currentTimeMillis()
            )
            historyRepository.insert(newHistory)
        }
    }

    fun getAllIntake() {
        viewModelScope.launch(Dispatchers.IO) {
            intakeList.postValue(intakeRepository.getAll())
        }
    }


    fun insertIntake(amount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingIntake = intakeRepository.getIntakeByAmount(amount)
            if (existingIntake != null) {
                Log.d("IntakeCheck", "Amount $amount đã tồn tại!")
            } else {
                val intakeModel = IntakeModel(
                    amountIntake = amount,
                )
                intakeRepository.insert(intakeModel)
            }
        }
    }


}