package com.example.newproject

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newproject.databinding.FragmentSchBinding
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar
import com.example.newproject.viewmodel.Schviewmodel
import com.example.newproject.EventAdapter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class SchFragment : Fragment(), ScheduleDialogFragment.OnScheduleAddedListener, EventAdapter.OnItemActionListener {
    private lateinit var binding: FragmentSchBinding
    private lateinit var adapter: EventAdapter
    private lateinit var viewModel: Schviewmodel
    private var selectedDate: LocalDate = LocalDate.now()

    private fun updateEvents(date: LocalDate) {
        viewModel.getEventsForCurrentDate(date).observe(viewLifecycleOwner, { events ->
            adapter.setEvents(events)
        })
    }

    override fun onEditClicked(event: Event) {
        val dialog = ScheduleDialogFragment()
        dialog.setEvent(event)
        dialog.setOnScheduleAddedListener(object : ScheduleDialogFragment.OnScheduleAddedListener {
            override fun onScheduleAdded(startTime: String, endTime: String, schedule: String) {
                event.startTime = startTime
                event.endTime = endTime
                event.schedule = schedule
                viewModel.updateEvent(event)
                updateEvents(selectedDate)
            }
        })
        dialog.show(childFragmentManager, "ScheduleDialogFragment")
    }


    override fun onDeleteClicked(event: Event) {
        viewModel.deleteEvent(event)
        updateEvents(selectedDate)
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSchBinding.inflate(inflater, container, false)
        val calendarView: CalendarView = binding.calendarView

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            updateEvents(selectedDate)
        }
        viewModel = ViewModelProvider(this)[Schviewmodel::class.java]
        viewModel.init(this)
        adapter = EventAdapter()
        adapter.setOnItemActionListener(this)

        binding.timeSchAdd.setOnClickListener {
            val dialog = ScheduleDialogFragment()
            dialog.setOnScheduleAddedListener(this)
            dialog.show(childFragmentManager, "ScheduleDialogFragment")
        }

        val recyclerView = binding.recSch
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        updateEvents(selectedDate)

        return binding.root
    }

    override fun onScheduleAdded(startTime: String, endTime:String, schedule: String) {
        val event = Event(id = "", date = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), startTime, endTime, schedule, isFixed = false)
        viewModel.addEvent(event)
        updateEvents(selectedDate)
    }
}
