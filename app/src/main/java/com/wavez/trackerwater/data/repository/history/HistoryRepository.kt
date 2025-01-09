package com.wavez.trackerwater.data.repository.history

import com.wavez.trackerwater.data.model.HistoryModel

interface HistoryRepository {
    suspend fun getAll(): List<HistoryModel>
    suspend fun insert(historyModel: HistoryModel)
    suspend fun delete(historyModel: HistoryModel)
    suspend fun update(historyModel: HistoryModel)
    suspend fun deleteHistoryRecently(amount: Int)
    suspend fun getHistoryBetweenDates(startDate: Long, endDate: Long): List<HistoryModel>

}