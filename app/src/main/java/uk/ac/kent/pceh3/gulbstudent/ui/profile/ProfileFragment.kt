package uk.ac.kent.pceh3.gulbstudent

import android.app.AlarmManager
import android.app.PendingIntent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_profile.*
import uk.ac.kent.pceh3.gulbstudent.model.Bookmarks
import uk.ac.kent.pceh3.gulbstudent.model.Categories
import uk.ac.kent.pceh3.gulbstudent.network.AlarmBroadcastReceiver
import uk.ac.kent.pceh3.gulbstudent.ui.comp.CompViewModel
import uk.ac.kent.pceh3.gulbstudent.ui.profile.ProfileAdapter
import uk.ac.kent.pceh3.gulbstudent.ui.profile.ProfileViewModel
import java.util.*
import java.util.Calendar.MONDAY


class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var database: DatabaseReference
    private lateinit var gulbDatabase: DatabaseReference
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var gulbCard : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        user = auth.currentUser!!

        database = FirebaseDatabase.getInstance().reference.child("users").child(user.uid).child("categories")

        gulbDatabase = FirebaseDatabase.getInstance().reference.child("users").child(user.uid).child("gulbCard")

        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(CompViewModel::class.java)

        viewModel.getGulbCard(user).observe(this, Observer<Boolean> { t ->
            gulbCard = t == true
            switch1.isChecked = gulbCard
        })

        switch1.setOnCheckedChangeListener{ _: CompoundButton, b: Boolean ->
            if (b){
                //push true
                gulbDatabase.setValue(true)
            }
            else {
                //push false
                gulbDatabase.setValue(false)
            }
        }

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

                archive.isChecked = t[0].archive
                audioDescribed.isChecked = t[0].audioDescribed
                boing.isChecked = t[0].boing
                cafe.isChecked = t[0].cafe
                captionedSubtitles.isChecked = t[0].captionedSubtitles
                comedy.isChecked = t[0].comedy
                family.isChecked = t[0].family
                festival.isChecked = t[0].festival
                foreign.isChecked = t[0].foreign
                music.isChecked = t[0].music
                live.isChecked = t[0].live
                relaxed.isChecked = t[0].relaxed
                talks.isChecked = t[0].talks
                theatreDance.isChecked = t[0].theatreDance
                workshops.isChecked = t[0].workshops
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

        val calender = Calendar.getInstance().apply {
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