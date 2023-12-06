package com.example.newproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import com.example.newproject.viewmodel.Schviewmodel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class FixedScheduleDialogFragment : DialogFragment() {
    private lateinit var checkBoxSun: CheckBox
    private lateinit var checkBoxMon: CheckBox
    private lateinit var checkBoxTue: CheckBox
    private lateinit var checkBoxWed: CheckBox
    private lateinit var checkBoxThu: CheckBox
    private lateinit var checkBoxFri: CheckBox
    private lateinit var checkBoxSat: CheckBox
    private lateinit var editTextStartTime: EditText
    private lateinit var editTextEndTime: EditText
    private lateinit var editTextSchedule: EditText
    private lateinit var editTextRecurringPeriod: EditText
    private lateinit var addButton: Button
    private lateinit var cancelButton: Button
    private lateinit var viewModel: Schviewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fixed_schedule_dialog2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkBoxSun = view.findViewById(R.id.checkBoxSun)
        checkBoxMon = view.findViewById(R.id.checkBoxMon)
        checkBoxTue = view.findViewById(R.id.checkBoxTue)
        checkBoxWed = view.findViewById(R.id.checkBoxWed)
        checkBoxThu = view.findViewById(R.id.checkBoxThu)
        checkBoxFri = view.findViewById(R.id.checkBoxFri)
        checkBoxSat = view.findViewById(R.id.checkBoxSat)
        editTextStartTime = view.findViewById(R.id.editTextStartTime)
        editTextEndTime = view.findViewById(R.id.editTextEndTime)
        editTextSchedule = view.findViewById(R.id.editTextSchedule)
        editTextRecurringPeriod = view.findViewById(R.id.editTextRecurringPeriod)
        addButton = view.findViewById(R.id.buttonAdd)
        cancelButton = view.findViewById(R.id.buttonCancel)


        viewModel = ViewModelProvider(this).get(Schviewmodel::class.java)

        addButton.setOnClickListener {
            onAddButtonClicked()
        }
        cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun onAddButtonClicked(){
        val startTime = editTextStartTime.text.toString()
        val endTime = editTextEndTime.text.toString()
        val schedule = editTextSchedule.text.toString()
        val recurringPeriod = editTextRecurringPeriod.text.toString().toInt()

        val startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val checkBoxes = listOf(checkBoxSun, checkBoxMon, checkBoxTue, checkBoxWed, checkBoxThu, checkBoxFri, checkBoxSat)
        val daysOfWeek = listOf(DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)

        for ((checkBox, dayOfWeek) in checkBoxes.zip(daysOfWeek)) {
            if (checkBox.isChecked) {
                for (i in 0 until recurringPeriod) {
                    val week = startOfWeek.plusWeeks((i.toLong()))
                    val event = Event(id = "", startTime = startTime, endTime = endTime, schedule = schedule, isFixed = true)
                    event.setDateFromLocalDate(week.with(TemporalAdjusters.nextOrSame(dayOfWeek)))
                    viewModel.addEvent(event)
                }
            }
        }
        dismiss()
    }

}