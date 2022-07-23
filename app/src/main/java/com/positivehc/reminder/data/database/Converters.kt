package com.positivehc.reminder.data.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val offsetDateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    private val localDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return offsetDateTimeFormatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(offsetDateTimeFormatter)
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let {
            return LocalDate.parse(value, localDateFormatter)
        }
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(localDateFormatter)
    }
}