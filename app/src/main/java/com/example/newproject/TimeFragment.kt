package com.example.newproject

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.newproject.databinding.FragmentTimeBinding
import com.example.newproject.viewmodel.Schviewmodel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TimeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimeFragment : Fragment() {
    private lateinit var binding: FragmentTimeBinding
    private val viewModel: Schviewmodel by activityViewModels()

    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the RecyclerView
        val adapter = EventAdapter()
        binding.recyclerView.adapter = adapter

        // Observe the shared data and update the UI

        viewModel.events.observe(viewLifecycleOwner, { events ->
            adapter.submitList(events)
        })



        // Add event handling - This is just an example. You need to implement actual logic.
        binding.addEventButton.setOnClickListener {
            val newEvent = Event(/* ... */)
            viewModel.addEvent(newEvent)
        }
    }


/*

     */

     */

}
