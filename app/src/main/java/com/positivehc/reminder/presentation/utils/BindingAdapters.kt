package com.positivehc.reminder.presentation.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.time.OffsetDateTime

@BindingAdapter("date")
fun bindDate(textView: TextView, dateTime: OffsetDateTime?) {
    textView.text =
        dateTime?.toLocalDate()?.toLocalizedString() ?: ""
}

@BindingAdapter("time")
fun bindTime(textView: TextView, dateTime: OffsetDateTime?) {
    textView.text = dateTime?.toLocalTime()?.toString() ?: ""
}