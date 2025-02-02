package com.rafsan.schedular.di

import android.app.Application
import androidx.room.Room
import com.rafsan.schedular.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) : AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "schedules")
            .build()
}