package uk.ac.kent.pceh3.gulbstudent

import android.app.AlarmManager
import android.app.PendingIntent
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_profile.*
import uk.ac.kent.pceh3.gulbstudent.model.Bookmarks
import uk.ac.kent.pceh3.gulbstudent.model.Categories
import uk.ac.kent.pceh3.gulbstudent.network.AlarmBroadcastReceiver
import uk.ac.kent.pceh3.gulbstudent.ui.profile.ProfileAdapter
import uk.ac.kent.pceh3.gulbstudent.ui.profile.ProfileViewModel
import java.util.*
import java.util.Calendar.MONDAY
import java.util.Calendar.SATURDAY


class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var database: DatabaseReference
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        user = auth.currentUser!!

        database = FirebaseDatabase.getInstance().reference.child("users").child(user.uid).child("categories")

        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveBtn.setOnClickListener {
            var categorySearch = ""
            if (archive.isChecked) {
                database.child("archive").setValue(true)
                categorySearch += "&event_type%5B%5D=archive"
            } else {
                database.child("archive").setValue(false)
            }
            if (audioDescribed.isChecked) {
                database.child("audioDescribed").setValue(true)
                categorySearch += "&event_type%5B%5D=audio-description"
            } else {
                database.child("audioDescribed").setValue(false)
            }
            if (boing.isChecked) {
                database.child("boing").setValue(true)
                categorySearch += "&event_type%5B%5D=boing-festival"
            } else {
                database.child("boing").setValue(false)
            }
            if (cafe.isChecked) {
                database.child("cafe").setValue(true)
                categorySearch += "&event_type%5B%5D=cafe"
            } else {
                database.child("cafe").setValue(false)
            }
            if (captionedSubtitles.isChecked) {
                database.child("captionedSubtitles").setValue(true)
                categorySearch += "&event_type%5B%5D=captioned-subtitles"
            } else {
                database.child("captionedSubtitles").setValue(false)
            }
            if (comedy.isChecked) {
                database.child("comedy").setValue(true)
                categorySearch += "event_type%5B%5D=comedy"
            } else {
                database.child("comedy").setValue(false)
            }
            if (family.isChecked) {
                database.child("family").setValue(true)
                categorySearch += "&event_type%5B%5D=family"
            } else {
                database.child("family").setValue(false)
            }
            if (festival.isChecked) {
                database.child("festival").setValue(true)
                categorySearch += "&event_type%5B%5D=festival"
            } else {
                database.child("festival").setValue(false)
            }
            if (foreign.isChecked) {
                database.child("foreign").setValue(true)
                categorySearch += "&event_type%5B%5D=foreign-language-subtitles"
            } else {
                database.child("foreign").setValue(false)
            }
            if (music.isChecked) {
                database.child("music").setValue(true)
                categorySearch += "&event_type%5B%5D=music"
            } else {
                database.child("music").setValue(false)
            }
            if (live.isChecked) {
                database.child("live").setValue(true)
                categorySearch += "&event_type%5B%5D=recorded-live-screening"
            } else {
                database.child("live").setValue(false)
            }
            if (relaxed.isChecked) {
                database.child("relaxed").setValue(true)
                categorySearch += "&event_type%5B%5D=relaxed"
            } else {
                database.child("relaxed").setValue(false)
            }
            if (talks.isChecked) {
                database.child("talks").setValue(true)
                categorySearch += "&event_type%5B%5D=talks"
            } else {
                database.child("talks").setValue(false)
            }
            if (theatreDance.isChecked) {
                database.child("theatreDance").setValue(true)
                categorySearch += "&event_type%5B%5D=theathre-dance"
            } else {
                database.child("theatreDance").setValue(false)
            }
            if (workshops.isChecked) {
                database.child("workshops").setValue(true)
                categorySearch += "&event_type%5B%5D=workshops"
            } else {
                database.child("workshops").setValue(false)
            }
            setNotification(categorySearch)
        }


        loadBookmarks(user)
        loadCats(user)
    }

    private fun loadBookmarks(user: FirebaseUser) {
        val viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getBookmarks(user).observe(this, Observer<List<Bookmarks>> { t ->
            if (t != null) {
                linearLayoutManager = LinearLayoutManager(context)
                showsRV.layoutManager = linearLayoutManager
                val profileAdapter = ProfileAdapter(t)
                showsRV.adapter = profileAdapter
                profileAdapter.updateData(t)
            }
        })
    }

    private fun loadCats(user: FirebaseUser) {
        val viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getCats(user).observe(this, Observer<List<Categories>> { t ->
            if (t != null) {

                if (t[0].archive) {
                    archive.isChecked = true
                }else {
                    archive.isChecked = false
                }
                if (t[0].audioDescribed) {
                    audioDescribed.isChecked = true
                }else {
                    audioDescribed.isChecked = false
                }
                if (t[0].boing) {
                    boing.isChecked = true
                }else {
                    boing.isChecked = false
                }
                if (t[0].cafe) {
                    cafe.isChecked = true
                }else {
                    cafe.isChecked = false
                }
                if (t[0].captionedSubtitles) {
                    captionedSubtitles.isChecked = true
                }else {
                    captionedSubtitles.isChecked = false
                }
                if (t[0].comedy) {
                    comedy.isChecked = true
                }else {
                    comedy.isChecked = false
                }
                if (t[0].family) {
                    family.isChecked = true
                }else {
                    family.isChecked = false
                }
                if (t[0].festival) {
                    festival.isChecked = true
                }else {
                    festival.isChecked = false
                }
                if (t[0].foreign) {
                    foreign.isChecked = true
                }else {
                    foreign.isChecked = false
                }
                if (t[0].music) {
                    music.isChecked = true
                }else {
                    music.isChecked = false
                }
                if (t[0].live) {
                    live.isChecked = true
                }else {
                    live.isChecked = false
                }
                if (t[0].relaxed) {
                    relaxed.isChecked = true
                }else {
                    relaxed.isChecked = false
                }
                if (t[0].talks) {
                    talks.isChecked = true
                }else {
                    talks.isChecked = false
                }
                if (t[0].theatreDance) {
                    theatreDance.isChecked = true
                }else {
                    theatreDance.isChecked = false
                }
                if (t[0].workshops) {
                    workshops.isChecked = true
                }else {
                    workshops.isChecked = false
                }

            }
        })
    }

    private fun setNotification(category: String) {

        val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val categoryIntent = PendingIntent.getBroadcast(
                context,
                0,
                Intent(activity, AlarmBroadcastReceiver::class.java).apply {
                    putExtra("notificationId", 1)
                    putExtra("categorySearch", category)
                    putExtra("type", "category")
                },
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val receiver = ComponentName(context!!, AlarmBroadcastReceiver::class.java)

        activity!!.packageManager.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
        )

        var calender = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.DAY_OF_WEEK, MONDAY)
        }

        val now = Calendar.getInstance()
        now.set(Calendar.SECOND, 0)
        now.set(Calendar.MILLISECOND, 0)

        if (calender.before(now)) {
            println("BEFORE")
            //this condition is used for future reminder that means your reminder not fire for past time
            calender.add(Calendar.DATE, 7)
        }
        else{
            println("AFTER")
        }

        alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calender.timeInMillis,
                AlarmManager.INTERVAL_DAY * 7,
                categoryIntent
        )

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        super.onPrepareOptionsMenu(menu)
    }


}