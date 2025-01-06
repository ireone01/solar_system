package com.wavez.trackerwater.feature.fragment.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavez.trackerwater.data.model.DrinkModel
import com.wavez.trackerwater.data.repository.DrinkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val drinkRepository: DrinkRepository
) : ViewModel(){
    val drinkList = MutableLiveData<List<DrinkModel>>()
    private val _progress = MutableLiveData<Int>()
    val progress: LiveData<Int> get() = _progress

    fun getAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            drinkList.postValue(drinkRepository.getAll())
        }

    }

    init {
        _progress.value = 0  // Giá trị mặc định
    }

    val totalAmount = MutableLiveData<Int>()

    fun getTotal() {
        viewModelScope.launch(Dispatchers.IO) {
            val total = drinkRepository.getAll().sumOf { it.amountDrink*it.countDrink }
            totalAmount.postValue(total)
        }
    }

    fun setProgress(value: Int) {
        _progress.value = value
    }
}