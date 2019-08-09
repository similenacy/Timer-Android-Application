package com.example.timer.Util

import android.content.Context
import android.preference.PreferenceManager
/*
Class used to store the timer variables and to get and set the alarm time.
This is used in the Timer Activity
 */

class TimerUtil {
    companion object {
        var mins  = 0L
        var seconds = 0L

        var initialMins = 0L
        var initialSecs = 0L

        private const val BACKGROUNDTIMER_SET_TIME_ID = "backgroundTimer"

        fun getAlarmSetTime(context: Context):Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(BACKGROUNDTIMER_SET_TIME_ID,0)
        }

        fun setAlarmTime(time:Long,context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(BACKGROUNDTIMER_SET_TIME_ID,time)
            editor.apply()
        }

    }
}