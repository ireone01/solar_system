package com.wavez.trackerwater.feature.drink.viewModel

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
                val newDrink = DrinkModel(
                    amountDrink = amount,
                    dateDrink = System.currentTimeMillis()
                )
                drinkRepository.insert(newDrink)
            }
        }

}