package uk.ac.kent.pceh3.gulbstudent.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.LocationResult


class LocationUpdatesBroadcastReceiver: BroadcastReceiver() {
    companion object{
        val TAG = "LUBroadcastReceiver"

        val ACTION_PROCESS_UPDATES =
                "uk.ac.kent.pceh3.gulbstudent.network.action" + ".PROCESS_UPDATES"
    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            if (ACTION_PROCESS_UPDATES == action) {
                val result = LocationResult.extractResult(intent)
                if (result != null) {
                    val locations = result.locations
                    val locationResultHelper = LocationResultHelper(
                            context, locations
                    )
                    // Save the location data to SharedPreferences.
                    locationResultHelper.saveResults()
                    // Show notification with the location data.
                    locationResultHelper.showNotification()
                    Log.i(TAG, LocationResultHelper.getSavedLocationResult(context))
                }
            }
        }
    }
}