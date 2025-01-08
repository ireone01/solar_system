package com.wavez.trackerwater.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wavez.trackerwater.data.dao.HistoryDAO
import com.wavez.trackerwater.data.dao.IntakeDAO
import com.wavez.trackerwater.data.model.HistoryModel
import com.wavez.trackerwater.data.model.IntakeModel

@Database(entities = [IntakeModel::class], version = 2, exportSchema = false)
abstract class IntakeDatabase: RoomDatabase() {
    abstract fun intakeDao(): IntakeDAO

    companion object {
        val NAME_DB = "intake_database"
    }
}