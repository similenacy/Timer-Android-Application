package com.example.timer

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*
Class that defines a category structure.
 */
@Parcelize
data class Category(var name:String) : Parcelable {



    var subCats = arrayListOf<Category>()
    var timers = arrayListOf<Timer>()


    fun addSubCategory(name:String){
        subCats.add(Category(name))
    }

    fun addTimer(name:String, mins:Int, secs:Int){
        timers.add(Timer(name,mins,secs))
    }


}