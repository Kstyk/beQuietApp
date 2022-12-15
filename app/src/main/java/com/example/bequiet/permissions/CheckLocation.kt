package com.example.bequiet.permissions

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings

class CheckLocation {
    fun statusCheck(context: Context) {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (!manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(context)
        }
    }

    private fun buildAlertMessageNoGps(context: Context) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage("Musisz włączyć GPS, by aplikacja działała poprawnie")
            .setCancelable(false)
            .setPositiveButton("Włącz",
                DialogInterface.OnClickListener { dialog, id ->
                    val panelIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(panelIntent)
                })
            .setNegativeButton("Wyjdź",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}