package com.example.timer
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.text.DecimalFormat


class TimerViewerAdapter(val timers:ArrayList<Timer>) : RecyclerView.Adapter<TimerViewerAdapter.ViewHolderTimer>() {

    lateinit var context:Context
    lateinit var clickListen:OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position:Int)
        fun onDeleteClick(position:Int)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolderTimer {
        context = p0.context
        val view: View = LayoutInflater.from(p0.context).inflate(R.layout.recycler_view_list_timers,p0,false)
        return ViewHolderTimer(view,clickListen)
    }

    override fun getItemCount() =  timers.size

    fun setOnItemClickListener(listener:OnItemClickListener){
        clickListen = listener
    }


    override fun onBindViewHolder(p0: ViewHolderTimer, p1: Int) {

        p0.timer_name.text = timers[p1].name

        var formattedMins =   DecimalFormat("00").format(timers[p1].mins)
        var formattedSecs = DecimalFormat("00").format(timers[p1].secs)
        var duration:String = "$formattedMins:$formattedSecs"
        p0.timer_duration.text = duration

    }

    class ViewHolderTimer(itemView:View, listener:OnItemClickListener):RecyclerView.ViewHolder(itemView){
        val timer_name: TextView = itemView.findViewById(R.id.textViewTimer)
        var timer_duration: TextView = itemView.findViewById(R.id.textViewTimerDuration)
        val timerDelete: ImageView = itemView.findViewById(R.id.timerDelete)

        init{
            itemView.setOnClickListener {
                if (listener != null) {
                    var position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position)
                    }
                }
            }
            timerDelete.setOnClickListener {
                if(listener != null){
                    var position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        listener.onDeleteClick(position)
                    }
                }
            }
        }


    }

}