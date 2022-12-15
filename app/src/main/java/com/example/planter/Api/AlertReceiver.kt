package com.example.planter.Api

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat


class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationHelper = NotificationHelper(context)
        val nb: NotificationCompat.Builder = notificationHelper.getChannel2Notification("식물에 물을 줘야해요",intent?.extras?.getString("time"))!!
        notificationHelper.getManager()!!.notify(1, nb.build())
    }
}