package com.example.timer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity;
import com.example.timer.Util.TimerUtil

import kotlinx.android.synthetic.main.activity_timer.*
import kotlinx.android.synthetic.main.content_timer.*
import java.text.DecimalFormat
import java.util.*

class TimerActivity : AppCompatActivity() {

/*
The companion object is used to enable the background timer so that the timer will still run even when the application is out of
scope on the android OS (another app is being used)
 */
    companion object {
        var isFinished = false

        fun setBackgroundTimer(context: Context, nowSeconds:Long, minsRemaining:Long,secsRemaining:Long):Long{
            val finishTime = (nowSeconds + secsRemaining + (minsRemaining * 60)) * 1000
            val backgroundTimerManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context,TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context,0,intent,0)
            backgroundTimerManager.setExact(AlarmManager.RTC_WAKEUP, finishTime, pendingIntent)
            TimerUtil.setAlarmTime(currentSeconds,context)
            return finishTime
        }

        fun removeBackgrountTimer(context: Context){
            val intent = Intent(context,TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context,0,intent,0)
            val backgroundTimerManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            backgroundTimerManager.cancel(pendingIntent)
            TimerUtil.setAlarmTime(0,context)
        }


        val currentSeconds:Long
            get()= Calendar.getInstance().timeInMillis / 1000
    }



/*
Enum class to determine the timer state (Stopped,Paused,Running). This is used to know when to save/load variables
and which ones to save/load.
 */
    enum class TimerState {
        Stopped, Paused, Running
    }

    private lateinit var countTimer: CountDownTimer
    private var timerState = TimerState.Stopped

    private var minsRemaining = 0L
    private var secsRemaining = 0L

/*
Function that get's called when the activity is started. It adds the correct functions to all the buttons and saves the state
of the timer so that it can be stopped and restarted.
 */
    override fun onCreate(savedInstanceState: Bundle?) {

        var timer = intent.getParcelableExtra<Timer>("timer")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setSupportActionBar(toolbar)
        setTitle("Timer: " + timer.name)
        val initialTimer: String =
            DecimalFormat("00").format((timer.mins)).toString() + ":" + DecimalFormat("00").format((timer.secs)).toString()

        minsRemaining = timer.mins.toLong()
        secsRemaining = timer.secs.toLong()

        saveState()
        saveInitialState()

        timerCountDown.text = initialTimer

        fab_start.setOnClickListener { view ->
            startTimer()
        }


        fab_pause.setOnClickListener { view ->
            pauseTimer()
        }
        fab_stop.setOnClickListener { view ->
            stopTimer()
        }


    }

    /*
    Function that gets called when the activity is resumed.
     */
    override fun onResume() {
        super.onResume()
        removeBackgrountTimer(this)

    }

/*
Function that sets background timer if the timer is running and saves the state
 */
    override fun onPause() {
        super.onPause()

        if(timerState== TimerState.Running && !isFinished){
            val finishTime = setBackgroundTimer(this, currentSeconds,minsRemaining,secsRemaining)

        }
        else if(timerState == TimerState.Paused){
            //Show notification
        }
        saveState()

    }
/*
This function is used to start a timer. It will used the minsReaming and secsRemaining to countdown from
When finished it will take the user to the TimerFinished activity
 */
    fun startTimer(){
        timerState = TimerState.Running
        updateButtons()
        getState()
        var timerMilli: Long = (minsRemaining * 60000) + (secsRemaining * 1000)

        countTimer = object : CountDownTimer(timerMilli, 1000) {
            override fun onFinish() {
                timerCountDown.text = "00:00"
                timerState = TimerState.Stopped
                getInitialState()
                updateButtons()
                goToAlarmFinishedActivity()
                displayTimer()


            }

            override fun onTick(p0: Long) {
                minsRemaining = (p0 / 1000) / 60
                secsRemaining = (p0 / 1000) % 60
                displayTimer()
            }
        }.start()
    }

    /*
    Function to jump to the next activity which happens when the timer is finished
     */
    fun goToAlarmFinishedActivity(){
        isFinished = true
        var intent = Intent(applicationContext,TimerFinishedActivity::class.java)
        startActivity(intent)
    }
/*
This function will enable/disable buttons depending on the state of the timer
 */
    fun updateButtons(){
        when(timerState){
            TimerState.Running->{
                fab_start.isEnabled = false
                fab_pause.isEnabled = true
                fab_stop.isEnabled = true
            }
            TimerState.Paused->{
                fab_start.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = true
            }
            TimerState.Stopped->{
                fab_start.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = false
            }
        }
    }
/*
Displays the current timer remanining time
 */
    fun displayTimer(){
        timerCountDown.text = DecimalFormat("00").format(minsRemaining).toString() + ":" + DecimalFormat("00").format(secsRemaining).toString()
    }
/*
Function to pause the timer
 */
    fun pauseTimer(){
        timerState = TimerState.Paused
        updateButtons()
        countTimer.cancel()
        saveState()

    }
/*
Function to stop the timer
 */
    fun stopTimer(){
        timerState = TimerState.Stopped
        updateButtons()
        countTimer.cancel()
        getInitialState()

    }
/*
Saves the state of the variables. This is used when the timer is paused and has to be resumed.
The variables are saved in the TimerUtil class.
 */
    fun saveState(){
        TimerUtil.mins = minsRemaining
        TimerUtil.seconds=secsRemaining
    }
/*
Retrieves the state, this is used when the variables need to be retrieved from the TimerUtil class
 */
    fun getState(){
        minsRemaining = TimerUtil.mins
        secsRemaining = TimerUtil.seconds
    }
/*
Saves the initial state, This is used when a timer is stopped and must be resumed. If previously paused the saved variables
will be wrong and therefore an second set must be saved
 */
    fun saveInitialState(){
        TimerUtil.initialMins = minsRemaining
        TimerUtil.initialSecs = secsRemaining
    }
/*
Retrieves the initial state of the timer from the TimerUtil class. Used when resuming after timer has been paused.
 */
    fun getInitialState(){
        minsRemaining = TimerUtil.initialMins
        secsRemaining = TimerUtil.initialSecs
    }

}
