package com.wavez.trackerwater.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wavez.trackerwater.data.model.IntakeDrink

@Dao
interface IntakeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(intakeModel: IntakeDrink)

    @Query("SELECT * FROM intake_table")
    suspend fun getAll(): List<IntakeDrink>

    @Query("SELECT * FROM intake_table WHERE amountIntake = :amount LIMIT 1")
    suspend fun getIntakeByAmount(amount: Int): IntakeDrink?

}