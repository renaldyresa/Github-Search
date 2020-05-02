package com.example.submission2fundamentalandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.example.submission2fundamentalandroid.entity.Reminder
import com.example.submission2fundamentalandroid.service.ReminderReceiver
import com.example.submission2fundamentalandroid.service.ReminderPreference
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var alarmReceiver: ReminderReceiver
    private lateinit var mReminderPreference: ReminderPreference
    private lateinit var reminder: Reminder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        alarmReceiver = ReminderReceiver()

        mReminderPreference = ReminderPreference(this)
        reminder = mReminderPreference.getReminder()
        sw_reminder.isChecked = reminder.isReminder

        tv_change_language.setOnClickListener(this)
        sw_reminder.setOnCheckedChangeListener { _, isChecked ->
            val reminderPreference = ReminderPreference(this)
            if (isChecked){
                alarmReceiver.setReminder(this@SettingActivity)
                reminder.isReminder = true
            }else{
                alarmReceiver.cancelAlarm(this@SettingActivity)
                reminder.isReminder = false
            }
            reminderPreference.setReminder(reminder)
        }


    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.tv_change_language -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }
}
