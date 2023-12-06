package com.example.newproject

import androidx.lifecycle.LiveData
import com.google.firebase.database.Exclude
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class Event(
    var id: String = "",
    var date: String = "",
    var startTime: String = "",
    var endTime: String = "",
    var schedule: String= "",
    var isFixed: Boolean = false,
    ) {
    @Exclude
    fun getDateAsLocalDate(): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return LocalDate.parse(date, formatter)
    }

    fun setDateFromLocalDate(localDate: LocalDate) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        date = localDate.format(formatter)
    }
}