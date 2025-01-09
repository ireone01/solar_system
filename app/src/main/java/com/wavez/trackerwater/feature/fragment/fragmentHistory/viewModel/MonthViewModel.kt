package com.wavez.trackerwater.feature.fragment.fragmentHistory.viewModel

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
import javax.inject.Inject

@HiltViewModel
class MonthViewModel @Inject constructor(
    val historyRepository: HistoryRepository

) : ViewModel() {

    private val _historyList = MutableLiveData<List<HistoryModel>>(emptyList())
    val historyList: LiveData<List<HistoryModel>> = _historyList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getHistoryByMonth(time: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            withContext(Dispatchers.IO) {
                try {
                    val (startOfMonth, endOfMonth) = TimeUtils.getStartAndEndOfMonth(time)
                    val data = historyRepository.getHistoryBetweenDates(startOfMonth, endOfMonth)
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