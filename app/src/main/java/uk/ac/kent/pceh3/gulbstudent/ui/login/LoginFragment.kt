package uk.ac.kent.pceh3.gulbstudent.ui.login

import android.app.ActivityOptions
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.WhatsOnFragment
import uk.ac.kent.pceh3.gulbstudent.model.Bookmarks
import uk.ac.kent.pceh3.gulbstudent.network.AlarmBroadcastReceiver
import uk.ac.kent.pceh3.gulbstudent.ui.DetailActivity
import java.util.*

//login fragment
class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        setHasOptionsMenu(true)
        auth = FirebaseAuth.getInstance()

        activity!!.toolBar.navigationIcon = null

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = context as AppCompatActivity

        loginButton.setOnClickListener {
            // get entered details
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            Log.d(TAG, "signIn:$email")

            //signin
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this.requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")

                            val user = auth.currentUser

                            println("USER: " + user?.uid)

                            //set bookmark notifications
                            FirebaseDatabase.getInstance().reference.child("users").child(user!!.uid).child("bookmarked")
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            for (snapshot: DataSnapshot in dataSnapshot.children) {
                                                val bookmarks = snapshot.getValue(Bookmarks::class.java)

                                                val bookmarkIntent = PendingIntent.getBroadcast(
                                                        activity,
                                                        bookmarks!!.id!!,
                                                        Intent(activity, AlarmBroadcastReceiver::class.java).apply {
                                                            putExtra("title", bookmarks.title!!)
                                                            putExtra("url", snapshot.key)
                                                            putExtra("notificationId", bookmarks.id!!)
                                                            putExtra("type", "bookmark")
                                                        },
                                                        PendingIntent.FLAG_CANCEL_CURRENT
                                                )

                                                val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                                                //set notification 9am of show day
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


                            //set categories notification
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
                                                    activity,
                                                    0,
                                                    Intent(activity, AlarmBroadcastReceiver::class.java).apply {
                                                        putExtra("notificationId", 1)
                                                        putExtra("categorySearch", categorySearch)
                                                        putExtra("type", "category")
                                                    },
                                                    PendingIntent.FLAG_UPDATE_CURRENT
                                            )


                                            val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager

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
                                            } else {
                                                println("AFTER")
                                            }

                                            // set repeat every monday 9am
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

                            activity.viewPager.currentItem = 0
                            activity.content.visibility = View.GONE
                            activity.viewPager.visibility = View.VISIBLE
                            activity.tab_layout.visibility = View.VISIBLE

                            activity.toolBar.setNavigationIcon(R.drawable.icons8_menu_24)


                            activity.supportFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.content, WhatsOnFragment())
                                    .commit()


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(activity, task.exception!!.message, Toast.LENGTH_SHORT).show()
                        }

                    }
        }

        createAccountButton.setOnClickListener {

            val activity = context as AppCompatActivity

            activity.let {
                val intent = Intent(it, DetailActivity::class.java)
                intent.putExtra("openingFragment", "create")

                val options = ActivityOptions.makeSceneTransitionAnimation(activity)
                it.startActivity(intent, options.toBundle())
            }
        }

        forgotPasswordButton.setOnClickListener {
            val activity = context as AppCompatActivity

            activity.let {
                val intent = Intent(it, DetailActivity::class.java)
                intent.putExtra("openingFragment", "reset")

                val options = ActivityOptions.makeSceneTransitionAnimation(activity)
                it.startActivity(intent, options.toBundle())
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        super.onPrepareOptionsMenu(menu)
    }

}