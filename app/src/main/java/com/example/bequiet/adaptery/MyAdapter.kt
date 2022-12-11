package com.example.bequiet.adaptery

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bequiet.R
import com.example.bequiet.db.DBHelper
import com.example.bequiet.db.Place
import com.example.bequiet.permissions.isServiceRunning
import com.example.bequiet.locationService.LocationService
import com.example.bequiet.widoki.AddPlace


class MyAdapter(private var size: Int, var context: Context): RecyclerView.Adapter<MyViewHolder>() {
    private var items = size

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
        val places: ArrayList<Place> = db.listPlaces()

        if(places.size > 0) {
            holder.name.append(places[position].name.uppercase())
            holder.volume.append(places[position].volume.toString())
            holder.x.append(places[position].x.toString())
            holder.y.append(places[position].y.toString())
            holder.range.append(places[position].range.toString())
        }

        holder.btnDel.setOnClickListener {
            db.deletePlace(places[position].id)
            places.removeAt(position)

            items--

            notifyItemRemoved(position)

            val service = isServiceRunning()
            val ifRun = service.isMyServiceRunning(LocationService::class.java, context)

            if(ifRun && (db.listPlaces().size) == 0) {
                Intent(context, LocationService::class.java).apply {
                    action = LocationService.ACTION_STOP
                    context.startService(this)
                }
            }
        }

        holder.btnEdit.setOnClickListener {
            val int = Intent(context, AddPlace::class.java)
            int.flags = Intent.FLAG_ACTIVITY_NEW_TASK

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
    val name: TextView = itemView.findViewById(R.id.name)
    val volume: TextView = itemView.findViewById(R.id.volume)
    val x: TextView = itemView.findViewById(R.id.x_coord)
    val y: TextView = itemView.findViewById(R.id.y_coord)
    val range: TextView = itemView.findViewById(R.id.rangeTv)

    val btnDel: Button = itemView.findViewById(R.id.delete)
    val btnEdit: Button = itemView.findViewById(R.id.editBtn)
}