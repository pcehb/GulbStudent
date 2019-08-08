package uk.ac.kent.pceh3.gulbstudent.network

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import uk.ac.kent.pceh3.gulbstudent.MainActivity

class AlarmBroadcastReceiver : BroadcastReceiver() {
    private var notificationManager: NotificationManager? = null

    override fun onReceive(context: Context?, intent: Intent?) {

        notificationManager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(
                "uk.ac.kent.pceh3.gulbstudent",
                "GulbStudent",
                "Show notifications")

        if (intent!!.action == "android.intent.action.BOOT_COMPLETED") {
            // Set the alarm here.
            Log.d(TAG, "onReceive: BOOT_COMPLETED")
            return
        }

            bookmarkNotification(context, intent)


    }

    fun bookmarkNotification(context: Context?, intent: Intent?){
        Log.d(TAG, "onReceive: bookmarkNotification")
        val notificationID = intent!!.getIntExtra("notificationId", 0)
        val channelID = "uk.ac.kent.pceh3.gulbstudent"

        val resultIntent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )


        val notification = Notification.Builder(context,
                channelID)
                .setContentTitle("GulbStudent")
                .setContentText("'"+ intent.getCharSequenceExtra("title") + "' is happening tonight.")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentIntent(pendingIntent)
                .setChannelId(channelID)
                .setAutoCancel(true)
                .build()


        notificationManager?.notify(notificationID, notification)
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