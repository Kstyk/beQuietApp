package com.example.bequiet.permissions

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings

class CheckConnection {
    @Suppress("DEPRECATION")
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        return ni != null
    }

    fun showAlertDialogButtonClicked(context: Context){
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Połączenie internetowe")
        builder.setMessage("Włącz połączenie internetowe, by aplikacja mogła działać")
        builder.setCancelable(false)

        builder.setPositiveButton("Włącz",
            DialogInterface.OnClickListener { dialog, which ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val panelIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
                    context.startActivity(panelIntent)
                } else {
                    // TODO("VERSION.SDK_INT < Q")
                }
            })

        builder.setNegativeButton("Wyjdź") { dialog, which ->
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

            if(am != null) {
                val tasks: List<ActivityManager.AppTask> = am.appTasks

                if(tasks!= null) {
                    tasks.get(0).finishAndRemoveTask()
                }
            }
        }

        val alertDialog = builder.create()

        alertDialog.show()
    }
}