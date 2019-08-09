package com.example.timer

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.NumberPicker
import com.example.timer.Util.Preferences
import kotlinx.android.synthetic.main.activity_sub_category.*
import kotlinx.android.synthetic.main.content_sub_category.*
import kotlinx.android.synthetic.main.timer_add_dialogue_numberpicker.*
import kotlin.concurrent.timer

class sub_category : AppCompatActivity() {

    /*
    Function that gets created once activity is started
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        var catPosition = intent.getIntExtra("catPosition",0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_category)
        setSupportActionBar(toolbarSub)
        title = CategoryList.listOfCategory[catPosition].name

        createRecycleView(catPosition)

        fab_addTimer.setOnClickListener { view ->

            if(Preferences.getNumberPicker()) {
                createDialoguePicker(catPosition)
            }
            else{
                createDialogue(catPosition)
            }
        }


    }

    /*
    Function to create the dialogue for adding a new timer. This one uses normal text fields
     */
    fun createDialogue(catPos:Int){
        val dialog = AlertDialog.Builder(this)
        val dialogView =layoutInflater.inflate(R.layout.timer_add_dialogue, null)
        val timerName = dialogView.findViewById<EditText>(R.id.timerName)
        val timerMins = dialogView.findViewById<EditText>(R.id.timerMins)
        val timerSecs = dialogView.findViewById<EditText>(R.id.timerSecs)
        dialog.setView(dialogView)
        dialog.setPositiveButton("OK") {dialogInterface, i ->Int}
        val customDialog = dialog.create()
        customDialog.show()
        customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {
            var timer_Name = timerName.text.toString().trim()
            var timer_Mins = timerMins.text.toString()
            var timer_Secs = timerSecs.text.toString()

            if(timer_Name.isBlank()){
                timerName.error = "Please enter valid timer name"
                }
            else if (timer_Mins.isBlank()){
                timerMins.error = "Please enter timer minutes"
            }
            else if(timer_Secs.isBlank() || timer_Secs.toInt() > 59){
                timerSecs.error = "Please enter valid seconds"
            }
            else if(timer_Secs.toInt() == 0 && timer_Mins.toInt() == 0){
                timerMins.error = "Please enter minutes or seconds more than 0"
                timerSecs.error = "Please enter minutes or seconds more than 0"
            }

            else{
                insertTimer(timerName.text.toString(),timerMins.text.toString().toInt(),timerSecs.text.toString().toInt(),catPos)
                customDialog.dismiss()
            }
        }
    }
/*
Function to create the dialogue for adding a new timer. This one uses the number picker
 */
    fun createDialoguePicker(catPos: Int) {
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.timer_add_dialogue_numberpicker, null)
        val timerName = dialogView.findViewById<EditText>(R.id.timerNameNumberPicker)
        var timerMins = dialogView.findViewById<NumberPicker>(R.id.minsPicker)
        var timerSecs = dialogView.findViewById<NumberPicker>(R.id.secsPicker)
        var minsInt = timerMins.value
        var secsInt = timerSecs.value
        dialog.setView(dialogView)
        timerMins.minValue = 0
        timerMins.maxValue = 300
        timerSecs.minValue = 0
        timerSecs.maxValue = 59
        timerMins.setFormatter {
            String.format("%02d",it)
        }
        timerSecs.setFormatter {
            String.format("%02d",it)
        }
        timerMins.wrapSelectorWheel = true
        timerSecs.wrapSelectorWheel = true
        timerMins.setOnValueChangedListener() { picker, oldVal, newVal ->
            minsInt = newVal
        }
        timerSecs.setOnValueChangedListener() { picker, oldVal, newVal ->
            secsInt = newVal
        }
        dialog.setPositiveButton("OK") { dialogInterface, i -> Int }
        val customDialog = dialog.create()
        customDialog.show()
        customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {

            var timer_Name = timerName.text.toString().trim()

            if (timer_Name.isBlank()) {
                timerName.error = "Please enter valid timer name"
            } else if (minsInt == 0 && secsInt == 0) {
                Toast.makeText(this, "Please enter correct time", Toast.LENGTH_SHORT).show()
            }
            else {
                insertTimer(timer_Name,minsInt,secsInt,catPos)
                customDialog.dismiss()
            }
        }
    }


/*
Function to ask the user for confirmation to delete a timer
 */
    fun onDeleteClickDialogue(position:Int,catPosition:Int){
        var builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete this timer?")
        builder.setPositiveButton("Yes"){ _, _ -> }
        builder.setNegativeButton("No"){ _, _ -> }

        var dialogue =builder.create()
        dialogue.show()

        dialogue.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener{
            removeTimer(position,catPosition)
            dialogue.dismiss()
        }

        dialogue.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener{
            dialogue.dismiss()
        }
    }

/*
Creates the recycle viewer that displays the timers
 */
    fun createRecycleView(catPosition:Int){
        timerViewer.layoutManager= LinearLayoutManager(this)
        var timerAdapter = TimerViewerAdapter(CategoryList.listOfCategory[catPosition].timers)
        timerViewer.adapter = timerAdapter

        val obj = object: TimerViewerAdapter.OnItemClickListener{
            override fun onDeleteClick(position: Int) {
                onDeleteClickDialogue(position,catPosition)
            }
            override fun onItemClick(position: Int) {
                startTimerActivity(position,catPosition)

            }
        }

        timerAdapter.setOnItemClickListener(obj)
    }
/*
Function that gets called when the dialogue to create a timer is finished and is used to add a timer to the list
of timers for that category.
 */
    private fun insertTimer(name:String, mins:Int, secs:Int, pos:Int){
        CategoryList.listOfCategory[pos].addTimer(name,mins,secs)
        timerViewer.adapter!!.notifyItemInserted((CategoryList.listOfCategory[pos].timers.size) - 1)
        CategoryList.saveData(this)

    }

    /*
    Function that gets called
     */
    private fun removeTimer(pos:Int,catPos:Int){
        CategoryList.listOfCategory[catPos].timers.removeAt(pos)
        timerViewer.adapter!!.notifyItemRemoved((CategoryList.listOfCategory[catPos].timers.size) - 1)
        CategoryList.saveData(this)
    }

    fun startTimerActivity(timerPos:Int, categoryPosition:Int){
        val intent = Intent(applicationContext,TimerActivity::class.java)
        intent.putExtra("timer",CategoryList.listOfCategory[categoryPosition].timers[timerPos])
        startActivity(intent)
    }

}
