package com.wavez.trackerwater.data.repository.intake

import com.wavez.trackerwater.data.model.IntakeModel

interface IntakeRepository {
    suspend fun getAll(): List<IntakeModel>
    suspend fun insert(intakeModel: IntakeModel)
    suspend fun getIntakeByAmount(amountIntake: Int): IntakeModel?
}