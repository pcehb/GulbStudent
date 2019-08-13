package uk.ac.kent.pceh3.gulbstudent.network

import android.app.*
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import uk.ac.kent.pceh3.gulbstudent.MainActivity
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters


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

        Log.d(TAG, "onReceive: a notification")

        if (intent!!.getCharSequenceExtra("type").toString() == "bookmark"){
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
            val indexUrl = intent.getCharSequenceExtra("url").toString()

            val notification = Notification.Builder(context,
                    channelID)
                    .setContentTitle("GulbStudent")
                    .setContentText("'"+ intent.getCharSequenceExtra("title") + "' is happening tonight.")
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentIntent(pendingIntent)
                    .setChannelId(channelID)
                    .setAutoCancel(true)
                    .build()


            notificationManager?.notify(intent.getIntExtra("notificationId", 0), notification)

            FirebaseDatabase.getInstance().reference.child("users").child(user!!.uid).child("bookmarked").child(indexUrl).removeValue()
        }
        else if(intent.getCharSequenceExtra("type").toString() == "category") {
            Log.d(TAG, "onReceive: categoryNotification")
            val channelID = "uk.ac.kent.pceh3.gulbstudent"

            val fieldISO = TemporalAdjusters.next(DayOfWeek.MONDAY)
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.BASIC_ISO_DATE
            val formattedStartDate = current.format(formatter)
            val next = current.with(fieldISO)
            val formattedEndDate = next.format(formatter)

            val data = WhatsOnAjax().getWhatsOn("", intent.getCharSequenceExtra("categorySearch").toString(), formattedStartDate, formattedEndDate)
            if (data != null) {
                val resultIntent = Intent(context, MainActivity::class.java)
                resultIntent.putExtra("openingFragment", "suggested")
                resultIntent.putExtra("categorySearch", intent.getCharSequenceExtra("categorySearch").toString())
                resultIntent.putExtra("startDate", formattedStartDate)
                resultIntent.putExtra("endDate", formattedEndDate)

                val pendingIntent = PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                )

                val notification = Notification.Builder(context,
                        channelID)
                        .setContentTitle("GulbStudent")
                        .setContentText("You have suggested shows happening this week.")
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentIntent(pendingIntent)
                        .setChannelId(channelID)
                        .setAutoCancel(true)
                        .build()
                notificationManager?.notify(intent.getIntExtra("notificationId", 1), notification)

            }
            else{
                println("No events happening")
            }
        }
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