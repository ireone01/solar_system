package com.wavez.trackerwater.feature.drink.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavez.trackerwater.data.model.HistoryDrink
import com.wavez.trackerwater.data.model.IntakeDrink
import com.wavez.trackerwater.data.repository.history.HistoryRepository
import com.wavez.trackerwater.data.repository.intake.IntakeRepository
import com.wavez.trackerwater.evenbus.DataUpdatedEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@HiltViewModel
class DrinkViewModel @Inject constructor(
    private val historyRepository: HistoryRepository, private val intakeRepository: IntakeRepository
) : ViewModel() {

    private val _historyList = MutableLiveData<List<HistoryDrink>>()
    val historyList: LiveData<List<HistoryDrink>> get() = _historyList

    private val _intakeList = MutableLiveData<List<IntakeDrink>>()
    val intakeList: LiveData<List<IntakeDrink>> get() = _intakeList

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
            val newHistory = HistoryDrink(
                amountHistory = amount, dateHistory = System.currentTimeMillis()
            )
            historyRepository.insert(newHistory)
            EventBus.getDefault().post(DataUpdatedEvent())


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
                val intakeModel = IntakeDrink(
                    amountIntake = amount,
                )
                intakeRepository.insert(intakeModel)
            }
        }
    }


}