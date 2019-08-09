package com.example.timer

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.recycler_view_list.*
import java.lang.NullPointerException
import java.lang.reflect.Type
import java.util.*

class MainActivity : AppCompatActivity() {



/* Function that runs when the app is launched, it creates the instance, sets the view, loads the data from the phone disk
   and creates the list of categories
 */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        CategoryList.loadData(this)
        createRecycleView()
        fab_addCategory.setOnClickListener { view ->
            createDialogue()
        }
    }


    /*
    Function that creates the recycle viewer for the list of categories and sets the on click listeners to select or delete
    a category
     */
    fun createRecycleView(){
        categoryViewer.layoutManager = LinearLayoutManager(this)
        var catAdapter = CategoryViewerAdapter(CategoryList.listOfCategory)
        categoryViewer.adapter = catAdapter

        val obj = object: CategoryViewerAdapter.OnItemClickListener{
            override fun onDeleteClick(position: Int) {
                onDeleteClickDialogue(position)
                //removeCategory(position)
            }
            override fun onItemClick(position: Int) {
                startSubCategoryActivity(position)
            }
        }

        catAdapter.setOnItemClickListener(obj)
    }

/*
Function that is called when a category is clicked. It opens up the sub category activity and passes the position of the category
from the global class CategoryList
 */
    fun startSubCategoryActivity(category:Int){
        val intent = Intent(applicationContext,sub_category::class.java)
        intent.putExtra("catPosition",category)
        startActivity(intent)
    }



/*
Function to create the dialogue used to add a new category. Prompts the user to select a name and adds a category with that name
using insertCategory
 */
    fun createDialogue(){
        val dialog =AlertDialog.Builder(this)
        val dialogView =layoutInflater.inflate(R.layout.add_categorie_dialog, null)
        val categoryName = dialogView.findViewById<EditText>(R.id.categoryName)
        dialog.setView(dialogView)
        dialog.setPositiveButton("OK") {dialogInterface, i ->Int}
        val customDialog = dialog.create()
        customDialog.show()
        customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener()
        {
            var category_Name = categoryName.text.toString().trim()

            if(category_Name.isBlank()) {
                categoryName.error = "Please enter a category name"

            }
            else{
                insertCategory(categoryName.text.toString())
                customDialog.dismiss()
            }
        }
    }

    /*
    Function that creates the dialogue for when a category is deleted. It asks the user if he/she is sure that it wants to
    delete the category. If yes the category is deleted, else the whole process is cancelled.
     */
   fun onDeleteClickDialogue(position:Int){
       var builder = AlertDialog.Builder(this)
       builder.setMessage("Are you sure you want to delete this category?")
       builder.setPositiveButton("Yes"){ _, _ -> }
       builder.setNegativeButton("No"){ _, _ -> }

       var dialogue =builder.create()
       dialogue.show()

       dialogue.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener{
           removeCategory(position)
           dialogue.dismiss()
       }

       dialogue.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener{
           dialogue.dismiss()
       }
   }



    fun checkName(name:String):Boolean{
        return !(!(name.contains("\n")) && name == "")
    }

    /*
    Adds a category to the CategoryList file
     */
    fun insertCategory(name:String){
        CategoryList.listOfCategory.add(Category(name))
        categoryViewer.adapter!!.notifyItemInserted((CategoryList.listOfCategory.size)-1)
        CategoryList.saveData(this)

    }

    /*
     * Removes a category from the list of categories
      */
    fun removeCategory(position:Int){
        CategoryList.listOfCategory.removeAt(position)
        categoryViewer.adapter!!.notifyItemRemoved(position)
        CategoryList.saveData(this)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
