package com.wavez.trackerwater.feature.reminder

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.wavez.trackerwater.R

class WaterReminderService : Service() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        val sharedPreferences = getSharedPreferences("WaterPreferences", Context.MODE_PRIVATE)
//        val waterIntake = sharedPreferences.getInt("WaterIntake", 0)
//
//
//        val notification: Notification = NotificationCompat.Builder(this, "WaterReminderChannel")
//            .setContentTitle("Nhắc nhở uống nước")
//            .setContentText("Bạn đã uống $waterIntake ml nước hôm nay!")
//            .setSmallIcon(R.drawable.ic_reminders)
//            .build()
//
//        startForeground(1, notification)

        // TODO: Cập nhật thông báo liên tục
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "WaterReminderChannel",
                "Water Reminder Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }
}