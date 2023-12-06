package com.example.newproject.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.newproject.Event
import com.example.newproject.repository.SchRepository
import java.time.DayOfWeek
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
        repository.deleteEventFromDatabase(userId, event)
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
    fun deleteMatchingEvents(event: Event) {
        val userId = "myId"
        val allEventsLiveData = repository.getAllEvents(userId) // 데이터베이스에서 모든 이벤트를 가져옴

        allEventsLiveData.observeForever { allEvents ->
            val eventsToDelete = allEvents.filter {
                it.startTime == event.startTime &&
                        it.endTime == event.endTime &&
                        it.schedule == event.schedule &&
                        it.isFixed
            }
            for (eventToDelete in eventsToDelete) {
                repository.deleteEventFromDatabase(userId, eventToDelete)
            }
            val updatedEventsLiveData = repository.getAllEvents(userId)
            updatedEventsLiveData.observeForever { updatedEvents ->
                _events.value = updatedEvents
            }
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
        val endOfWeek = startOfWeek.plusDays(6)
        return repository.getEventsBetween(userId, startOfWeek, endOfWeek)
    }

}
