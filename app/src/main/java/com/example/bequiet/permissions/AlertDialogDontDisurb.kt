package com.example.bequiet.permissions

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class AlertDialogDontDisurb {


    fun showAlertDialogButtonClicked(context: Context){
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Tryb cichy")
        builder.setMessage("Zezwól na dostęp do trybu cichego, by aplikacja mogła dalej działać")
        builder.setCancelable(false)

        builder.setPositiveButton("Włącz",
            DialogInterface.OnClickListener { dialog, which ->
                val panelIntent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                context.startActivity(panelIntent)
            }

        )

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