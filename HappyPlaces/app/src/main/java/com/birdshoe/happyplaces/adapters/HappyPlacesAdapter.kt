package com.birdshoe.happyplaces.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.birdshoe.happyplaces.R
import com.birdshoe.happyplaces.activities.AddHappyPlaceActivity
import com.birdshoe.happyplaces.activities.MainActivity
import com.birdshoe.happyplaces.database.DatabaseHandler
import com.birdshoe.happyplaces.models.HappyPlaceModel
import de.hdodenhof.circleimageview.CircleImageView

open class HappyPlacesAdapter (
    private val context: Context,
    private var list:ArrayList<HappyPlaceModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_happy_place,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){
            holder.itemView.findViewById<CircleImageView>(R.id.iv_place_image).setImageURI(Uri.parse(model.image))
            holder.itemView.findViewById<TextView>(R.id.tvTitle).text = model.title
            holder.itemView.findViewById<TextView>(R.id.tvDescription).text = model.description

            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    onClickListener!!.onClick(position,model)
                }
            }
        }
    }

    fun notifyEditItem(activity : Activity, position: Int, requestCode : Int) {
        val intent = Intent(context, AddHappyPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, list[position])
        activity.startActivityForResult(intent, requestCode)
        notifyItemChanged(position)
    }

    fun removeAt(position: Int) {
        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deleteHappyPlace(list[position])

        if(isDeleted > 0){
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int,model: HappyPlaceModel)
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}