package com.positivehc.reminder.domain.usecases

import com.positivehc.reminder.domain.Event
import com.positivehc.reminder.domain.ReminderRepository

class EditEventUseCase(private val repository: ReminderRepository) {
    suspend operator fun invoke(event: Event) = repository.editEvent(event)
}