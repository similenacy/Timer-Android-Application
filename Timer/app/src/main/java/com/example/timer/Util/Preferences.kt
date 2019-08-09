package com.example.timer.Util

class Preferences {

    /*
    Class to change some preferences to be implemented in the future.

    Some of the settings include:
    o Start timer automatically when it is selected
    o Utilize the number picker to create timer
     */

    companion object {
        private var optionNumberPicker = false

        private var startTimerAutomatically = false

        fun setNumberPicker(bool:Boolean){
            optionNumberPicker = bool
        }

        fun getNumberPicker():Boolean{
            return optionNumberPicker
        }

    }
}