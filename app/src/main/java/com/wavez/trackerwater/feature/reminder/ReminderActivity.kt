package com.wavez.trackerwater.feature.reminder

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lingvo.base_common.ui.BaseActivity
import com.wavez.trackerwater.R
import com.wavez.trackerwater.databinding.ActivityReminderBinding
import com.wavez.trackerwater.util.SPUtils
import java.util.Calendar

class ReminderActivity : BaseActivity<ActivityReminderBinding>() {
    override fun createBinding(): ActivityReminderBinding {
        return ActivityReminderBinding.inflate(layoutInflater)
    }

    private var isReminder = false

    override fun initConfig(savedInstanceState: Bundle?) {
        super.initConfig(savedInstanceState)

        isReminder = SPUtils.getBoolean(this, SPUtils.REMINDER, false)

        if (isReminder) {
            SPUtils.setBoolean(this, SPUtils.REMINDER, isReminder)
            binding.ivSwReminder.setImageResource(R.drawable.ic_sw_reminder_on)
        } else {
            SPUtils.setBoolean(this, SPUtils.REMINDER, isReminder)
            binding.ivSwReminder.setImageResource(R.drawable.img_sw_reminder_off)
        }
    }

    override fun initObserver() {
        super.initObserver()
    }

    override fun initListener() {
        super.initListener()
        binding.ivBack.setOnClickListener { finish() }
        binding.ivSwReminder.setOnClickListener {
            isReminder = !isReminder
            if (isReminder) {
                SPUtils.setBoolean(this, SPUtils.REMINDER, isReminder)
                binding.ivSwReminder.setImageResource(R.drawable.ic_sw_reminder_on)
                NotificationScheduler.scheduleNotification(this, 16, 2)
            } else {
                SPUtils.setBoolean(this, SPUtils.REMINDER, isReminder)
                binding.ivSwReminder.setImageResource(R.drawable.img_sw_reminder_off)
                NotificationScheduler.cancelNotification(this)
            }
        }
    }

}