package com.example.timer

import android.media.Ringtone
import android.media.RingtoneManager
import android.media.RingtoneManager.getActualDefaultRingtoneUri
import android.media.RingtoneManager.getDefaultUri
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_timer_finished.*

class TimerFinishedActivity : AppCompatActivity() {

    var alertSound:Uri = getDefaultUri(RingtoneManager.TYPE_ALARM)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer_finished)
        var ringToneAlarm = startRingTone()


        stopTimer.setOnClickListener{
            stopRingTone(ringToneAlarm)
        }
    }

    fun startRingTone():Ringtone{
        var ringToneAlarm = RingtoneManager.getRingtone(applicationContext,alertSound)
        ringToneAlarm.play()
        return ringToneAlarm
    }

    fun stopRingTone(ringtone: Ringtone){
        ringtone.stop()
        finish()
    }



}
