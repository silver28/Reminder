package com.positivehc.reminder.domain.usecases

import androidx.lifecycle.LiveData
import com.positivehc.reminder.domain.Event
import com.positivehc.reminder.domain.ReminderRepository

class GetEventListUseCase(private val repository: ReminderRepository) {
    operator fun invoke(): LiveData<List<Event>> {
        return repository.getEventList()
    }
}