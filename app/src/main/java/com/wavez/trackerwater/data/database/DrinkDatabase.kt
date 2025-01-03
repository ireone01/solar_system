package com.wavez.trackerwater.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wavez.trackerwater.data.dao.DrinkDAO
import com.wavez.trackerwater.data.model.DrinkModel

@Database(entities = [DrinkModel::class], version = 1, exportSchema = false)
abstract class DrinkDatabase : RoomDatabase() {
    abstract fun drinkDao(): DrinkDAO

    companion object {
        val NAME_DB = "drink_database"
    }
}