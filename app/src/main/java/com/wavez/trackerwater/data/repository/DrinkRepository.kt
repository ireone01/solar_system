package com.wavez.trackerwater.data.repository

import com.wavez.trackerwater.data.model.DrinkModel

interface DrinkRepository {
    suspend fun getAll(): List<DrinkModel>
    suspend fun insert(drinkModel: DrinkModel)
    suspend fun delete(drinkModel: DrinkModel)
    suspend fun update(drinkModel: DrinkModel)
    suspend fun getDrinkByAmount(amount: Int): DrinkModel?

}