package com.wavez.trackerwater.data.repository.intake

import com.wavez.trackerwater.data.model.IntakeDrink

interface IntakeRepository {
    suspend fun getAll(): List<IntakeDrink>
    suspend fun insert(intakeModel: IntakeDrink)
    suspend fun getIntakeByAmount(amountIntake: Int): IntakeDrink?
}