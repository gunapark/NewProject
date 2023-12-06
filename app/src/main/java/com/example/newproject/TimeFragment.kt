package com.example.newproject

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newproject.databinding.FragmentTimeBinding
import com.example.newproject.viewmodel.Schviewmodel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

class TimeFragment : Fragment() {
    private var _binding: FragmentTimeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: Schviewmodel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val today = LocalDate.now()
        val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))

        viewModel.getEventsForWeek("myId", startOfWeek).observe(viewLifecycleOwner) { events ->
            val sortedEvents = events.sortedBy { LocalTime.parse(it.startTime)}
            val eventsByDayOfWeek = sortedEvents.groupBy { it.getDateAsLocalDate().dayOfWeek }

            val sundayEvents = eventsByDayOfWeek[DayOfWeek.SUNDAY] ?: emptyList()
            val sundayAdapter = TimeAdapter(sundayEvents)
            binding.recyclerViewSun.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewSun.adapter = sundayAdapter

            val mondayEvents = eventsByDayOfWeek[DayOfWeek.MONDAY] ?: emptyList()
            val mondayAdapter = TimeAdapter(mondayEvents)
            binding.recyclerViewMon.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewMon.adapter = mondayAdapter

            val tuesdayEvents = eventsByDayOfWeek[DayOfWeek.TUESDAY] ?: emptyList()
            val tuesdayAdapter = TimeAdapter(tuesdayEvents)
            binding.recyclerViewTue.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewTue.adapter = tuesdayAdapter

            val wednesdayEvent = eventsByDayOfWeek[DayOfWeek.WEDNESDAY] ?: emptyList()
            val wednesdayAdapter = TimeAdapter(wednesdayEvent)
            binding.recyclerViewWed.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewWed.adapter = wednesdayAdapter

            val thursdayEvent = eventsByDayOfWeek[DayOfWeek.THURSDAY] ?: emptyList()
            val thursdayAdapter = TimeAdapter(thursdayEvent)
            binding.recyclerViewThu.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewThu.adapter = thursdayAdapter

            val fridayEvent = eventsByDayOfWeek[DayOfWeek.FRIDAY] ?: emptyList()
            val fridayAdapter = TimeAdapter(fridayEvent)
            binding.recyclerViewFri.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewFri.adapter = fridayAdapter

            val saturdayEvent = eventsByDayOfWeek[DayOfWeek.SATURDAY] ?: emptyList()
            val saturdayAdapter = TimeAdapter(saturdayEvent)
            binding.recyclerViewSat.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewSat.adapter = saturdayAdapter

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

    }
}
