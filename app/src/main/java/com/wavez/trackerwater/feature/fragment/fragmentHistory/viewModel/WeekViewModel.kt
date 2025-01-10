package com.wavez.trackerwater.feature.fragment.fragmentHistory.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavez.trackerwater.data.model.HistoryModel
import com.wavez.trackerwater.data.repository.history.HistoryRepository
import com.wavez.trackerwater.util.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WeekViewModel @Inject constructor(
    val historyRepository: HistoryRepository
) : ViewModel() {

    private val _historyList = MutableLiveData<List<HistoryModel>>(emptyList())
    val historyList: LiveData<List<HistoryModel>> = _historyList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _total = MutableLiveData<Int>(0)
    val total: LiveData<Int> get() = _total

    private val _average = MutableLiveData<Int>(0)
    val average: LiveData<Int> get() = _average

    private var currentStartOfWeek: Long = TimeUtils.getStartOfWeek(System.currentTimeMillis())
    private var currentEndOfWeek: Long = TimeUtils.getEndOfWeek(System.currentTimeMillis())

    private val _weekRange = MutableLiveData<String>()
    val weekRange: LiveData<String> get() = _weekRange

    init {
        getHistoryByWeek()
    }

    fun getHistoryByWeek() {
        viewModelScope.launch {
            _isLoading.value = true
            withContext(Dispatchers.IO) {
                try {
                    val data = historyRepository.getHistoryBetweenDates(currentStartOfWeek, currentEndOfWeek)
                    _historyList.postValue(data)
                    calculateTotalAndAverage(data)
                    updateWeekRange()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _isLoading.postValue(false)
                }
            }
        }
    }

    fun nextWeek() {
        currentStartOfWeek = TimeUtils.getStartOfWeek(currentStartOfWeek + TimeUtils.MILLISECONDS_IN_WEEK)
        currentEndOfWeek = TimeUtils.getEndOfWeek(currentEndOfWeek + TimeUtils.MILLISECONDS_IN_WEEK)
        getHistoryByWeek()
    }

    fun previousWeek() {
        currentStartOfWeek = TimeUtils.getStartOfWeek(currentStartOfWeek - TimeUtils.MILLISECONDS_IN_WEEK)
        currentEndOfWeek = TimeUtils.getEndOfWeek(currentEndOfWeek - TimeUtils.MILLISECONDS_IN_WEEK)
        getHistoryByWeek()
    }

    private fun calculateTotalAndAverage(data: List<HistoryModel>) {
        val validData = data.filter { it.amountHistory > 0 }
        val totalAmount = validData.sumOf { it.amountHistory }
        val avgAmount = if (validData.isNotEmpty()) totalAmount / validData.size else 0

        Log.d("WeekViewModel", "Total: $totalAmount, Average: $avgAmount")

        _total.postValue(totalAmount)
        _average.postValue(avgAmount)
    }

    private fun updateWeekRange() {
        val startDate = Date(currentStartOfWeek)
        val endDate = Date(currentEndOfWeek)

        val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
        val startDateStr = dateFormat.format(startDate)
        val endDateStr = dateFormat.format(endDate)

        val calendar = Calendar.getInstance()
        calendar.time = startDate
        val year = calendar.get(Calendar.YEAR)

        _weekRange.postValue("$startDateStr - $endDateStr, $year")
    }

}
