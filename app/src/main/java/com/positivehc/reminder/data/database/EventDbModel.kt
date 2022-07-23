package com.positivehc.reminder.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.OffsetDateTime

@Entity(tableName = "event")
class EventDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val eventDateTime: OffsetDateTime,
    val description: String
)