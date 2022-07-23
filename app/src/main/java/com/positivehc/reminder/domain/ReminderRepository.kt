package com.positivehc.reminder.domain

import androidx.lifecycle.LiveData
import java.time.LocalDate

interface ReminderRepository {

    suspend fun addEvent(event: Event)

    suspend fun editEvent(event: Event)

    suspend fun deleteEvent(event: Event)

    suspend fun getEvent(id: Int): Event

    fun getEventList(): LiveData<List<Event>>

    suspend fun getEventListByDate(date: LocalDate): List<Event>
}