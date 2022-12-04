package com.example.bequiet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bequiet.databinding.ActivityListOfPlacesBinding


class MyAdapter(var size: Int): RecyclerView.Adapter<MyViewHolder>() {
    var count: Int = 0
    private lateinit var binding: ActivityListOfPlacesBinding
    var items = size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val placeRow = layoutInflater.inflate(R.layout.place_record, parent, false)
        return MyViewHolder(placeRow)
    }

    override fun getItemCount(): Int {
        return items
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val db = DBHelper(holder.itemView.context,null)
        var places: ArrayList<Place> = db.listPlaces()

        if(places.size > 0) {
            holder.name.append(places[position].name.uppercase())
            holder.volume.append(places[position].volume.toString())
            holder.x.append(places[position].x.toString())
            holder.y.append(places[position].y.toString())
            holder.range.append(places[position].range.toString())
        }

        holder.btnDel.setOnClickListener() {
            db.deletePlace(places[position].id)
            places.removeAt(position)

            items--

            notifyItemRemoved(position)
        }
    }
}

class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val name = itemView.findViewById<TextView>(R.id.name)
    val volume = itemView.findViewById<TextView>(R.id.volume)
    val x = itemView.findViewById<TextView>(R.id.x_coord)
    val y = itemView.findViewById<TextView>(R.id.y_coord)
    val range = itemView.findViewById<TextView>(R.id.rangeTv)

    val btnDel = itemView.findViewById<Button>(R.id.delete)

//    init {
//        btnDel.setOnClickListener() {
//            val db = DBHelper(itemView.context, null)
//            var places: ArrayList<Place> = db.listPlaces()
//
//            db.deletePlace(places[adapterPosition].id)
//            places.removeAt(adapterPosition)
//
//
//        }
//    }
}