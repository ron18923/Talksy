package com.example.talksy.presentation.reusable.nonComposables

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    return format.format(date)
}