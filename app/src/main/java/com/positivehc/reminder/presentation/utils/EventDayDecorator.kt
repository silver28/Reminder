package com.positivehc.reminder.presentation.utils

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class EventDayDecorator(private val eventDates: Collection<CalendarDay>) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day in eventDates
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan())
    }
}