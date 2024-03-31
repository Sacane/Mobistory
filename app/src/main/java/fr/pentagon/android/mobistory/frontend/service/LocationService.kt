package fr.pentagon.android.mobistory.frontend.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import fr.pentagon.android.mobistory.R
import fr.pentagon.android.mobistory.backend.entity.searchNearbyEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                val chanelEventNearby = NotificationChannel(
                    "eventNearby",
                    "eventNearbyChanel",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply { description = "chanel for events that are near the user" }
                notificationManager.createNotificationChannel(chanelEventNearby)
                Log.i(null, "START GPS")
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    lm.requestLocationUpdates("gps", 10000L, 0f) {
                        val eventNearby = runBlocking {
                            return@runBlocking withContext(Dispatchers.IO) {
                                return@withContext searchNearbyEvent(it.latitude, it.longitude)
                            }
                        }
                        if(eventNearby != null) {
                            val date = Date()
                            val notifyId = SimpleDateFormat("ddHHmmss", Locale.FRANCE).format(date).toInt()
                            val eventNotificationBuilder = NotificationCompat.Builder(context, "eventNearby")
                                .setContentTitle("There is an event near you !")
                                .setContentText("The event ${eventNearby.title} has occurred near your current position !")
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                            val eventNotification = eventNotificationBuilder.build()
                            with(NotificationManagerCompat.from(context)){
                                notify(notifyId, eventNotification)
                            }
                        }
                    }
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