package com.example.newproject.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newproject.Event
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.util.Calendar

class SchRepository {
    val database = Firebase.database
    val userRef = database.getReference("user")
    fun addEventToDatabase(userId: String, event: Event) {
        userRef.child(userId).push().setValue(event)
    }

    fun observeSch(userId: String): LiveData<MutableList<Event>> {
        val events = MutableLiveData<MutableList<Event>>()
        userRef.child(userId).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Event>()
                snapshot.children.forEach {childSnapshot ->
                    val event = childSnapshot.getValue(Event::class.java)
                    event?.let {
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
