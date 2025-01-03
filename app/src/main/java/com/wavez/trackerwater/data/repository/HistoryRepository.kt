package com.wavez.trackerwater.data.repository

import com.wavez.trackerwater.data.model.HistoryModel

interface HistoryRepository {
    suspend fun getAll(): List<HistoryModel>

    suspend fun insert(historyModel: HistoryModel)
}