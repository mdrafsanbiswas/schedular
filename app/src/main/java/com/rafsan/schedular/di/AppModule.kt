package com.rafsan.schedular.di

import com.rafsan.schedular.data.AppDatabase
import com.rafsan.schedular.repo.ScheduleRepository
import com.rafsan.schedular.utility.workUtility.WorkManagerHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideScheduleRepository(
        database: AppDatabase,
        workManagerHelper: WorkManagerHelper
    ): ScheduleRepository {
        return ScheduleRepository(database, workManagerHelper)
    }
}