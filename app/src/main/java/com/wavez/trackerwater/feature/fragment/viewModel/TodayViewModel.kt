package com.wavez.trackerwater.feature.fragment.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavez.trackerwater.data.model.HistoryModel
import com.wavez.trackerwater.data.model.HistoryModelWithCount
import com.wavez.trackerwater.data.repository.history.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _historyList = MutableLiveData<List<HistoryModelWithCount>>()
    val historyList: LiveData<List<HistoryModelWithCount>> get() = _historyList

    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int> get() = _progress

    val totalAmount = MutableLiveData<Int>()

    init {
        _progress.value = 0
        getAllData()
        getTotal()
    }

    fun getAllData() {
        viewModelScope.launch(Dispatchers.IO) {

            val historyList = historyRepository.getAll()
            // Nhóm và đếm số lần xuất hiện của các dung tích trùng lặp
            val groupedByAmountHistory = historyList.groupingBy { it.amountHistory }.eachCount()

            // Tạo một danh sách mới với mỗi dung tích chỉ xuất hiện một lần, kèm theo count
            val uniqueHistoryList = groupedByAmountHistory.map { (amount, count) ->
                HistoryModelWithCount(amountHistory = amount, count = count)
            }

            _historyList.postValue(uniqueHistoryList)
        }
    }

    fun getTotal() {
        viewModelScope.launch(Dispatchers.IO) {
            val total = historyRepository.getAll().sumOf { it.amountHistory }
            totalAmount.postValue(total)
        }
    }

    fun delete(historyModel: HistoryModelWithCount) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.deleteHistoryRecently(historyModel.amountHistory)
            getAllData()

        }
    }

    fun setProgress(value: Int) {
        _progress.value = value
    }

    fun insertHistory(amount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newHistory = HistoryModel(
                amountHistory = amount, dateHistory = System.currentTimeMillis()
            )
            historyRepository.insert(newHistory)
        }
    }
}