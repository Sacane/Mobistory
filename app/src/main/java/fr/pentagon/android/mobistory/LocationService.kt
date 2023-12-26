package fr.pentagon.android.mobistory

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class LocationService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "START") {
            Log.i(null, "START")
        } else if (intent?.action == "STOP") {
            Log.i(null, "STOP")
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}