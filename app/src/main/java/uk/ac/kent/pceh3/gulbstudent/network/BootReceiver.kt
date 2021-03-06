package uk.ac.kent.pceh3.gulbstudent.network

import android.Manifest
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk.ac.kent.pceh3.gulbstudent.model.Bookmarks
import java.util.*

// broadcast receiver for boot

class BootReceiver : BroadcastReceiver() {

    private lateinit var auth: FirebaseAuth

    lateinit var geofence : Geofence


    override fun onReceive(context: Context?, intent: Intent?) {

// if phone booted
        if (intent!!.action == "android.intent.action.BOOT_COMPLETED") {

            Log.d(ContentValues.TAG, "onReceive: BOOT_COMPLETED")

            auth = FirebaseAuth.getInstance()
            val user = auth.currentUser

            // if the user is logged in set notifications
            if (user != null) {
                //bookmark notifications
                FirebaseDatabase.getInstance().reference.child("users").child(user.uid).child("bookmarked")
                        .addListenerForSingleValueEvent(object : ValueEventListener {

                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (snapshot: DataSnapshot in dataSnapshot.children) {
                                    val bookmarks = snapshot.getValue(Bookmarks::class.java)

                                    val bookmarkIntent = PendingIntent.getBroadcast(
                                            context,
                                            bookmarks!!.id!!,
                                            Intent(context, AlarmBroadcastReceiver::class.java).apply {
                                                putExtra("title", bookmarks.title!!)
                                                putExtra("url", snapshot.key)
                                                putExtra("notificationId", bookmarks.id!!)
                                                putExtra("type", "bookmark")
                                            },
                                            PendingIntent.FLAG_CANCEL_CURRENT
                                    )

                                    val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                                    // set alarm to show 9am on date of show
                                    alarmManager.set(
                                            AlarmManager.RTC_WAKEUP,
                                            Calendar.getInstance().apply {
                                                set(Calendar.HOUR_OF_DAY, 9)
                                                set(Calendar.MINUTE, 0)
                                                set(Calendar.SECOND, 0)
                                                set(Calendar.YEAR, bookmarks.year!!)
                                                set(Calendar.MONTH, bookmarks.month!!)
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


                // category notifications
                FirebaseDatabase.getInstance().reference.child("users").child(user.uid).child("categories")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                var categorySearch = ""
                                if (dataSnapshot.child("archive").value == true) {
                                    categorySearch += "&event_type%5B%5D=archive"
                                }
                                if (dataSnapshot.child("audioDescribed").value == true) {
                                    categorySearch += "&event_type%5B%5D=audio-description"
                                }
                                if (dataSnapshot.child("boing").value == true) {
                                    categorySearch += "&event_type%5B%5D=boing-festival"
                                }
                                if (dataSnapshot.child("cafe").value == true) {
                                    categorySearch += "&event_type%5B%5D=cafe"
                                }
                                if (dataSnapshot.child("captionedSubtitles").value == true) {
                                    categorySearch += "&event_type%5B%5D=captioned-subtitles"
                                }
                                if (dataSnapshot.child("comedy").value == true) {
                                    categorySearch += "event_type%5B%5D=comedy"
                                }
                                if (dataSnapshot.child("family").value == true) {
                                    categorySearch += "&event_type%5B%5D=family"
                                }
                                if (dataSnapshot.child("festival").value == true) {
                                    categorySearch += "&event_type%5B%5D=festival"
                                }
                                if (dataSnapshot.child("foreign").value == true) {
                                    categorySearch += "&event_type%5B%5D=foreign-language-subtitles"
                                }
                                if (dataSnapshot.child("music").value == true) {
                                    categorySearch += "&event_type%5B%5D=music"
                                }
                                if (dataSnapshot.child("live").value == true) {
                                    categorySearch += "&event_type%5B%5D=recorded-live-screening"
                                }
                                if (dataSnapshot.child("relaxed").value == true) {
                                    categorySearch += "&event_type%5B%5D=relaxed"
                                }
                                if (dataSnapshot.child("talks").value == true) {
                                    categorySearch += "&event_type%5B%5D=talks"
                                }
                                if (dataSnapshot.child("theatreDance").value == true) {
                                    categorySearch += "&event_type%5B%5D=theathre-dance"
                                }
                                if (dataSnapshot.child("workshops").value == true) {
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

                                val calender = Calendar.getInstance().apply {
                                    set(Calendar.HOUR_OF_DAY, 9)
                                    set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                                }

                                val now = Calendar.getInstance()
                                now.set(Calendar.SECOND, 0)
                                now.set(Calendar.MILLISECOND, 0)

                                if (calender.before(now)) {
                                    println("BEFORE")
                                    calender.add(Calendar.DATE, 7)
                                }
                                else{
                                    println("AFTER")
                                }

                                //set to repeat every monday at 9am
                                alarmManager.setInexactRepeating(
                                        AlarmManager.RTC_WAKEUP,
                                        calender.timeInMillis,
                                        AlarmManager.INTERVAL_DAY * 7,
                                        categoryIntent
                                )
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Failed to read value
                            }
                        })
            }

            // add geofence
            addGeo(context!!, success = {
                println("GEOFENCE SUCCESS")
                Toast.makeText(context, "SUCCESS", Toast.LENGTH_LONG).show()
            },
                    failure = {
                        println("GEOFENCE FAILURE")
                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    })

            return
        }

    }

    fun addGeo(context: Context, success: () -> Unit,
               failure: (error: String) -> Unit) {

        val geofencePendingIntent: PendingIntent by lazy {
            val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
            PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
        }


        // 1

        val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)

        geofence = Geofence.Builder()
                .setRequestId("gulbenkian")
                // latlong of the gulbenkian
                .setCircularRegion(
                        51.298564,
                        1.069307,
                        400.toFloat()
                )
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build()

        if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 2
            geofencingClient
                    .addGeofences(buildGeofencingRequest(geofence), geofencePendingIntent)
                    .addOnSuccessListener {
                        success()
                    }
                    .addOnFailureListener {
                        // 4
                        failure(GeofenceErrorMessages.getErrorString(context, it))
                    }
        }
    }

    fun buildGeofencingRequest(geofence: Geofence): GeofencingRequest {
        println("GEOFENCE BUILD REQUEST")
        return GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofences(listOf(geofence))
                .build()
    }
}