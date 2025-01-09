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

    @Query("SELECT * FROM history_table WHERE dateHistory BETWEEN :startDate AND :endDate ORDER BY dateHistory DESC")
    fun getHistoryBetweenDates(startDate: Long, endDate: Long): List<HistoryModel>

    @Query("SELECT * FROM history_table WHERE dateHistory >= :startOfDay AND dateHistory < :endOfDay ORDER BY dateHistory DESC")
    fun getHistoryOfDay(startOfDay: Long, endOfDay: Long): List<HistoryModel>

    @Query("SELECT * FROM history_table WHERE dateHistory >= :startOfWeek AND dateHistory < :endOfWeek ORDER BY dateHistory DESC")
    fun getHistoryOfWeek(startOfWeek: Long, endOfWeek: Long): List<HistoryModel>

    @Query("SELECT * FROM history_table WHERE dateHistory >= :startOfMonth AND dateHistory < :endOfMonth ORDER BY dateHistory DESC")
    fun getHistoryOfMonth(startOfMonth: Long, endOfMonth: Long): List<HistoryModel>
}