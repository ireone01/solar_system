package com.wavez.trackerwater.feature.page.history.fragment.fragmentHistory.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavez.trackerwater.data.model.HistoryDrink
import com.wavez.trackerwater.data.model.IntakeDrink
import com.wavez.trackerwater.data.repository.history.HistoryRepository
import com.wavez.trackerwater.data.repository.intake.IntakeRepository
import com.wavez.trackerwater.util.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DayViewModel @Inject constructor(
    val historyRepository: HistoryRepository,
    val intakeRepository: IntakeRepository
) : ViewModel() {
    private val selectedDate = Calendar.getInstance()

    private val _historyList = MutableLiveData<List<HistoryDrink>>(emptyList())
    val     historyList: LiveData<List<HistoryDrink>> = _historyList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val _totalAmount = MutableLiveData<Int>()
    val totalAmount :LiveData<Int> get() = _totalAmount

    private val _currentDateText = MutableLiveData<String>()
    val currentDateText: LiveData<String> get() = _currentDateText

    init {
        getAllData()
    }

    fun getAllData() {
        getTotal()
        getHistoryByDay()
    }


    fun delete(historyModel: HistoryDrink) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.delete(historyModel)
            getAllData()
        }
    }

    fun edit(historyModel: HistoryDrink) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.update(historyModel)
        }
    }

    fun insertHistory(historyModel: HistoryDrink) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.insert(historyModel)
            getAllData()
        }
    }

    fun insertIntake(amount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingIntake = intakeRepository.getIntakeByAmount(amount)
            if (existingIntake != null) {
                Log.d("IntakeCheck", "Amount $amount đã tồn tại!")
            } else {
                val intakeModel = IntakeDrink(
                    amountIntake = amount,
                )
                intakeRepository.insert(intakeModel)
            }
        }
    }

    fun getTotal() {
        viewModelScope.launch(Dispatchers.IO) {
            val historyData = _historyList.value ?: emptyList()
            val total = historyData.sumOf { it.amountHistory }
            _totalAmount.postValue(total)
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
                    updateTotalAmount(data)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _isLoading.postValue(false)
                }
            }

        }
    }

    fun getHistoryByDayRange(startOfDay: Long, endOfDay: Long) : List<HistoryDrink> {
        viewModelScope.launch {
            _isLoading.value = true
            withContext(Dispatchers.IO) {
                try {
                    val data = historyRepository.getHistoryBetweenDates(startOfDay, endOfDay)
                    _historyList.postValue(data)
                    updateTotalAmount(data)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _isLoading.postValue(false)
                }
            }
        }
        return emptyList()
    }

    private fun updateTotalAmount(data: List<HistoryDrink>) {
        val total = data.sumOf { it.amountHistory }
        _totalAmount.postValue(total)
    }

    fun nextDay() {
        selectedDate.add(Calendar.DAY_OF_MONTH, 1)
        updateDateText()
        updateHistoryByDate()
    }

    fun prevDay() {
        selectedDate.add(Calendar.DAY_OF_MONTH, -1)
        updateDateText()
        updateHistoryByDate()
    }

     private fun updateDateText() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        _currentDateText.value = dateFormat.format(selectedDate.time)

    }

    private fun updateHistoryByDate() {
        val startOfDay = selectedDate.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val endOfDay = selectedDate.apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis

        _historyList.value = getHistoryByDayRange(startOfDay, endOfDay)
    }
}