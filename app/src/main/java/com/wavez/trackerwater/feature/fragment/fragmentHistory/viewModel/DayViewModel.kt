package com.wavez.trackerwater.feature.fragment.fragmentHistory.viewModel

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
class DayViewModel @Inject constructor(
    val historyRepository: HistoryRepository,
    val intakeRepository: IntakeRepository
): ViewModel(){
    val historyList = MutableLiveData<List<HistoryModel>>()

    fun getAllData(){
        viewModelScope.launch(Dispatchers.IO) {
            historyList.postValue(historyRepository.getAll())
        }
    }

    fun delete(historyModel: HistoryModel){
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.delete(historyModel)
            historyList.postValue(historyRepository.getAll())
        }
    }

    fun edit(historyModel: HistoryModel){
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.update(historyModel)
        }
    }

    val totalAmount = MutableLiveData<Int>()
    fun getTotal() {
        viewModelScope.launch(Dispatchers.IO) {
            val total = historyRepository.getAll().sumOf { it.amountHistory }
            totalAmount.postValue(total)
        }
    }

    fun insertHistory(historyModel: HistoryModel) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.insert(historyModel)
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