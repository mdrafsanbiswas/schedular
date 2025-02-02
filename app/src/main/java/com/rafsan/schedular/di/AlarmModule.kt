package com.rafsan.schedular.di

import android.app.AlarmManager
import android.content.Context
import com.rafsan.schedular.utility.alarmUtility.AlarmScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {

    @Provides
    @Singleton
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager {
        return context.getSystemService(AlarmManager::class.java)
    }

    @Provides
    @Singleton
    fun provideAlarmScheduler(alarmManager: AlarmManager): AlarmScheduler {
        return AlarmScheduler(alarmManager)
    }
}

