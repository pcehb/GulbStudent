package uk.ac.kent.pceh3.gulbstudent.network

import uk.ac.kent.pceh3.gulbstudent.MainActivity
import android.content.Context
import android.location.Location
import android.app.NotificationManager
import android.app.Notification
import android.app.NotificationChannel
import java.text.DateFormat
import java.util.*
import android.preference.PreferenceManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.graphics.Color
import uk.ac.kent.pceh3.gulbstudent.R

class LocationResultHelper (context: Context, locations: List<Location>) {

    val context: Context = context
    private val mLocations: List<Location> = locations
    private var mNotificationManager: NotificationManager? = null

    companion object {
        val KEY_LOCATION_UPDATES_RESULT: String? = "location-update-result"
        val PRIMARY_CHANNEL: String? = "uk.ac.kent.pceh3.gulbstudent"

        fun getSavedLocationResult(context: Context): String? {
            return PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(KEY_LOCATION_UPDATES_RESULT, "")
        }
    }

    init {

        val channel = NotificationChannel(
                PRIMARY_CHANNEL,
                context.getString(R.string.default_channel), NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.lightColor = Color.GREEN
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        mNotificationManager?.createNotificationChannel(channel)
    }

    private fun getLocationResultTitle(): String {
        val numLocationsReported = context.resources.getQuantityString(
                R.plurals.num_locations_reported, mLocations.size, mLocations.size)

        return numLocationsReported + ": " + DateFormat.getDateTimeInstance().format(Date())
    }

    private fun getLocationResultText(): String {
        if (mLocations.isEmpty()) {
            return context.getString(R.string.unknown_location)
        }
        val sb = StringBuilder()
        for (location in mLocations) {
            sb.append("(")
            sb.append(location.latitude)
            sb.append(", ")
            sb.append(location.longitude)
            sb.append(")")
            sb.append("\n")
        }
        return sb.toString()
    }


    fun saveResults() {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(
                        KEY_LOCATION_UPDATES_RESULT, getLocationResultTitle() + "\n" +
                        getLocationResultText()
                )
                .apply()
    }

    private fun getNotificationManager(): NotificationManager? {
        if (mNotificationManager == null) {
            mNotificationManager = context.getSystemService(
                    Context.NOTIFICATION_SERVICE
            ) as NotificationManager?
        }
        return mNotificationManager
    }

    fun showNotification() {
        val notificationIntent = Intent(context, MainActivity::class.java)

        // Construct a task stack.
        val stackBuilder = TaskStackBuilder.create(context)

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity::class.java)

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent)

        // Get a PendingIntent containing the entire back stack.
        val notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = Notification.Builder(context, PRIMARY_CHANNEL)
                .setContentTitle(getLocationResultTitle())
                .setContentText(getLocationResultText())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(notificationPendingIntent)

        getNotificationManager()?.notify(0, notificationBuilder.build())
    }
}