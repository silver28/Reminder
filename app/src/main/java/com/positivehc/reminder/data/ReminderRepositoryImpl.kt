package com.positivehc.reminder.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.positivehc.reminder.data.database.AppDatabase
import com.positivehc.reminder.domain.Event
import com.positivehc.reminder.domain.ReminderRepository
import java.time.LocalDate

class ReminderRepositoryImpl(application: Application) : ReminderRepository {

    private val dao = AppDatabase.getInstance(application).dao
    private val mapper = EventMapper()

    override suspend fun addEvent(event: Event) {
        dao.insert(mapper.mapEntityToDbModel(event))
    }

    override suspend fun editEvent(event: Event) {
        dao.insert(mapper.mapEntityToDbModel(event))
    }

    override suspend fun deleteEvent(event: Event) {
        dao.delete(mapper.mapEntityToDbModel(event))
    }

    override suspend fun getEvent(id: Int): Event {
        return mapper.mapDbModelToEntity(dao.get(id))
    }

    override fun getEventList(): LiveData<List<Event>> {
        return Transformations.map(dao.getAll()) {
            it.map { mapper.mapDbModelToEntity(it) }
        }
    }

    override suspend fun getEventListByDate(date: LocalDate): List<Event> {
        return dao.getByDate(date).map { mapper.mapDbModelToEntity(it) }
    }
}