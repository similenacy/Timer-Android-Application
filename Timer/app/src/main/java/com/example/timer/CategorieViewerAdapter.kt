package com.example.timer
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/*
Class That handles the recycle viewer that is used to display all the categories in the Main Activity
 */

class CategoryViewerAdapter(val categories:ArrayList<Category>) : RecyclerView.Adapter<CategoryViewerAdapter.ViewHolder>() {

    lateinit var context:Context
    lateinit var clickListen:OnItemClickListener

    /*
    Interface that is used in the Main activity to define the actions performed
    when the category is tapped or the delete button is tapped.
     */
    interface OnItemClickListener{
        fun onItemClick(position:Int)
        fun onDeleteClick(position:Int)
    }

    /*
    Creates the view holder
     */
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        context = p0.context
        val view: View = LayoutInflater.from(p0.context).inflate(R.layout.recycler_view_list,p0,false)
        return ViewHolder(view,clickListen)
    }

    override fun getItemCount() =  categories.size

    fun setOnItemClickListener(listener:OnItemClickListener){
        clickListen = listener
    }



/*
Fucntion used to define what information about the category should be displayed in the recycle viewer
 */
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.category_name.text = categories[p1].name
    }
/*
Class used to define the view holder for each item in the recycle viewer. It is used to set the on click listeners for the
categories and the delete button and invokes the interface mentioned above.
 */
    class ViewHolder(itemView:View,listener:OnItemClickListener):RecyclerView.ViewHolder(itemView){
        val category_name: TextView = itemView.findViewById(R.id.textViewCat)
        val remove_cat: ImageView = itemView.findViewById(R.id.remove_cat)

        init {
            /*
            Sets the on click listener for the category
             */
            itemView.setOnClickListener {
                if (listener != null) {
                    var position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position)
                    }
                }
            }
            /*
            Sets the on click listener for the delete button of a category
             */
            remove_cat.setOnClickListener {
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