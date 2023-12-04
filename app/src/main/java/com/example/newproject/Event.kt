package com.example.newproject

import androidx.lifecycle.LiveData
import java.time.DayOfWeek
import java.time.LocalDate

data class Event(
    val date: Long = 0L,
    val startTime: String = "",
    val endTime: String = "",
    val schedule: String= ""
) {
    fun overlapsWith(other: Event): Boolean {
        return this.date == other.date && this.startTime < other.endTime && this.endTime > other.startTime
    }

    /*
    fun getEventsForWeek(date: LocalDate): LiveData<List<Event>> {
        val startOfWeek = date.with(DayOfWeek.MONDAY)
        val endOfWeek = startOfWeek.plusDays(6)
        return
    }

     */

}
