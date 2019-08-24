package uk.ac.kent.pceh3.gulbstudent.network

import android.app.*
import android.content.Intent
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk.ac.kent.pceh3.gulbstudent.MainActivity
import uk.ac.kent.pceh3.gulbstudent.ui.MainActivityViewModel
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class GeofenceTransitionsJobIntentService : JobIntentService() {
    private var notificationManager: NotificationManager? = null

    companion object {
        private const val LOG_TAG = "GeoTrIntentService"

        private const val JOB_ID = 573

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                    context,
                    GeofenceTransitionsJobIntentService::class.java, JOB_ID,
                    intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.errorCode)
            Log.e(LOG_TAG, errorMessage)
            return
        }
        handleEvent(geofencingEvent)
    }

    private fun handleEvent(event: GeofencingEvent) {
        println("GEOFENCE EVENT")
        var message = "You are near The Gulbenkian. Click to see whats going on!"

        val myRef = FirebaseDatabase.getInstance().getReference("message")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value

                message = dataSnapshot.value.toString()
                if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

                    val current = LocalDateTime.now()
                    val formatter = DateTimeFormatter.BASIC_ISO_DATE
                    val formattedStartDate = current.format(formatter)


                    val sharedPref: SharedPreferences = getSharedPreferences("GULB_VISIT", 0)

                    if (sharedPref.getString("GULB_VISIT", "") != formattedStartDate) {
                        val editor = sharedPref.edit()
                        editor.putString("GULB_VISIT", formattedStartDate)
                        editor.apply()

                        val channelID = "uk.ac.kent.pceh3.gulbstudent"

                        notificationManager =
                                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                        createNotificationChannel(
                                "uk.ac.kent.pceh3.gulbstudent",
                                "GulbStudent",
                                "Show notifications")


                        val resultIntent = Intent(applicationContext, MainActivity::class.java)

                        val pendingIntent = PendingIntent.getActivity(
                                applicationContext,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        )

                        val notification = Notification.Builder(applicationContext,
                                channelID)
                                .setContentTitle("GulbStudent")
                                .setContentText(message)
                                .setSmallIcon(android.R.drawable.ic_dialog_info)
                                .setContentIntent(pendingIntent)
                                .setChannelId(channelID)
                                .setAutoCancel(true)
                                .build()
                        notificationManager?.notify(1, notification)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("Geofence", "Failed to read value.", error.toException())
            }
        })

        println("GEOFENCE ENTER GULB")
    }

    private fun createNotificationChannel(id: String, name: String,
                                          description: String) {

        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(id, name, importance)

        channel.description = description
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        channel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager?.createNotificationChannel(channel)
    }
}