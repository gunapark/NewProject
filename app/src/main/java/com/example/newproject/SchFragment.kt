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




class SchFragment : Fragment(), ScheduleDialogFragment.OnScheduleAddedListener {
    private lateinit var binding: FragmentSchBinding
    private lateinit var adapter: EventAdapter
    private lateinit var viewModel: Schviewmodel
    private var selectedDate: Long = System.currentTimeMillis()
    private fun updateEvents(date: Long) {
        viewModel.getEventsForCurrentDate(date).observe(viewLifecycleOwner, { events ->
            adapter.setEvents(events)
        })
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSchBinding.inflate(inflater, container, false)
        val calendarView: CalendarView = binding.calendarView
        calendarView.date = selectedDate

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.timeInMillis
            updateEvents(selectedDate)
        }
        viewModel = ViewModelProvider(this)[Schviewmodel::class.java]
        adapter = EventAdapter()

        binding.addEventButton.setOnClickListener {
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
        val event = Event(selectedDate, startTime, endTime, schedule)
        val existingEvents = viewModel.getEventsForCurrentDate(selectedDate).value ?: emptyList()
        if (existingEvents.any { it.overlapsWith(event) }) {
            AlertDialog.Builder(context)
                .setTitle("시간 중복")
                .setMessage("선택하신 시간에는 이미 일정이 존재합니다. 그래도 추가하시겠습니까?")
                .setPositiveButton("네") { _, _ ->
                    viewModel.addEvent(event)
                    updateEvents(selectedDate)
                }
                .setNegativeButton("아니요", null)
                .show()
        } else {
            viewModel.addEvent(event)
            updateEvents(selectedDate)
        }
    }
}
