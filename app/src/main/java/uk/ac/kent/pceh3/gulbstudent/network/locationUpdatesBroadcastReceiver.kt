package uk.ac.kent.pceh3.gulbstudent.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.LocationResult



class locationUpdatesBroadcastReceiver : BroadcastReceiver() {

    private val TAG = "LUBroadcastReceiver"

    val ACTION_PROCESS_UPDATES = "com.google.android.gms.location.sample.backgroundlocationupdates.action" + ".PROCESS_UPDATES"

    override fun onReceive(context: Context, intent: Intent?) {
        Log.i("LOCATION", "BROADCAST RECEIVER")

        if (intent != null) {
            Log.i("LOCATION", "NOT NULL")
            val action = intent.action
            if (ACTION_PROCESS_UPDATES == action) {
                val result = LocationResult.extractResult(intent)
                if (result != null) {
                    val locations = result.locations
                    val locationResultHelper = locationResultHelper(
                            context, locations)
                    // Save the location data to SharedPreferences.
                    locationResultHelper.saveResults()
                    // Show notification with the location data.
                    locationResultHelper.showNotification()
                    Log.i(TAG, locationResultHelper.getSavedLocationResult(context))
                }
            }
        }
    }
}
