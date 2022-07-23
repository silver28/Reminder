package com.positivehc.reminder.presentation.utils

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class DateUtils {
    companion object {
        private val formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)

        fun toOffsetDateTime(date: String?, time: String?): OffsetDateTime? {
            if (date != null && time != null) {
                val localDate = LocalDate.parse(date, formatter)
                val localTime = LocalTime.parse(time)
                return OffsetDateTime.of(localDate, localTime, OffsetDateTime.now().offset)
            }
            return null
        }

        fun nowAsOffsetTime(): OffsetTime {
            val now = OffsetDateTime.now()
            val hour = now.hour
            val minute = now.minute
            return OffsetTime.of(hour, minute, 0, 0, now.offset)
        }
    }
}

fun LocalDate.toCalendarDay(): CalendarDay {
    return CalendarDay.from(org.threeten.bp.LocalDate.of(year, monthValue, dayOfMonth))
}

fun LocalDate.toLocalizedString(): String {
    return format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
}

fun String.toLocalDate(): LocalDate {
    return LocalDate.parse(this, DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
}

fun String.toLocalTime(): LocalTime {
    return LocalTime.parse(this)
}

fun CalendarDay.toLocalDate(): LocalDate {
    return LocalDate.of(date.year, date.monthValue, date.dayOfMonth)
}

