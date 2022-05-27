package com.example.countdown

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment(val listener: (String) -> Unit): DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR)
        val minutes = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            activity as Context,
            this,
            hour,
            minutes,
            true
        )
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        listener("$p1:$p2")
    }
}