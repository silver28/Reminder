package com.positivehc.reminder.presentation.main

import androidx.recyclerview.widget.DiffUtil
import com.positivehc.reminder.domain.Event

class EventDiffCallback: DiffUtil.ItemCallback<Event>() {

    override fun areItemsTheSame(oldItem: Event, newItem: Event) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Event, newItem: Event) = oldItem == newItem
}