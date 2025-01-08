package com.wavez.trackerwater.data.repository.history

import com.wavez.trackerwater.data.dao.HistoryDAO
import com.wavez.trackerwater.data.model.HistoryModel
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDAO
) : HistoryRepository {

    override suspend fun getAll(): List<HistoryModel> {
        return historyDao.getAll()
    }

    override suspend fun delete(historyModel: HistoryModel){
        historyDao.delete(historyModel)
    }

    override suspend fun update(historyModel: HistoryModel){
        historyDao.update(historyModel)
    }

    override suspend fun deleteHistoryRecently(amount: Int) {
        historyDao.deleteLatestHistoryByAmount(amount)
    }

    override suspend fun insert(historyModel: HistoryModel) {
        historyDao.insert(historyModel)
    }

}