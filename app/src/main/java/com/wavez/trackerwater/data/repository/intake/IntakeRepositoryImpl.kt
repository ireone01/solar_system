package com.wavez.trackerwater.data.repository.intake

import com.wavez.trackerwater.data.dao.IntakeDAO
import com.wavez.trackerwater.data.model.IntakeModel
import javax.inject.Inject

class IntakeRepositoryImpl @Inject constructor(
    private val intakeDAO: IntakeDAO
): IntakeRepository {
    override suspend fun getAll(): List<IntakeModel> {
        return intakeDAO.getAll()
    }

    override suspend fun insert(intakeModel: IntakeModel) {
        intakeDAO.insert(intakeModel)
    }

    override suspend fun getIntakeByAmount(amountIntake: Int): IntakeModel? {
        return intakeDAO.getIntakeByAmount(amountIntake)
    }

}