package com.example.planter.Api

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.planter.R
import java.util.*


class TimePickerFragment : DialogFragment(){
    lateinit var tvHomeSet : TextView
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        tvHomeSet = requireActivity().findViewById<TextView>(R.id.tvHomeSet)

        val c: Calendar = Calendar.getInstance()
        val hour: Int = c.get(Calendar.HOUR_OF_DAY)
        val minute: Int = c.get(Calendar.MINUTE)

        return TimePickerDialog(
            context,
            { view, hourOfDay, minute ->
                val c = Calendar.getInstance()
                Log.d("test","test")
                c.set(Calendar.HOUR_OF_DAY, hourOfDay)
                c.set(Calendar.MINUTE, minute)
                c.set(Calendar.SECOND, 0)
                Log.d("test2",java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(c.time))

                updateTimeText(c)
                startAlarm(c)
            },
            hour,
            minute,
            DateFormat.is24HourFormat(context)
        )
    }
    fun startAlarm(c: Calendar) {
        val alarmManager: AlarmManager? = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val intent = Intent(context, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }
        alarmManager!!.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
        Log.d("test1","test")
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1*60*1000 ,  pendingIntent);
    }

    fun updateTimeText(c: Calendar){
        var timeText = "알람시간 : "
        timeText += java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(c.time)
        tvHomeSet.text = timeText
        var intent = Intent(requireActivity(), AlertReceiver:: class.java)
        intent.putExtra("time", timeText)
    }

}