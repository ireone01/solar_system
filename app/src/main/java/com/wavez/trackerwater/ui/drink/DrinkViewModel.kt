package com.wavez.trackerwater.ui.drink

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavez.trackerwater.data.model.DrinkModel
import com.wavez.trackerwater.data.model.HistoryModel
import com.wavez.trackerwater.data.repository.DrinkRepository
import com.wavez.trackerwater.data.repository.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrinkViewModel @Inject constructor(
    private val drinkRepository: DrinkRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {
    val historyList = MutableLiveData<List<HistoryModel>>()

    val TAG = "minh"

    fun getAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            historyList.postValue(historyRepository.getAll())
        }

    }

    fun insertDrink(amount: Float) {
        Log.d(TAG, "insertDrink: ")
        viewModelScope.launch(Dispatchers.IO) {
            drinkRepository.insert(DrinkModel(amountDrink = amount, countDrink = 1, dateDrink = System.currentTimeMillis()))

        }

    }

    fun insertHistory(amount: Float) {
        Log.d(TAG, "insertHistory: ")
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.insert(HistoryModel(amountHistory = amount))

        }

    }
}