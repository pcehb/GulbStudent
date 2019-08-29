package uk.ac.kent.pceh3.gulbstudent.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

// broadcast receiver for geofence events
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        println("GEOFENCE RECEIVED")

        GeofenceTransitionsJobIntentService.enqueueWork(context, intent)
    }
}