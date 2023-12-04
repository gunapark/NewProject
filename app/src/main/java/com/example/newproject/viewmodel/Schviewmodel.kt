package com.example.newproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.newproject.Event
import com.example.newproject.repository.SchRepository
import java.util.Calendar

class Schviewmodel: ViewModel() {
    private var _events = MutableLiveData<MutableList<Event>>(mutableListOf())
    private val repository = SchRepository()


    init{
        val userId = "myId"
        repository.observeSch(userId).observeForever { events->
            _events.value = events
        }
    }
    val events: LiveData<MutableList<Event>> get() = _events

    fun addEvent(event: Event) {
        val userId = "myId"
        repository.addEventToDatabase(userId, event)

        val updatedEvents = _events.value ?: mutableListOf()
        updatedEvents.add(event)
        _events.value = updatedEvents
    }

    fun getEventsForCurrentDate(date: Long): LiveData<List<Event>> {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        return events.map { events ->
            events.filter { event ->
                val eventCalendar = Calendar.getInstance()
                eventCalendar.timeInMillis = event.date
                eventCalendar[Calendar.YEAR] == year &&
                        eventCalendar[Calendar.MONTH] == month &&
                        eventCalendar[Calendar.DAY_OF_MONTH] == day
            }
        }
    }


}
