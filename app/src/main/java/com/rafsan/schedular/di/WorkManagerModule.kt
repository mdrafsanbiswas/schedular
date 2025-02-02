package com.rafsan.schedular.di

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.rafsan.schedular.utility.workUtility.WorkManagerHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object WorkManagerModule {

    @Provides
    fun provideWorkManagerHelper(@ApplicationContext context: Context): WorkManagerHelper {
        return WorkManagerHelper(context)
    }
}