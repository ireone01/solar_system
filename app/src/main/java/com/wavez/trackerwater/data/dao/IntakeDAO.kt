package com.wavez.trackerwater.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wavez.trackerwater.data.model.IntakeModel

@Dao
interface IntakeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(intakeModel: IntakeModel)

    @Query("SELECT * FROM intake_table")
    suspend fun getAll(): List<IntakeModel>

    @Query("SELECT * FROM intake_table WHERE amountIntake = :amount LIMIT 1")
    suspend fun getIntakeByAmount(amount: Int): IntakeModel?

}