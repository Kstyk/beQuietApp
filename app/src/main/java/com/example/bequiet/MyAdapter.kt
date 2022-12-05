package com.example.bequiet

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bequiet.databinding.ActivityListOfPlacesBinding


class MyAdapter(var size: Int, var context: Context): RecyclerView.Adapter<MyViewHolder>() {
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

        holder.btnEdit.setOnClickListener() {
            val int = Intent(context, AddPlace::class.java)
            int.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            int.putExtra("id", places[position].id)
            int.putExtra("name", places[position].name)
            int.putExtra("range", places[position].range)
            int.putExtra("volume", places[position].volume)
            int.putExtra("x", places[position].x)
            int.putExtra("y", places[position].y)

            context.startActivity(int)
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
    val btnEdit = itemView.findViewById<Button>(R.id.editBtn)

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