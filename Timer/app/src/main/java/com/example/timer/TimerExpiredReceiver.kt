package com.example.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("alarm","alarm finished")
        var intent = Intent(context,TimerFinishedActivity::class.java)
        context.startActivity(intent)
    }
}
