package com.example.bequiet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.SystemClock
import android.widget.Toast
import java.util.*

class myBackgroundProcess: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val ringtone = RingtoneManager.getRingtone(
            p0,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        )
        ringtone.play()

        Toast.makeText(
            p0, """
     This is my background process: 
     ${Calendar.getInstance().getTime().toString()}
     """.trimIndent(), Toast.LENGTH_LONG
        ).show()

        SystemClock.sleep(2000)
        ringtone.stop()
    }
}