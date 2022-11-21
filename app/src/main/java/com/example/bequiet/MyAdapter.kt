package com.example.bequiet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bequiet.databinding.ActivityListOfPlacesBinding

class MyAdapter: RecyclerView.Adapter<MyViewHolder>() {
    var count: Int = 0
    private lateinit var binding: ActivityListOfPlacesBinding
    val db = DBHelper(binding.rv.context,null)
    var size: Int = db.listPlaces().size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val placeRow = layoutInflater.inflate(R.layout.place_record, parent, false)
        return MyViewHolder(placeRow)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val name = holder.itemView.findViewById<TextView>(R.id.name)
        val volume = holder.itemView.findViewById<TextView>(R.id.volume)
        val x = holder.itemView.findViewById<TextView>(R.id.x_coord)
        val y = holder.itemView.findViewById<TextView>(R.id.y_coord)

        val db = DBHelper(holder.itemView.context,null)
        var places: ArrayList<Place> = db.listPlaces()

        name.append(places[position].name)
        volume.append(places[position].volume.toString())
        x.append(places[position].x.toString())
        y.append(places[position].y.toString())
    }

}

class MyViewHolder(view: View): RecyclerView.ViewHolder(view)