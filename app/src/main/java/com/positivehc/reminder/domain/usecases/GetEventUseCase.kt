package com.positivehc.reminder.domain.usecases

import com.positivehc.reminder.domain.Event
import com.positivehc.reminder.domain.ReminderRepository

class GetEventUseCase(private val repository: ReminderRepository) {
    suspend operator fun invoke(id: Int): Event {
        return repository.getEvent(id)
    }
}