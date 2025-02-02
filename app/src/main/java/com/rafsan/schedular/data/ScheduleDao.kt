package com.rafsan.schedular.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Insert
    suspend fun insertSchedule(schedule: ScheduleEntity)

    @Delete
    suspend fun deleteSchedule(schedule: ScheduleEntity)

    @Query("SELECT * FROM schedules")
    fun getAllSchedules(): Flow<List<ScheduleEntity>>

    @Query("SELECT * FROM schedules WHERE completed = 1")
    fun getAllCompletedSchedules(): Flow<List<ScheduleEntity>>

    @Query("UPDATE schedules SET completed = 1 WHERE id = :scheduleId")
    suspend fun markAsCompleted(scheduleId: Int)
}
