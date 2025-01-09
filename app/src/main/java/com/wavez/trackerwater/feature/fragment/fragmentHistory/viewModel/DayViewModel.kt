package com.wavez.trackerwater.feature.fragment.fragmentHistory.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavez.trackerwater.data.model.HistoryModel
import com.wavez.trackerwater.data.model.IntakeModel
import com.wavez.trackerwater.data.repository.history.HistoryRepository
import com.wavez.trackerwater.data.repository.intake.IntakeRepository
import com.wavez.trackerwater.util.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DayViewModel @Inject constructor(
    val historyRepository: HistoryRepository,
    val intakeRepository: IntakeRepository
) : ViewModel() {

    private val _historyList = MutableLiveData<List<HistoryModel>>(emptyList())
    val historyList: LiveData<List<HistoryModel>> = _historyList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
//        getAllData()
        getTotal()
        getHistoryByDay()
    }

//    fun getAllData() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            withContext(Dispatchers.IO) {
//                try {
//                    val list = historyRepository.getAll()
//                    _historyList.postValue(list)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                } finally {
//                    _isLoading.postValue(false)
//                }
//            }
//        }
//
//    }

//    fun getAllData(){
//        viewModelScope.launch(Dispatchers.IO) {
//            historyList.postValue(historyRepository.getAll())
//        }
//    }

    fun delete(historyModel: HistoryModel) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.delete(historyModel)
//            getAllData()
        }
    }

    fun edit(historyModel: HistoryModel) {
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
//            getAllData()
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

    fun getHistoryByDay() {
        viewModelScope.launch {
            _isLoading.value = true
            withContext(Dispatchers.IO) {
                try {
                    val (startOfDay, endOfDay) = TimeUtils.getStartAndEndOfDay(System.currentTimeMillis())
                    val data = historyRepository.getHistoryBetweenDates(startOfDay, endOfDay)
                    _historyList.postValue(data)
                } catch (e: Exception) {

                } finally {
                    _isLoading.postValue(false)
                }
            }

        }
    }

    fun getHistoryByDayRange(startOfDay: Long, endOfDay: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            withContext(Dispatchers.IO) {
                try {
                    val data = historyRepository.getHistoryBetweenDates(startOfDay, endOfDay)
                    _historyList.postValue(data)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _isLoading.postValue(false)
                }
            }
        }
    }


}