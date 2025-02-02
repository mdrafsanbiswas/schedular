package com.rafsan.schedular.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ScheduleEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao
}