package com.positivehc.reminder.domain.usecases

import com.positivehc.reminder.domain.Event
import com.positivehc.reminder.domain.ReminderRepository
import java.time.LocalDate

class GetEventListByDateUseCase(private val repository: ReminderRepository) {
    suspend operator fun invoke(date: LocalDate): List<Event> {
        return repository.getEventListByDate(date)
    }
}