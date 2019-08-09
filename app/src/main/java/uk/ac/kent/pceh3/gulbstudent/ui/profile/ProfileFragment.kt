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
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_profile.*
import uk.ac.kent.pceh3.gulbstudent.model.Bookmarks
import uk.ac.kent.pceh3.gulbstudent.model.Categories
import uk.ac.kent.pceh3.gulbstudent.model.WhatsOn
import uk.ac.kent.pceh3.gulbstudent.network.AlarmBroadcastReceiver
import uk.ac.kent.pceh3.gulbstudent.ui.WhatsOnViewModel
import uk.ac.kent.pceh3.gulbstudent.ui.profile.ProfileAdapter
import uk.ac.kent.pceh3.gulbstudent.ui.profile.ProfileViewModel
import java.lang.Double
import java.text.SimpleDateFormat
import java.util.*


class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var database: DatabaseReference
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var categorySearch = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        user = auth.currentUser!!

        database = FirebaseDatabase.getInstance().reference.child("users").child(user!!.uid).child("categories")

        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveBtn.setOnClickListener {
            if (archive.isChecked) {
                database.child("archive").setValue(true)
            } else{
                database.child("archive").setValue(false)
            }
            if (audioDescribed.isChecked) {
                database.child("audioDescribed").setValue(true)
            } else{
                database.child("audioDescribed").setValue(false)
            }
            if (boing.isChecked) {
                database.child("boing").setValue(true)
            } else{
                database.child("boing").setValue(false)
            }
            if (cafe.isChecked) {
                database.child("cafe").setValue(true)
            } else{
                database.child("cafe").setValue(false)
            }
            if (captionedSubtitles.isChecked) {
                database.child("captionedSubtitles").setValue(true)
            } else{
                database.child("captionedSubtitles").setValue(false)
            }
            if (comedy.isChecked) {
                database.child("comedy").setValue(true)
            } else{
                database.child("comedy").setValue(false)
            }
            if (family.isChecked) {
                database.child("family").setValue(true)
            } else{
                database.child("family") .setValue(false)
            }
            if (festival.isChecked) {
                database.child("festival").setValue(true)
            } else{
                database.child("festival").setValue(false)
            }
            if (foreign.isChecked) {
                database.child("foreign").setValue(true)
            } else{
                database.child("foreign").setValue(false)
            }
            if (music.isChecked) {
                database.child("music").setValue(true)
            } else{
                database.child("music").setValue(false)
            }
            if (live.isChecked) {
                database.child("live").setValue(true)
            } else{
                database.child("live").setValue(false)
            }
            if (relaxed.isChecked) {
                database.child("relaxed").setValue(true)
            } else{
                database.child("relaxed").setValue(false)
            }
            if (talks.isChecked) {
                database.child("talks").setValue(true)
            } else{
                database.child("talks").setValue(false)
            }
            if (theatreDance.isChecked) {
                database.child("theatreDance").setValue(true)
            } else{
                database.child("theatreDance").setValue(false)
            }
            if (workshops.isChecked) {
                database.child("workshops").setValue(true)
            } else{
                database.child("workshops").setValue(false)
            }
        }


        loadBookmarks(user)
        loadCats(user)
    }

    fun loadBookmarks(user: FirebaseUser){
        val viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getBookmarks(user).observe(this, object : Observer<List<Bookmarks>> {
            override fun onChanged(t: List<Bookmarks>?) {
                val data = t
                if (data != null){
                    linearLayoutManager = LinearLayoutManager(context)
                    showsRV.layoutManager = linearLayoutManager
                    val ProfileAdapter = ProfileAdapter(data)
                    showsRV.adapter = ProfileAdapter
                    ProfileAdapter.updateData(data)
                }
            }
        })
    }

    fun loadCats(user: FirebaseUser){
        val viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getCats(user).observe(this, object : Observer<List<Categories>> {
            override fun onChanged(t: List<Categories>?) {
                val data = t
                if (data != null){
                    if (data[0].archive == true) {
                        archive.isChecked = true
                        setNotification("archive")
                    }
                    if (data[0].audioDescribed == true) {
                        audioDescribed.isChecked = true
                        setNotification("audioDescribed")
                    }
                    if (data[0].boing == true){
                        boing.isChecked = true
                        setNotification("boing")
                    }
                    if (data[0].cafe == true) {
                        cafe.isChecked = true
                        setNotification("cafe")
                    }
                    if (data[0].captionedSubtitles == true) {
                        captionedSubtitles.isChecked = true
                        setNotification("captionedSubtitles")
                    }
                    if (data[0].comedy == true) {
                        comedy.isChecked = true
                        setNotification("comedy")
                    }
                    if (data[0].family == true){
                        family.isChecked = true
                        setNotification("family")
                    }
                    if (data[0].festival == true){
                        festival.isChecked = true
                        setNotification("festival")
                    }
                    if (data[0].foreign == true) {
                        foreign.isChecked = true
                        setNotification("foreign")
                    }
                    if (data[0].music == true) {
                        music.isChecked = true
                        setNotification("music")
                    }
                    if (data[0].live == true) {
                        live.isChecked = true
                        setNotification("live")
                    }
                    if (data[0].relaxed == true) {
                        relaxed.isChecked = true
                        setNotification("relaxed")
                    }
                    if (data[0].talks == true) {
                        talks.isChecked = true
                        setNotification("talks")
                    }
                    if (data[0].theatreDance == true) {
                        theatreDance.isChecked = true
                        setNotification("theatreDance")
                    }
                    if (data[0].workshops == true) {
                        workshops.isChecked = true
                        setNotification("workshops")
                    }
                }
            }
        })
    }

    fun setNotification(category: String){

        val viewModel = ViewModelProviders.of(this).get(WhatsOnViewModel::class.java)
        viewModel.getWhatsOn(eventType, date).observe(this, object : Observer<List<WhatsOn>> {
            override fun onChanged(t: List<WhatsOn>?) {
                val data = t
                if (data != null){
//                    recyclerViewWO.layoutManager = layoutManager
//                    val rvWhatsOnAdapter = RvWhatsOnAdapter(data)
//                    recyclerViewWO.adapter = rvWhatsOnAdapter
//                    rvWhatsOnAdapter.updateData(data)
                }
            }
        })
//        val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        val categoryIntent = PendingIntent.getBroadcast(
//                context,
//                0,
//                Intent(activity, AlarmBroadcastReceiver::class.java).apply {
//                    putExtra("title", category)
//                },
//                PendingIntent.FLAG_CANCEL_CURRENT
//        )
//
//            val receiver = ComponentName(context!!, AlarmBroadcastReceiver::class.java)
//
//            this.context!!.packageManager.setComponentEnabledSetting(
//                    receiver,
//                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                    PackageManager.DONT_KILL_APP
//            )
//
//            alarmManager.set(
//                    AlarmManager.RTC_WAKEUP,
//                    Calendar.getInstance().apply {
//                        set(Calendar.HOUR_OF_DAY, 9)
//                        set(Calendar.MINUTE, 0)
//                        set(Calendar.SECOND, 0)
//                        set(Calendar.YEAR, year)
//                        set(Calendar.MONTH, month)
//                        set(Calendar.DATE, day)
//                    }.timeInMillis,
//                    categoryIntent
//            )

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        super.onPrepareOptionsMenu(menu)
    }


}