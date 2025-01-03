package com.wavez.trackerwater.di

import android.content.Context
import androidx.room.Room
import com.wavez.trackerwater.data.dao.DrinkDAO
import com.wavez.trackerwater.data.dao.HistoryDAO
import com.wavez.trackerwater.data.database.DrinkDatabase
import com.wavez.trackerwater.data.database.HistoryDatabase
import com.wavez.trackerwater.data.repository.DrinkRepository
import com.wavez.trackerwater.data.repository.DrinkRepositoryImpl
import com.wavez.trackerwater.data.repository.HistoryRepository
import com.wavez.trackerwater.data.repository.HistoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun providesDatabaseDrink(
        @ApplicationContext context: Context
    ): DrinkDatabase {
        return Room.databaseBuilder(
            context,
            DrinkDatabase::class.java,
            DrinkDatabase.NAME_DB
        ).build()
    }

    @Provides
    @Singleton
    fun providesDatabaseHis(
        @ApplicationContext context: Context
    ): HistoryDatabase {
        return Room.databaseBuilder(
            context,
            HistoryDatabase::class.java,
            HistoryDatabase.NAME_DB
        ).build()
    }

    @Provides
    @Singleton
    fun providesDrinkDao(appDatabase: DrinkDatabase): DrinkDAO {
        return appDatabase.drinkDao()
    }

    @Provides
    @Singleton
    fun providesHistoryDao(appDatabase: HistoryDatabase): HistoryDAO {
        return appDatabase.historyDao()
    }

    @Provides
    @Singleton
    fun provideDrinkRepository(
        appDao: DrinkDAO
    ): DrinkRepository {
        return DrinkRepositoryImpl(appDao)
    }

    @Provides
    @Singleton
    fun provideHistoryRepository(
        historyDAO: HistoryDAO
    ): HistoryRepository {
        return HistoryRepositoryImpl(historyDAO)
    }

}