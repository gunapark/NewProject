package com.example.newproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newproject.databinding.FragmentSchBinding
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

data class Event(val date: Long, val time: String, val schedule: String)

class EventAdapter(private val events: MutableList<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    var currentDate: Long = System.currentTimeMillis()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    class EventViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val time: TextView = view.findViewById(R.id.item_time)
        val schedule: TextView = view.findViewById(R.id.item_sch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getEventsForCurrentDate()[position]
        holder.time.text = event.time
        holder.schedule.text = event.schedule
    }

    override fun getItemCount() = getEventsForCurrentDate().size

    fun addEvent(event: Event) {
        events.add(event)
        events.sortBy {it.time}
        notifyDataSetChanged()
    }


    private fun getEventsForCurrentDate(): List<Event> {
        val calendar1 = Calendar.getInstance().apply { timeInMillis = currentDate }
        return events.filter {
            val calendar2 = Calendar.getInstance().apply { timeInMillis = it.date }
            calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                    calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                    calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
        }
    }
}

class SchFragment : Fragment(), ScheduleDialogFragment.OnScheduleAddedListener {
    private lateinit var binding: FragmentSchBinding
    private lateinit var adapter: EventAdapter
    private var selectedDate: Long = System.currentTimeMillis()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSchBinding.inflate(inflater, container, false)
        val calendarView: CalendarView = binding.calendarView
        calendarView.date = System.currentTimeMillis()

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.timeInMillis
        }
        adapter = EventAdapter(ArrayList())

        binding.addEventButton.setOnClickListener {
            val dialog = ScheduleDialogFragment()
            dialog.setOnScheduleAddedListener(this)
            dialog.show(childFragmentManager, "ScheduleDialogFragment")
        }

        val recyclerView = binding.recSch
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.timeInMillis
            adapter.currentDate = selectedDate
        }


        return binding.root
    }

    override fun onScheduleAdded(time: String, schedule: String) {
        val event = Event(selectedDate, time, schedule)
        adapter.addEvent(event)
    }
}
