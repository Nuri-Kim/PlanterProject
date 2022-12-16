package com.example.planter.Api

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.graphics.toColorInt
import com.example.planter.MainActivity


class NotificationHelper(base: Context?) : ContextWrapper(base) {
    val channel1ID: String = "channel1ID";
    val channel1Name: String = "channel1 ";
    val channel2ID: String = "channel2ID";
    val channel2Name: String = "channel2 ";

    //클릭하면 이벤트 발생
    val intent = Intent(base, MainActivity::class.java)
    val fullScreenPendingIntent = PendingIntent.getActivity(baseContext, 0,
        intent, PendingIntent.FLAG_UPDATE_CURRENT)

 init {
     //
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
         createChannels()
     }
 }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannels() {
        //메시지 알람채널
//        val channel1 =
//            NotificationChannel(channel1ID, channel1Name, NotificationManager.IMPORTANCE_DEFAULT)
//        channel1.enableLights(true)
//        channel1.enableVibration(true)
//        channel1.lightColor = "#0D6B3E".toColorInt()
//        channel1.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
//        getManager()?.createNotificationChannel(channel1)

        //물주기 알람채널
        val channel2 =
            NotificationChannel(channel2ID, channel2Name, NotificationManager.IMPORTANCE_HIGH)
        channel2.enableLights(true)
        channel2.enableVibration(true)
        channel2.lightColor = "#0D6B3E".toColorInt()
        channel2.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        getManager().createNotificationChannel(channel2)
    }

    fun getManager(): NotificationManager {
        return getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

//    fun getChannel1Notification(title: String?, message: String?): NotificationCompat.Builder? {
//        return NotificationCompat.Builder(applicationContext, channel1ID)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setSmallIcon(R.drawable.ic_one)
//    }

    fun getChannel2Notification(title: String?, message: String?): NotificationCompat.Builder? {
        return NotificationCompat.Builder(applicationContext, channel2ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.stat_notify_sync)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(fullScreenPendingIntent, true)
    }

}