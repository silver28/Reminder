package com.positivehc.reminder.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.positivehc.reminder.data.ReminderRepositoryImpl
import com.positivehc.reminder.domain.Event
import com.positivehc.reminder.domain.usecases.GetEventListByDateUseCase
import com.positivehc.reminder.domain.usecases.GetEventListUseCase
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = ReminderRepositoryImpl(app)

    private val getEventListUseCase = GetEventListUseCase(repository)
    private val getEventListByDateUseCase = GetEventListByDateUseCase(repository)

    fun updateDateEventList(date: LocalDate) {
        viewModelScope.launch {
            _dateEventList.postValue(getEventListByDateUseCase(date))
        }
    }

    val eventList = getEventListUseCase()

    private val _dateEventList = MutableLiveData<List<Event>>()
    val dateEventList: LiveData<List<Event>>
        get() = _dateEventList
}