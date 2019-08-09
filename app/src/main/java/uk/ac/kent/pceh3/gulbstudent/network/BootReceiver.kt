package uk.ac.kent.pceh3.gulbstudent.network

import android.app.*
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk.ac.kent.pceh3.gulbstudent.MainActivity
import uk.ac.kent.pceh3.gulbstudent.model.Bookmarks
import java.util.*

class BootReceiver : BroadcastReceiver() {

        private lateinit var auth: FirebaseAuth

        override fun onReceive(context: Context?, intent: Intent?) {


            if (intent!!.action == "android.intent.action.BOOT_COMPLETED") {
                // Set the alarm here.
                Log.d(ContentValues.TAG, "onReceive: BOOT_COMPLETED")

                auth = FirebaseAuth.getInstance()
                var user = auth.currentUser

                FirebaseDatabase.getInstance().reference.child("users").child(user!!.uid).child("bookmarked")
                        .addListenerForSingleValueEvent(object : ValueEventListener {

                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for(snapshot: DataSnapshot in dataSnapshot.getChildren())
                                {
                                    val bookmarks = snapshot.getValue(Bookmarks::class.java)
                                    val id = System.currentTimeMillis().toInt()

                                    val bookmarkIntent = PendingIntent.getBroadcast(
                                            context,
                                            0,
                                            Intent(context, AlarmBroadcastReceiver::class.java).apply {
                                                putExtra("title", bookmarks!!.title!!)
                                                putExtra("url", snapshot.key)
                                                putExtra("notificationId", id)
                                                putExtra("type", "bookmark")
                                            },
                                            PendingIntent.FLAG_CANCEL_CURRENT
                                    )

                                    val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                                    alarmManager.set(
                                            AlarmManager.RTC_WAKEUP,
                                            Calendar.getInstance().apply {
                                                set(Calendar.HOUR_OF_DAY, 9)
                                                set(Calendar.MINUTE, 0)
                                                set(Calendar.SECOND, 0)
                                                set(Calendar.YEAR, bookmarks!!.year!!)
                                                set(Calendar.MONTH,bookmarks!!.month!!)
                                                set(Calendar.DATE, bookmarks!!.date!!)
                                            }.timeInMillis,
                                            bookmarkIntent
                                    )
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Failed to read value
                            }
                        })

                return
            }

        }
    }