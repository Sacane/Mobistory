package fr.pentagon.android.mobistory.frontend.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import android.Manifest
import fr.pentagon.android.mobistory.R

class LocationService : Service() {
    private lateinit var lm: LocationManager
    private var started: Boolean = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val context = this

        if (intent?.action == "START") {
            lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            started = false

            val chanel = NotificationChannel(
                "locationService",
                "locationService",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(chanel)
            val notification = NotificationCompat.Builder(context, "locationService")
                .setContentTitle("Foreground Service")
                .setContentText("Running...")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()

            startForeground(1, notification)

            if (lm.getProviders(true).contains("gps")) {
                Log.i(null, "START GPS")
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    lm.requestLocationUpdates("gps", 10000L, 0f, object : LocationListener {
                        override fun onLocationChanged(location: Location) {
                            // Log.i(null, location.toString())
                        }
                    })
                }
                started = true
            }
        } else if (intent?.action == "STOP") {
            Log.i(null, "STOP")
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}