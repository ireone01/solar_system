package com.wavez.trackerwater.feature.page.today.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavez.trackerwater.data.model.HistoryDrink
import com.wavez.trackerwater.data.model.RecentDrink
import com.wavez.trackerwater.data.repository.history.HistoryRepository
import com.wavez.trackerwater.util.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _historyList = MutableLiveData<List<RecentDrink>>()
    val historyList: LiveData<List<RecentDrink>> get() = _historyList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _totalDrank = MutableLiveData<Int>()
    val totalDrank: LiveData<Int> get() = _totalDrank

    var recentDrinkSelected: RecentDrink? = null

    init {
        getAllData()
    }

    fun getAllData() {
        getHistoryByDay()
        getTodayTotalAmount()
    }

    fun getHistoryByDay() {
        viewModelScope.launch {
            _isLoading.value = true
            withContext(Dispatchers.IO) {
                try {
                    val (startOfDay, endOfDay) = TimeUtils.getStartAndEndOfDay(System.currentTimeMillis())
                    val data = historyRepository.getHistoryBetweenDates(startOfDay, endOfDay)

                    val groupedByAmountHistory = data.groupingBy { it.amountHistory }.eachCount()
                    val uniqueHistoryList = groupedByAmountHistory.map { (amount, count) ->
                        RecentDrink(amountHistory = amount, count = count)
                    }

                    _historyList.postValue(uniqueHistoryList)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _isLoading.postValue(false)
                }
            }
        }
    }


    fun getTodayTotalAmount() {
        viewModelScope.launch {
            _isLoading.value = true
            withContext(Dispatchers.IO) {
                try {
                    val (startOfDay, endOfDay) = TimeUtils.getStartAndEndOfDay(System.currentTimeMillis())
                    val todayData = historyRepository.getHistoryBetweenDates(startOfDay, endOfDay)

                    val groupedByAmountHistory =
                        todayData.groupingBy { it.amountHistory }.eachCount()
                    val uniqueHistoryList = groupedByAmountHistory.map { (amount, count) ->
                        RecentDrink(amountHistory = amount, count = count)
                    }

                    _historyList.postValue(uniqueHistoryList)

                    val total = todayData.sumOf { it.amountHistory }
                    _totalDrank.postValue(total)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    _isLoading.postValue(false)
                }
            }
        }
    }


    fun delete() {
        viewModelScope.launch(Dispatchers.IO) {
            recentDrinkSelected?.amountHistory?.let {
                historyRepository.deleteHistoryRecently(it)
            }
            recentDrinkSelected = null
            getAllData()
        }
    }

    fun insertHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val newHistory = recentDrinkSelected?.amountHistory?.let {
                HistoryDrink(
                    amountHistory = it, dateHistory = System.currentTimeMillis()
                )
            }
            if (newHistory != null) {
                historyRepository.insert(newHistory)
            }
            recentDrinkSelected = null
            getAllData()
        }
    }
}