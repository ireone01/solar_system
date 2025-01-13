package com.wavez.trackerwater.data.repository.history

import com.wavez.trackerwater.data.dao.HistoryDAO
import com.wavez.trackerwater.data.model.HistoryDrink
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDAO
) : HistoryRepository {

    override suspend fun getAll(): List<HistoryDrink> {
        return historyDao.getAll()
    }

    override suspend fun delete(historyModel: HistoryDrink){
        historyDao.delete(historyModel)
    }

    override suspend fun update(historyModel: HistoryDrink){
        historyDao.update(historyModel)
    }

    override suspend fun deleteHistoryRecently(amount: Int) {
        historyDao.deleteLatestHistoryByAmount(amount)
    }

    override suspend fun getHistoryBetweenDates(
        startDate: Long,
        endDate: Long
    ): List<HistoryDrink> {
        return historyDao.getHistoryBetweenDates(startDate, endDate)
    }

    override suspend fun insert(historyModel: HistoryDrink) {
        historyDao.insert(historyModel)
    }

}