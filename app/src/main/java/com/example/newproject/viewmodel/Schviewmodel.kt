package com.example.newproject.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.newproject.Event
import com.example.newproject.repository.SchRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class Schviewmodel: ViewModel() {
    private var _events = MutableLiveData<MutableList<Event>>(mutableListOf())
    private val repository = SchRepository()



    fun init(lifecycleOwner: LifecycleOwner) {
        val userId = "myId"
        repository.observeSch(userId).observeForever { events ->
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
    fun deleteEvent(event:Event){
        val userId = "myId"
        repository.deletEventFromDatabase(userId, event)
        val updateEvent = _events.value ?: mutableListOf()
        updateEvent.remove(event)
        _events.value = updateEvent
    }
    fun updateEvent(event: Event) {
        val userId = "myId"
        repository.updateEventInDatabase(userId, event)

        val updatedEvents = _events.value ?: mutableListOf()
        val index = updatedEvents.indexOfFirst { it.id == event.id }
        if (index != -1) {
            updatedEvents[index] = event
            _events.value = updatedEvents
        }
    }


    fun getEventsForCurrentDate(date: LocalDate): LiveData<List<Event>> {
        return events.map { events ->
            events.filter { event ->
                event.getDateAsLocalDate() == date
            }.sortedBy { LocalTime.parse(it.startTime) }
        }
    }
    fun getEventsForWeek(userId: String, startOfWeek: LocalDate): LiveData<List<Event>> {
        val endOfWeek = startOfWeek.plusDays(7)
        return repository.getEventsBetween(userId, startOfWeek, endOfWeek)
    }

}
