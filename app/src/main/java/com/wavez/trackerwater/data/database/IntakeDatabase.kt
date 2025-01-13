package com.wavez.trackerwater.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wavez.trackerwater.data.dao.IntakeDAO
import com.wavez.trackerwater.data.model.IntakeDrink

@Database(entities = [IntakeDrink::class], version = 2, exportSchema = false)
abstract class IntakeDatabase: RoomDatabase() {
    abstract fun intakeDao(): IntakeDAO

    companion object {
        val NAME_DB = "intake_database"
    }
}