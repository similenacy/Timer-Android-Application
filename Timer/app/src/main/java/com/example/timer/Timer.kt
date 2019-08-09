package com.example.timer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
Class that specifies the variabels in a timer
 */
@Parcelize
data class Timer(val name:String, val mins:Int, val secs:Int):Parcelable