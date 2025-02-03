package com.rafsan.schedular.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey val packageName: String,
    val appName: String = "",
    val scheduleTime: Long? = null,
    val completed: Boolean = false,
    val isScheduled: Boolean = false,
    val appIcon: ByteArray? = null
)