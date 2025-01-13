package com.wavez.trackerwater.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wavez.trackerwater.data.dao.HistoryDAO
import com.wavez.trackerwater.data.model.HistoryDrink

@Database(entities = [HistoryDrink::class], version = 1, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDAO

    companion object {
        val NAME_DB = "history_database"
    }
}