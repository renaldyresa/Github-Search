package com.example.submission2fundamentalandroid.service

import android.content.Context
import androidx.core.content.edit
import com.example.submission2fundamentalandroid.entity.Reminder

class ReminderPreference(context: Context) {

    companion object {
        private const val PREFS_NAME = "reminder_pref"
        private const val REMINDER = "isreminder"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)


    fun setReminder(value: Reminder){
        preferences.edit{
            putBoolean(REMINDER, value.isReminder)
        }
    }

    fun getReminder(): Reminder {
        val model = Reminder()
        model.isReminder = preferences.getBoolean(REMINDER, false)
        return model
    }

}