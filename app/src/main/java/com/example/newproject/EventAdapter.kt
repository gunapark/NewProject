package com.example.newproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter: RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    var events: List<Event> = listOf()
        private set
    fun setEvents(newEvent: List<Event>) {
        events = newEvent
        notifyDataSetChanged()
    }
    class EventViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val starttime: TextView = view.findViewById(R.id.item_start_time)
        val endtime: TextView = view.findViewById(R.id.item_end_time)
        val schedule: TextView = view.findViewById(R.id.item_sch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.starttime.text = event.startTime
        holder.endtime.text = event.endTime
        holder.schedule.text = event.schedule
    }

    override fun getItemCount() = events.size


}