package com.positivehc.reminder.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.positivehc.reminder.databinding.ItemEventBinding
import com.positivehc.reminder.domain.Event

class EventAdapter: ListAdapter<Event, EventAdapter.EventViewHolder>(EventDiffCallback()) {

    var onClickListener: ((Event) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onClickListener)
    }

    class EventViewHolder(private val binding: ItemEventBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event, onClickListener: ((Event) -> Unit)?) {
            binding.tvDescription.text = event.description
            binding.tvTime.text = event.eventDateTime.toLocalTime().toString()
            if (onClickListener != null) {
                binding.root.setOnClickListener {
                    onClickListener(event)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): EventViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemEventBinding.inflate(layoutInflater, parent, false)
                return EventViewHolder(binding)
            }
        }
    }
}