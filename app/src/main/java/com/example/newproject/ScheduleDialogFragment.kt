package com.example.newproject

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ScheduleDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScheduleDialogFragment : DialogFragment() {
    interface OnScheduleAddedListener {
        fun onScheduleAdded(time: String, schedule: String)
    }
    private lateinit var listener: OnScheduleAddedListener
    private lateinit var timeEditText: EditText
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.fragment_schedule_dialog, null)

            timeEditText = view.findViewById(R.id.time)
            timeEditText.setOnClickListener{
                showTimePickerDialog()
            }
            builder.setView(view)
                .setPositiveButton("추가",
                    DialogInterface.OnClickListener { dialog, id ->
                        val time = timeEditText.text.toString()
                        val schedule = view.findViewById<EditText>(R.id.schedule).text.toString()
                        listener.onScheduleAdded(time, schedule)
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            val selectedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
            timeEditText.setText(selectedTime)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    fun setOnScheduleAddedListener(listener: OnScheduleAddedListener) {
        this.listener = listener
    }
}
