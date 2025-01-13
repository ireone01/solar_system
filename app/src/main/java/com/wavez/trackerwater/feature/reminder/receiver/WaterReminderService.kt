package com.wavez.trackerwater.feature.reminder.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
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
        val sharedPreferences = getSharedPreferences("WaterPreferences", Context.MODE_PRIVATE)
        val waterIntake = sharedPreferences.getInt("WaterIntake", 0)

//        // Inflate the custom layout for the notification
//        val remoteViews = RemoteViews(packageName, R.layout.notification_layout)
//        remoteViews.setTextViewText(R.id.notification_title, getString(R.string.noti_title))
//
//        // Đảm bảo chèn đúng thông tin waterIntake
//        val message = getString(R.string.noti_content, waterIntake)
//        remoteViews.setTextViewText(R.id.notification_message, message)
//
//        // Create the notification
//        val notification: Notification = NotificationCompat.Builder(this, "WaterReminderChannel")
//            .setContent(remoteViews) // Use RemoteViews for custom layout
//            .setSmallIcon(R.drawable.ic_reminders)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .build()

        val notification: Notification = NotificationCompat.Builder(this, "WaterReminderChannel")
            .setContentTitle("Nhắc nhở uống nước")
            .setContentText("Bạn đã uống $waterIntake ml nước hôm nay!")
            .setSmallIcon(R.drawable.ic_reminders)
            .build()

        startForeground(1, notification)

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