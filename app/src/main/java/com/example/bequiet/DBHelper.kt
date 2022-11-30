package com.example.bequiet

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory:SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                 NAME_COL + " TEXT, " +
                VOLUME_COL + " INTEGER, " +
                X_COORD_COL + " DOUBLE, " +
                Y_COORD_COL + " DOUBLE)")

        db?.execSQL(query)
    }

    fun addPlace(
        name: String,
        volume: Int,
        x_coord: Double,
        y_coord: Double
    ) {
        val values = ContentValues()

        values.put(NAME_COL, name)
        values.put(VOLUME_COL, volume)
        values.put(X_COORD_COL, x_coord)
        values.put(Y_COORD_COL, y_coord)

        val db = this.writableDatabase

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getPlace(): Cursor? {
        val db = this.readableDatabase

        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
    }

    fun deletePlace(id: Int) {
        val db = this.writableDatabase

        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + ID_COL + "=" + id +"")
        db.close()
    }

    fun listPlaces(): ArrayList<Place> {
        val sql = "select * from $TABLE_NAME"
        val db = this.readableDatabase
        val storePlaces =
            ArrayList<Place>()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(0).toInt()
                val name = cursor.getString(1)
                val volume = cursor.getString(2).toInt()
                val x = cursor.getString(3).toDouble()
                val y = cursor.getString(4).toDouble()
                storePlaces.add(Place(id, name, volume, x, y))
            }
            while (cursor.moveToNext())
        }
        cursor.close()
        return storePlaces
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    companion object{
        // here we have defined variables for our database

        // below is variable for database name
        private val DATABASE_NAME = "GEEKS_FOR_GEEKS"

        // below is the variable for database version
        private val DATABASE_VERSION = 1

        // below is the variable for table name
        val TABLE_NAME = "gfg_table"

        val ID_COL = "id"
        val NAME_COL = "name"
        val VOLUME_COL = "volume"
        val X_COORD_COL = "x_coordinate"
        val Y_COORD_COL = "y_coordinate"
    }
}