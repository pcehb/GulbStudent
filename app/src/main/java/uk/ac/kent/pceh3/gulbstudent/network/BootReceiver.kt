package uk.ac.kent.pceh3.gulbstudent.network

import android.app.*
import android.content.*
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk.ac.kent.pceh3.gulbstudent.model.Bookmarks
import java.util.*
import java.util.Calendar.MONDAY

class BootReceiver : BroadcastReceiver() {

        private lateinit var auth: FirebaseAuth

        override fun onReceive(context: Context?, intent: Intent?) {


            if (intent!!.action == "android.intent.action.BOOT_COMPLETED") {

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
                                                set(Calendar.MONTH,bookmarks.month!!)
                                                set(Calendar.DATE, bookmarks.date!!)
                                            }.timeInMillis,
                                            bookmarkIntent
                                    )
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Failed to read value
                            }
                        })


                FirebaseDatabase.getInstance().reference.child("users").child(user.uid).child("categories")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                var categorySearch = ""
                                if (dataSnapshot.child("archive").value == true){
                                    categorySearch += "&event_type%5B%5D=archive"
                                }
                                if (dataSnapshot.child("audioDescribed").value == true){
                                    categorySearch += "&event_type%5B%5D=audio-description"
                                }
                                if (dataSnapshot.child("boing").value == true){
                                    categorySearch += "&event_type%5B%5D=boing-festival"
                                }
                                if (dataSnapshot.child("cafe").value == true){
                                    categorySearch += "&event_type%5B%5D=cafe"
                                }
                                if (dataSnapshot.child("captionedSubtitles").value == true){
                                    categorySearch += "&event_type%5B%5D=captioned-subtitles"
                                }
                                if (dataSnapshot.child("comedy").value == true){
                                    categorySearch += "event_type%5B%5D=comedy"
                                }
                                if (dataSnapshot.child("family").value == true){
                                    categorySearch += "&event_type%5B%5D=family"
                                }
                                if (dataSnapshot.child("festival").value == true){
                                    categorySearch += "&event_type%5B%5D=festival"
                                }
                                if (dataSnapshot.child("foreign").value == true){
                                    categorySearch += "&event_type%5B%5D=foreign-language-subtitles"
                                }
                                if (dataSnapshot.child("music").value == true){
                                    categorySearch += "&event_type%5B%5D=music"
                                }
                                if (dataSnapshot.child("live").value == true){
                                    categorySearch += "&event_type%5B%5D=recorded-live-screening"
                                }
                                if (dataSnapshot.child("relaxed").value == true){
                                    categorySearch += "&event_type%5B%5D=relaxed"
                                }
                                if (dataSnapshot.child("talks").value == true){
                                    categorySearch += "&event_type%5B%5D=talks"
                                }
                                if (dataSnapshot.child("theatreDance").value == true){
                                    categorySearch += "&event_type%5B%5D=theathre-dance"
                                }
                                if (dataSnapshot.child("workshops").value == true){
                                    categorySearch += "&event_type%5B%5D=workshops"
                                }

                                    val categoryIntent = PendingIntent.getBroadcast(
                                            context,
                                            0,
                                            Intent(context, AlarmBroadcastReceiver::class.java).apply {
                                                putExtra("notificationId", 1)
                                                putExtra("categorySearch", categorySearch)
                                                putExtra("type", "category")
                                            },
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    )


                                    val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                                    alarmManager.setInexactRepeating(
                                            AlarmManager.RTC_WAKEUP,
                                            Calendar.getInstance().apply {
                                                set(Calendar.HOUR_OF_DAY, 9)
                                                set(Calendar.DAY_OF_WEEK, MONDAY)
                                            }.timeInMillis,
                                            AlarmManager.INTERVAL_DAY * 7,
                                            categoryIntent
                                    )
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Failed to read value
                            }
                        })

                return
            }

        }
    }