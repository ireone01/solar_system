package com.wavez.trackerwater.data.repository

import com.wavez.trackerwater.data.dao.DrinkDAO
import com.wavez.trackerwater.data.model.DrinkModel
import javax.inject.Inject

class DrinkRepositoryImpl @Inject constructor(
    private val drinkDAO: DrinkDAO
) : DrinkRepository {

    override suspend fun getAll(): List<DrinkModel> {
        return drinkDAO.getAll()
    }

    override suspend fun delete(drinkModel: DrinkModel){
        drinkDAO.delete(drinkModel)
    }

    override suspend fun update(drinkModel: DrinkModel){
        drinkDAO.update(drinkModel)
    }

    override suspend fun getDrinkByAmount(amount: Int): DrinkModel? {
        return drinkDAO.getDrinkByAmount(amount)
    }

    override suspend fun insert(drinkModel: DrinkModel) {
        drinkDAO.insert(drinkModel)
    }

}