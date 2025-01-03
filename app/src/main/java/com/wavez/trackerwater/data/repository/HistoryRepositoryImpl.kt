package com.wavez.trackerwater.data.repository

import com.wavez.trackerwater.data.dao.HistoryDAO
import com.wavez.trackerwater.data.model.HistoryModel
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDAO: HistoryDAO
) : HistoryRepository {
    override suspend fun getAll(): List<HistoryModel> {
        return historyDAO.getAll()
    }

    override suspend fun insert(historyModel: HistoryModel) {
        historyDAO.insert(historyModel)
    }

}