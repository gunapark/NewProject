package com.example.newproject.Dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.newproject.data.Event
import com.example.newproject.R
import java.util.Calendar
import java.util.Locale

class ScheduleDialogFragment : DialogFragment() {
    interface OnScheduleAddedListener {
        fun onScheduleAdded(starttime: String, endtime: String, schedule: String)
    }
    private lateinit var listener: OnScheduleAddedListener
    private lateinit var starttimeEditText: EditText
    private lateinit var endtimeEditText: EditText
    private var eventToEdit: Event? = null

    fun setEvent(event: Event){
        eventToEdit = event
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.fragment_schedule_dialog, null)

            starttimeEditText = view.findViewById(R.id.time)
            endtimeEditText = view.findViewById(R.id.time2)
            eventToEdit?.let { event ->
                starttimeEditText.setText(event.startTime)
                endtimeEditText.setText(event.endTime)
            }
            starttimeEditText.setOnClickListener{
                showTimePickerDialog(starttimeEditText)
            }
            endtimeEditText.setOnClickListener {
                showTimePickerDialog(endtimeEditText)
            }
            builder.setView(view)
                .setPositiveButton("추가",
                    DialogInterface.OnClickListener { dialog, id ->
                        val starttime = starttimeEditText.text.toString()
                        val endtime = endtimeEditText.text.toString()
                        val schedule = view.findViewById<EditText>(R.id.schedule).text.toString()

                        listener.onScheduleAdded(starttime, endtime, schedule)
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun showTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            val selectedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
            editText.setText(selectedTime)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    fun setOnScheduleAddedListener(listener: OnScheduleAddedListener) {
        this.listener = listener
    }
}
