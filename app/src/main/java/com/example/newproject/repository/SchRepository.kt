package com.example.newproject.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newproject.Event
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.*
import java.util.Calendar

class SchRepository {
    val database = Firebase.database
    val userRef = database.getReference("user")
    fun addEventToDatabase(userId: String, event: Event) {
        val key = userRef.child(userId).push().key
        if (key != null) {
            event.id = key
            event.setDateFromLocalDate(event.getDateAsLocalDate())
            userRef.child(userId).child(key).setValue(event)
        }
    }
    fun deleteEventFromDatabase(userId: String, event: Event){
        userRef.child(userId).child(event.id).removeValue()
    }
    fun updateEventInDatabase(userId: String, event: Event) {
        event.setDateFromLocalDate(event.getDateAsLocalDate())
        userRef.child(userId).child(event.id).setValue(event)
    }


    fun observeSch(userId: String): LiveData<MutableList<Event>> {
        val events = MutableLiveData<MutableList<Event>>()
        userRef.child(userId).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Event>()
                snapshot.children.forEach {childSnapshot ->
                    val event = childSnapshot.getValue(Event::class.java)
                    event?.let {
                        it.setDateFromLocalDate(it.getDateAsLocalDate())
                        list.add(it)
                    }
                }
                events.value = list
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return events
    }
    fun getEventsBetween(userId: String, start: LocalDate, end: LocalDate): LiveData<List<Event>> {
        val events = MutableLiveData<List<Event>>()

        userRef.child(userId).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { childSnapshot ->
                    val event = childSnapshot.getValue(Event::class.java)
                    event?.setDateFromLocalDate(event.getDateAsLocalDate())
                    event
                }
                val filteredEvents = list.filter {
                    val date = it.getDateAsLocalDate()
                    !date.isBefore(start) && !date.isAfter(end)
                }
                events.value = filteredEvents
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error here
            }
        })

        return events
    }
    fun getAllEvents(userId: String): LiveData<MutableList<Event>> {
        val events = MutableLiveData<MutableList<Event>>()

        userRef.child(userId).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Event>()
                snapshot.children.forEach {childSnapshot ->
                    val event = childSnapshot.getValue(Event::class.java)
                    event?.let {
                        it.setDateFromLocalDate(it.getDateAsLocalDate())
                        list.add(it)
                    }
                }
                events.value = list
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        return events
    }
}

