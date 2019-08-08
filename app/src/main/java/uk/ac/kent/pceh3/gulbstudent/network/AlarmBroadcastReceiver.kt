package uk.ac.kent.pceh3.gulbstudent.network

import android.app.*
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import uk.ac.kent.pceh3.gulbstudent.MainActivity
import uk.ac.kent.pceh3.gulbstudent.model.Bookmarks
import java.util.*
import android.support.v4.content.ContextCompat.getSystemService
import android.app.AlarmManager
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AlarmBroadcastReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManager? = null
    private lateinit var auth: FirebaseAuth

    override fun onReceive(context: Context?, intent: Intent?) {
        auth = FirebaseAuth.getInstance()
        notificationManager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(
                "uk.ac.kent.pceh3.gulbstudent",
                "GulbStudent",
                "Show notifications")


        Log.d(TAG, "onReceive: bookmarkNotification")
        val channelID = "uk.ac.kent.pceh3.gulbstudent"

        val resultIntent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        var user = auth.currentUser
        val indexUrl = intent!!.getCharSequenceExtra("url").toString()

        val notification = Notification.Builder(context,
                channelID)
                .setContentTitle("GulbStudent")
                .setContentText("'"+ intent!!.getCharSequenceExtra("title") + "' is happening tonight.")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentIntent(pendingIntent)
                .setChannelId(channelID)
                .setAutoCancel(true)
                .build()

        val id = System.currentTimeMillis().toInt()

        notificationManager?.notify(id, notification)

        FirebaseDatabase.getInstance().reference.child("users").child(user!!.uid).child("bookmarked").child(indexUrl).removeValue()

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