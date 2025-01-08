package com.wavez.trackerwater.data.dao

import androidx.room.*
import com.wavez.trackerwater.data.model.HistoryModel

@Dao
interface HistoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyModel: HistoryModel)

    @Update
    suspend fun update(historyModel: HistoryModel)

    @Delete
    suspend fun delete(historyModel: HistoryModel)

    @Query("SELECT * FROM history_table")
    suspend fun getAll(): List<HistoryModel>

    @Query("""
        DELETE FROM history_table
        WHERE id = (
            SELECT id FROM history_table
            WHERE amountHistory = :amount
            ORDER BY dateHistory DESC LIMIT 1
        )
    """)
    fun deleteLatestHistoryByAmount(amount: Int)

}