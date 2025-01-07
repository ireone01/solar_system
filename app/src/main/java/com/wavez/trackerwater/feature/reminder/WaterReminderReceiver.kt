package com.wavez.trackerwater.feature.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.wavez.trackerwater.R
import com.wavez.trackerwater.feature.drink.DrinkActivity

class WaterReminderReceiver : BroadcastReceiver() {
    companion object {
        const val CHANNEL_ID = "water_reminder_channel"
        const val CHANNEL_NAME = "Water Reminder"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notificationSound: Uri = Uri.parse("android.resource://${context.packageName}/${R.raw.reminder_sound_1}")

        val notificationIntent = Intent(context, DrinkActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Tạo một ID thông báo duy nhất bằng cách sử dụng System.currentTimeMillis()
        val notificationId = System.currentTimeMillis().toInt()

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_reminders)
            .setContentTitle("Nhắc nhở uống nước")
            .setContentText("Đến giờ uống nước rồi!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOngoing(true)
            .setSound(notificationSound)
            .setVibrate(longArrayOf(0, 1000, 500, 1000))

        // Sử dụng notificationId để đảm bảo rằng thông báo là duy nhất
        notificationManager.notify(notificationId, builder.build())
    }
}