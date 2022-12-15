package com.example.bequiet.permissions

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.example.bequiet.locationService.hasLocationPermission

class CheckLocationPermissions {
    fun statusCheck(context: Context) {
        if (!context.hasLocationPermission()) {
            buildAlertMessageNoLocationPermissions(context)
        }
    }

    private fun buildAlertMessageNoLocationPermissions(context: Context) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage("Twoja aplikacja nie ma uprawnień do lokalizacji, musisz je włączyć, by twoja aplikacja działała poprawnie")
            .setCancelable(false)
            .setPositiveButton("Włącz",
                DialogInterface.OnClickListener { dialog, id ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts(
                        "package",
                        context.packageName, null
                    )
                    intent.data = uri
                    context.startActivity(intent)
                })
            .setNegativeButton("Wyjdź",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert: AlertDialog = builder.create()

        alert.show()
    }


}