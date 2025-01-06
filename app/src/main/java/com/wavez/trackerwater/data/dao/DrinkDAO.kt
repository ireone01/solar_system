package com.wavez.trackerwater.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wavez.trackerwater.data.model.DrinkModel

@Dao
interface DrinkDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(drinkModel: DrinkModel)

    @Update
    suspend fun update(drinkModel: DrinkModel)

    @Delete
    suspend fun delete(drinkModel: DrinkModel)

    @Query("SELECT * FROM drink_table")
    suspend fun getAll(): List<DrinkModel>

    @Query("SELECT * FROM drink_table WHERE amountDrink = :amount LIMIT 1")
    suspend fun getDrinkByAmount(amount: Int): DrinkModel?

}