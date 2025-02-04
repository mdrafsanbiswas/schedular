package com.rafsan.schedular.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllApps(apps: List<ScheduleEntity>)

    @Query("UPDATE schedules SET scheduleTime = null, completed = 0, isScheduled = 0 WHERE packageName = :packageName")
    suspend fun resetScheduledApp(packageName: String)

    @Query("SELECT * FROM schedules")
    fun getAllApps(): Flow<List<ScheduleEntity>>

    @Query("SELECT * FROM schedules WHERE completed = 1")
    fun getAllCompletedSchedules(): Flow<List<ScheduleEntity>>

    @Query("UPDATE schedules SET completed = 1 WHERE packageName = :packageName")
    suspend fun markScheduleAsComplete(packageName :String)

    @Query("SELECT * FROM schedules WHERE packageName = :packageName LIMIT 1")
    suspend fun getScheduledInfo(packageName: String): ScheduleEntity?

    @Query("UPDATE schedules SET isScheduled = 1, scheduleTime = :scheduleTime WHERE packageName = :packageName")
    suspend fun scheduleApp(packageName: String, scheduleTime: Long)

}
