package com.wavez.trackerwater.data.repository.history

import com.wavez.trackerwater.data.model.HistoryDrink

interface HistoryRepository {
    suspend fun getAll(): List<HistoryDrink>
    suspend fun insert(historyModel: HistoryDrink)
    suspend fun delete(historyModel: HistoryDrink)
    suspend fun update(historyModel: HistoryDrink)
    suspend fun deleteHistoryRecently(amount: Int)
    suspend fun getHistoryBetweenDates(startDate: Long, endDate: Long): List<HistoryDrink>

}