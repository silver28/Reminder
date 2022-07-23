package com.positivehc.reminder.domain

import java.time.OffsetDateTime

data class Event(
    val eventDateTime: OffsetDateTime,
    var description: String,
    val id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = 0
    }
}