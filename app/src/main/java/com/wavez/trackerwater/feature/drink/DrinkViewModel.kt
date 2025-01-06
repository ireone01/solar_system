package com.wavez.trackerwater.feature.drink

import android.util.Log
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
class DrinkViewModel @Inject constructor(
    private val drinkRepository: DrinkRepository,
) : ViewModel() {
    val historyList = MutableLiveData<List<DrinkModel>>()

    val TAG = "minh"

    fun getAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            historyList.postValue(drinkRepository.getAll())
        }

    }


    fun insertDrink(amount: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingDrink = drinkRepository.getDrinkByAmount(amount)
            if (existingDrink != null) {
                val updatedDrink = existingDrink.copy(countDrink = existingDrink.countDrink + 1)
                drinkRepository.update(updatedDrink)
            } else {
                val newDrink = DrinkModel(
                    amountDrink = amount,
                    countDrink = 1,
                    dateDrink = System.currentTimeMillis()
                )
                drinkRepository.insert(newDrink)
            }
        }
    }

}