package com.example.newproject.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.newproject.Event
import com.example.newproject.R
import com.example.newproject.viewmodel.Schviewmodel
import java.time.DayOfWeek
import java.time.LocalDate

class FixedSchAdapter(private val viewModel: Schviewmodel, private val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<FixedSchAdapter.ViewHolder>() {

    private var events: List<Event> = listOf()

    init {
        val startOfWeek = LocalDate.now().with(DayOfWeek.SUNDAY)
        viewModel.getEventsForWeek("myId", startOfWeek).observe(lifecycleOwner) { updatedEvents ->
            events = updatedEvents.filter { it.isFixed }
            events = events.sortedWith(compareBy({ it.getDateAsLocalDate().dayOfWeek.value % 7 }, { it.startTime }))
            notifyDataSetChanged()
        }
    }


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val dayOfWeek: TextView = view.findViewById(R.id.textView2)
        val startTime: TextView = view.findViewById(R.id.textView3)
        val endTime: TextView = view.findViewById(R.id.textView6)
        val schedule: TextView = view.findViewById(R.id.textView7)
        val delete: TextView = view.findViewById(R.id.textView9)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fixedschedule_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.dayOfWeek.text = event.getDateAsLocalDate().dayOfWeek.toString()
        holder.startTime.text = event.startTime
        holder.endTime.text = event.endTime
        holder.schedule.text = event.schedule
        holder.delete.setOnClickListener {
            viewModel.deleteMatchingEvents(event)
        }
    }

    override fun getItemCount() = events.size
}
