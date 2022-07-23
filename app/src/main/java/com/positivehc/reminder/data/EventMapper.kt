package com.positivehc.reminder.data

import com.positivehc.reminder.data.database.EventDbModel
import com.positivehc.reminder.domain.Event

class EventMapper {

    fun mapEntityToDbModel(event: Event): EventDbModel {
        with (event) {
            return EventDbModel(
                id = id,
                eventDateTime = eventDateTime,
                description = description
            )
        }
    }

    fun mapDbModelToEntity(dbModel: EventDbModel): Event {
        with (dbModel) {
            return Event(
                id = id,
                eventDateTime = eventDateTime,
                description = description
            )
        }
    }
}