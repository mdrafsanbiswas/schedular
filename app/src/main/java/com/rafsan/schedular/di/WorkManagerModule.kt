package com.rafsan.schedular.di

import android.content.Context
import androidx.work.WorkManager
import com.rafsan.schedular.utility.workUtility.WorkManagerHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkManagerModule {

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideWorkManagerHelper(workManager: WorkManager): WorkManagerHelper {
        return WorkManagerHelper(workManager)
    }
}