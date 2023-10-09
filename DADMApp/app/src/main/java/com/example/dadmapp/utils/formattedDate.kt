package com.example.dadmapp.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun formattedDateStr(date: String): String {
    val ldt = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)

    return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(ldt)
}

fun formattedTimeStr(date: String): String {
    val ldt = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)

    return DateTimeFormatter.ofPattern("HH:mm").format(ldt)
}