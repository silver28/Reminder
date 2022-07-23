package com.positivehc.reminder.presentation.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.positivehc.reminder.data.ReminderRepositoryImpl
import com.positivehc.reminder.domain.Event
import com.positivehc.reminder.domain.usecases.AddEventUseCase
import com.positivehc.reminder.domain.usecases.DeleteEventUseCase
import com.positivehc.reminder.domain.usecases.EditEventUseCase
import com.positivehc.reminder.domain.usecases.GetEventUseCase
import com.positivehc.reminder.presentation.utils.DateUtils.Companion.toOffsetDateTime
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime

class DetailViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = ReminderRepositoryImpl(app)

    private val addEventUseCase = AddEventUseCase(repository)
    private val editEventUseCase = EditEventUseCase(repository)
    private val getEventUseCase = GetEventUseCase(repository)
    private val deleteEventUseCase = DeleteEventUseCase(repository)

    private lateinit var initialEvent: Event

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event>
        get() = _event

    private val _isSetAlarm = MutableLiveData<Boolean>()
    val isSetAlarm: LiveData<Boolean>
        get() = _isSetAlarm

    private val _isDescriptionBlankError = MutableLiveData<Boolean>()
    val isDescriptionBlankError: LiveData<Boolean>
        get() = _isDescriptionBlankError

    fun addEvent(date: String, time: String, description: String?) {
        if (!validateDescription(description)) {
            return
        }
        val dateTime: OffsetDateTime?
        try {
            dateTime = toOffsetDateTime(date, time)
        } catch (e: Exception) {
            return
        }
        viewModelScope.launch {
            val newEvent = Event(dateTime!!, description!!)
            addEventUseCase(newEvent)
            _event.value = newEvent
            _isSetAlarm.value = true
        }
    }

    fun editEvent(date: String, time: String, description: String?) {
        if (!validateDescription(description)) {
            return
        }
        val dateTime: OffsetDateTime?
        try {
            dateTime = toOffsetDateTime(date, time)
        } catch (e: Exception) {
            return
        }
        val newEvent = event.value?.copy(eventDateTime = dateTime!!, description = description!!)
        if (newEvent != null) {
            viewModelScope.launch {
                editEventUseCase(newEvent)
                _event.value = newEvent
                _isSetAlarm.value = true
            }
        }
    }

    fun getEvent(eventId: Int) {
        if (event.value == null) {
            viewModelScope.launch {
                _event.value = getEventUseCase(eventId)
                setInitialEvent(event.value!!)
            }
        }
    }

    fun initEvent(dateTime: OffsetDateTime) {
        if (event.value == null) {
            _event.value = Event(eventDateTime = dateTime, description = "")
            setInitialEvent(event.value!!)
        }
    }

    fun updateEventDate(date: LocalDate) {
        _event.value?.let {
            val time = it.eventDateTime.toOffsetTime()
            val dateTime = date.atTime(time)
            _event.value = it.copy(eventDateTime = dateTime)
        }
    }

    fun updateEventTime(time: LocalTime) {
        _event.value?.let {
            val date = it.eventDateTime.toLocalDate()
            _event.value = it.copy(eventDateTime = OffsetDateTime.of(date, time, OffsetDateTime.now().offset))
        }
    }

    fun clearIsSetAlarm() {
        _isSetAlarm.value = false
    }

    fun clearIsDescriptionBlankError() {
        _isDescriptionBlankError.value = false
    }

    fun deleteEvent() {
        viewModelScope.launch {
            event.value?.let {
                deleteEventUseCase(it)
            }
        }
    }

    fun isEventModified(): Boolean {
        return event.value != initialEvent
    }

    private fun setInitialEvent(event: Event) {
        initialEvent = event.copy()
    }

    private fun validateDescription(description: String?): Boolean {
        if (description.isNullOrBlank()) {
            _isDescriptionBlankError.value = true
            return false
        }
        return true
    }
}