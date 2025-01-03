package com.wavez.trackerwater.ui.fragment.viewModel

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
class TodayViewModel @Inject constructor(
    private val drinkRepository: DrinkRepository
) : ViewModel(){
    val drinkList = MutableLiveData<List<DrinkModel>>()

    fun getAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            drinkList.postValue(drinkRepository.getAll())
        }

    }

    fun delete(drinkModel: DrinkModel){
        viewModelScope.launch(Dispatchers.IO) {
            drinkRepository.delete(drinkModel)
            drinkList.postValue(drinkRepository.getAll())
        }

    }


}