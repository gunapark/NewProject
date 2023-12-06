package com.example.newproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newproject.Event
import com.example.newproject.R

class EventAdapter: RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    var events: List<Event> = listOf()
        private set
    fun setEvents(newEvent: List<Event>) {
        events = newEvent
        notifyDataSetChanged()
    }

    interface OnItemActionListener{
        fun onEditClicked(event: Event)
        fun onDeleteClicked(event: Event)
    }

    private var onItemActionListener: OnItemActionListener? = null

    fun setOnItemActionListener(listener: OnItemActionListener) {
        onItemActionListener = listener
    }
    class EventViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val starttime: TextView = view.findViewById(R.id.item_start_time)
        val endtime: TextView = view.findViewById(R.id.item_end_time)
        val schedule: TextView = view.findViewById(R.id.item_sch)
        val edit: TextView = view.findViewById(R.id.edit_sch)
        val delete: TextView = view.findViewById(R.id.del_Sch)
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
        holder.edit.setOnClickListener { onItemActionListener?.onEditClicked(event) }
        holder.delete.setOnClickListener { onItemActionListener?.onDeleteClicked(event) }
    }

    override fun getItemCount() = events.size


}