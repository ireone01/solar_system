package com.wavez.trackerwater.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.wavez.trackerwater.data.model.HistoryModel

@Dao
interface HistoryDAO {
    @Insert
    fun insert(historyModel: HistoryModel)

    @Query("SELECT * FROM history_table")
    fun getAll(): List<HistoryModel>
}