package com.wavez.trackerwater.di

import android.content.Context
import androidx.room.Room
import com.wavez.trackerwater.data.dao.HistoryDAO
import com.wavez.trackerwater.data.dao.IntakeDAO
import com.wavez.trackerwater.data.database.HistoryDatabase
import com.wavez.trackerwater.data.database.IntakeDatabase
import com.wavez.trackerwater.data.repository.history.HistoryRepository
import com.wavez.trackerwater.data.repository.history.HistoryRepositoryImpl
import com.wavez.trackerwater.data.repository.intake.IntakeRepository
import com.wavez.trackerwater.data.repository.intake.IntakeRepositoryImpl
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
    fun providesDatabaseHistory(
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
    fun providesDatabaseIntake(
        @ApplicationContext context: Context
    ): IntakeDatabase {
        return Room.databaseBuilder(
            context,
            IntakeDatabase::class.java,
            IntakeDatabase.NAME_DB
        ).build()
    }

    @Provides
    @Singleton
    fun provideshistoryDao(appDatabase: HistoryDatabase): HistoryDAO {
        return appDatabase.historyDao()
    }

    @Provides
    @Singleton
    fun providesIntakeDao(appDatabase: IntakeDatabase): IntakeDAO {
        return appDatabase.intakeDao()
    }

    @Provides
    @Singleton
    fun provideHistoryRepository(
        appDao: HistoryDAO
    ): HistoryRepository {
        return HistoryRepositoryImpl(appDao)
    }

    @Provides
    @Singleton
    fun provideIntakeRepository(
        appDao: IntakeDAO
    ): IntakeRepository {
        return IntakeRepositoryImpl(appDao)
    }

}