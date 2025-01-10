package com.wavez.trackerwater.feature.drink.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavez.trackerwater.data.model.HistoryModel
import com.wavez.trackerwater.data.model.HistoryModelWithCount
import com.wavez.trackerwater.data.model.IntakeModel
import com.wavez.trackerwater.data.repository.history.HistoryRepository
import com.wavez.trackerwater.data.repository.intake.IntakeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrinkViewModel @Inject constructor(
    private val historyRepository: HistoryRepository, private val intakeRepository: IntakeRepository
) : ViewModel() {

    private val _historyList = MutableLiveData<List<HistoryModel>>()
    val historyList: LiveData<List<HistoryModel>> get() = _historyList

    private val _intakeList = MutableLiveData<List<IntakeModel>>()
    val intakeList: LiveData<List<IntakeModel>> get() = _intakeList

    init {
        getAllData()
        getAllIntake()
    }

    fun getAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            _historyList.postValue(historyRepository.getAll())
        }
    }


    fun insertHistory(amount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newHistory = HistoryModel(
                amountHistory = amount, dateHistory = System.currentTimeMillis()
            )
            historyRepository.insert(newHistory)
        }
    }

    private fun getAllIntake() {
        viewModelScope.launch(Dispatchers.IO) {
            _intakeList.postValue(intakeRepository.getAll())
        }
    }


    fun insertIntake(amount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingIntake = intakeRepository.getIntakeByAmount(amount)
            if (existingIntake == null) {
                val intakeModel = IntakeModel(
                    amountIntake = amount,
                )
                intakeRepository.insert(intakeModel)
            }
        }
    }


}