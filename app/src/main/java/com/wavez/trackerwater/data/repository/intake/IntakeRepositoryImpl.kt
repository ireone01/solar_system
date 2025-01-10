package com.wavez.trackerwater.data.repository.intake

import com.wavez.trackerwater.data.dao.IntakeDAO
import com.wavez.trackerwater.data.model.IntakeDrink
import javax.inject.Inject

class IntakeRepositoryImpl @Inject constructor(
    private val intakeDAO: IntakeDAO
): IntakeRepository {
    override suspend fun getAll(): List<IntakeDrink> {
        return intakeDAO.getAll()
    }

    override suspend fun insert(intakeModel: IntakeDrink) {
        intakeDAO.insert(intakeModel)
    }

    override suspend fun getIntakeByAmount(amountIntake: Int): IntakeDrink? {
        return intakeDAO.getIntakeByAmount(amountIntake)
    }

}