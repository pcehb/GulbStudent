package uk.ac.kent.pceh3.gulbstudent.network

import android.app.*
import uk.ac.kent.pceh3.gulbstudent.MainActivity
import android.content.Intent
import android.content.Context.NOTIFICATION_SERVICE
import android.preference.PreferenceManager
import android.content.Context
import android.graphics.Color
import android.location.Location
import uk.ac.kent.pceh3.gulbstudent.R
import java.text.DateFormat.*
import java.util.*


class locationResultHelper (context: Context, locations: List<Location>){

    val KEY_LOCATION_UPDATES_RESULT = "location-update-result"

    private val PRIMARY_CHANNEL = "default"

    private var mContext: Context = context
    private var mLocations: List<Location> = locations
    private var mNotificationManager: NotificationManager? = null

    init {
        val channel = NotificationChannel(PRIMARY_CHANNEL, mContext.resources.getString(R.string.default_channel), NotificationManager.IMPORTANCE_DEFAULT)
        channel.lightColor = Color.GREEN
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        getNotificationManager().createNotificationChannel(channel)
    }



    /**
     * Returns the title for reporting about a list of [Location] objects.
     */
    private fun getLocationResultTitle(): String {
        val numLocationsReported = mContext.resources.getQuantityString(
                R.plurals.num_locations_reported, mLocations.size, mLocations.size)


        return numLocationsReported + ": " + getDateTimeInstance().format(Date())
    }

    private fun getLocationResultText(): String {
        if (mLocations.isEmpty()) {

            return mContext.resources.getString(R.string.unknown_location)
        }
        val sb = StringBuilder()
        for (location in mLocations) {
            sb.append("(")
            sb.append(location.getLatitude())
            sb.append(", ")
            sb.append(location.getLongitude())
            sb.append(")")
            sb.append("\n")
        }
        return sb.toString()
    }

    /**
     * Saves location result as a string to [android.content.SharedPreferences].
     */
    fun saveResults() {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(KEY_LOCATION_UPDATES_RESULT, getLocationResultTitle() + "\n" +
                        getLocationResultText())
                .apply()
    }

    /**
     * Fetches location results from [android.content.SharedPreferences].
     */
    fun getSavedLocationResult(context: Context): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_UPDATES_RESULT, "")
    }

    /**
     * Get the notification mNotificationManager.
     *
     *
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private fun getNotificationManager(): NotificationManager {
        if (mNotificationManager == null) {
            mNotificationManager = mContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        }
        return mNotificationManager!!
    }

    /**
     * Displays a notification with the location results.
     */
    fun showNotification() {
        val notificationIntent = Intent(mContext, MainActivity::class.java)

        // Construct a task stack.
        val stackBuilder = TaskStackBuilder.create(mContext)

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity::class.java)

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent)

        // Get a PendingIntent containing the entire back stack.
        val notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = Notification.Builder(mContext,
                PRIMARY_CHANNEL)
                .setContentTitle(getLocationResultTitle())
                .setContentText(getLocationResultText())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(notificationPendingIntent)

        getNotificationManager().notify(0, notificationBuilder.build())
    }
}