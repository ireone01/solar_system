package com.wavez.trackerwater.feature.fragment.fragmentHistory.viewModel

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
class DayViewModel @Inject constructor(
    val drinkRepository: DrinkRepository
): ViewModel(){
    val historyList = MutableLiveData<List<DrinkModel>>()

    fun getAllData(){
        viewModelScope.launch(Dispatchers.IO) {
            historyList.postValue(drinkRepository.getAll())
        }
    }

    fun delete(drinkModel: DrinkModel){
        viewModelScope.launch(Dispatchers.IO) {
            drinkRepository.delete(drinkModel)
            historyList.postValue(drinkRepository.getAll())
        }
    }

    fun edit(drinkModel: DrinkModel){
        viewModelScope.launch(Dispatchers.IO) {
            drinkRepository.update(drinkModel)
        }
    }

    val totalAmount = MutableLiveData<Int>()
    fun getTotal() {
        viewModelScope.launch(Dispatchers.IO) {
            val total = drinkRepository.getAll().sumOf { it.amountDrink*it.countDrink }
            totalAmount.postValue(total)
        }
    }
}