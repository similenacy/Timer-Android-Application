package com.example.timer

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

public  class CategoryList {


    /*
    Class made to save and store the list of categories that the application revolves around
    It saves the list to the disk so the data is retained even when the application is closed and re-opened.
    It does this by converting the list into a json and saving it to the SharedPrefences

     */
    companion object {
         var listOfCategory = arrayListOf<Category>()

        /*
        Function that saves data to the disk
         */
        fun saveData(context: Context){
            var sharedPreferenes = PreferenceManager.getDefaultSharedPreferences(context)
            var editor = sharedPreferenes.edit()
            var gson = Gson()
            var json = gson.toJson(listOfCategory)
            editor.putString("saveData",json)
            editor.apply()

        }
        /*
        Function that loads data from the disk. Called on application start
         */
        fun loadData(context: Context){
            var sharedPreferenes = PreferenceManager.getDefaultSharedPreferences(context)
            var gson = Gson()
            var json = sharedPreferenes.getString("saveData", null)
            var type = object:TypeToken<ArrayList<Category>>(){}.type

            if(json == null){
                listOfCategory = arrayListOf<Category>()
            }
            else {
                listOfCategory = gson.fromJson(json, type)
            }

            /*if(listOfCategory.isEmpty()){
                listOfCategory = arrayListOf<Category>()
            }*/
        }



    }





    }





