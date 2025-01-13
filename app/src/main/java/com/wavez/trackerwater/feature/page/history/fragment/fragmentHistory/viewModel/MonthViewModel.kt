package com.wavez.trackerwater.feature.page.history.fragment.fragmentHistory.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavez.trackerwater.data.model.HistoryDrink
import com.wavez.trackerwater.data.repository.history.HistoryRepository
import com.wavez.trackerwater.util.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MonthViewModel @Inject constructor(
    val historyRepository: HistoryRepository

) : ViewModel() {

    private val _historyList = MutableLiveData<List<HistoryDrink>>(emptyList())
    val historyList: LiveData<List<HistoryDrink>> = _historyList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _total = MutableLiveData<Int>(0)
    val total: LiveData<Int> get() = _total

    private val _average = MutableLiveData<Int>(0)
    val average: LiveData<Int> get() = _average

    private val _monthRange = MutableLiveData<String>()
    val monthRange: LiveData<String> get() = _monthRange

    private var currentMonthMillis: Long = System.currentTimeMillis()


    init {
        getHistoryByMonth(currentMonthMillis)
    }

    private fun getHistoryByMonth(monthMillis: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            withContext(Dispatchers.IO) {
                try {
                    val (startOfMonth, endOfMonth) = TimeUtils.getStartAndEndOfMonth(monthMillis)
                    val data = historyRepository.getHistoryBetweenDates(startOfMonth, endOfMonth)
                    _historyList.postValue(data)
                    getText(data, startOfMonth)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _isLoading.postValue(false)
                }
            }

        }
    }

    private fun getText(data: List<HistoryDrink>, startOfMonth: Long) {
        val validData = data.filter { it.amountHistory > 0 }
        val totalAmount = data.sumOf { it.amountHistory }
        val avgAmount = if (validData.isNotEmpty()) totalAmount / validData.size else 0
        _total.postValue(totalAmount)
        _average.postValue(avgAmount)

        _monthRange.postValue("${TimeUtils.formatMonth(startOfMonth)} ")
    }

    fun nextMonth() {
        currentMonthMillis = TimeUtils.getNextMonth(currentMonthMillis)
        getHistoryByMonth(currentMonthMillis)
    }

    fun previousMonth() {
        currentMonthMillis = TimeUtils.getPreviousMonth(currentMonthMillis)
        getHistoryByMonth(currentMonthMillis)
    }
}