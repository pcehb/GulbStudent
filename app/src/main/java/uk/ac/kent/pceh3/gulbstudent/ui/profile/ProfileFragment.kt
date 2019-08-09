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
import uk.ac.kent.pceh3.gulbstudent.model.WhatsOn
import uk.ac.kent.pceh3.gulbstudent.network.AlarmBroadcastReceiver
import uk.ac.kent.pceh3.gulbstudent.ui.WhatsOnViewModel
import uk.ac.kent.pceh3.gulbstudent.ui.profile.ProfileAdapter
import uk.ac.kent.pceh3.gulbstudent.ui.profile.ProfileViewModel
import java.util.*


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

        database = FirebaseDatabase.getInstance().reference.child("users").child(user!!.uid).child("categories")

        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveBtn.setOnClickListener {
            if (archive.isChecked) {
                database.child("archive").setValue(true)
            } else {
                database.child("archive").setValue(false)
            }
            if (audioDescribed.isChecked) {
                database.child("audioDescribed").setValue(true)
            } else {
                database.child("audioDescribed").setValue(false)
            }
            if (boing.isChecked) {
                database.child("boing").setValue(true)
            } else {
                database.child("boing").setValue(false)
            }
            if (cafe.isChecked) {
                database.child("cafe").setValue(true)
            } else {
                database.child("cafe").setValue(false)
            }
            if (captionedSubtitles.isChecked) {
                database.child("captionedSubtitles").setValue(true)
            } else {
                database.child("captionedSubtitles").setValue(false)
            }
            if (comedy.isChecked) {
                database.child("comedy").setValue(true)
            } else {
                database.child("comedy").setValue(false)
            }
            if (family.isChecked) {
                database.child("family").setValue(true)
            } else {
                database.child("family").setValue(false)
            }
            if (festival.isChecked) {
                database.child("festival").setValue(true)
            } else {
                database.child("festival").setValue(false)
            }
            if (foreign.isChecked) {
                database.child("foreign").setValue(true)
            } else {
                database.child("foreign").setValue(false)
            }
            if (music.isChecked) {
                database.child("music").setValue(true)
            } else {
                database.child("music").setValue(false)
            }
            if (live.isChecked) {
                database.child("live").setValue(true)
            } else {
                database.child("live").setValue(false)
            }
            if (relaxed.isChecked) {
                database.child("relaxed").setValue(true)
            } else {
                database.child("relaxed").setValue(false)
            }
            if (talks.isChecked) {
                database.child("talks").setValue(true)
            } else {
                database.child("talks").setValue(false)
            }
            if (theatreDance.isChecked) {
                database.child("theatreDance").setValue(true)
            } else {
                database.child("theatreDance").setValue(false)
            }
            if (workshops.isChecked) {
                database.child("workshops").setValue(true)
            } else {
                database.child("workshops").setValue(false)
            }
        }


        loadBookmarks(user)
        loadCats(user)
    }

    fun loadBookmarks(user: FirebaseUser) {
        val viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getBookmarks(user).observe(this, object : Observer<List<Bookmarks>> {
            override fun onChanged(t: List<Bookmarks>?) {
                val data = t
                if (data != null) {
                    linearLayoutManager = LinearLayoutManager(context)
                    showsRV.layoutManager = linearLayoutManager
                    val ProfileAdapter = ProfileAdapter(data)
                    showsRV.adapter = ProfileAdapter
                    ProfileAdapter.updateData(data)
                }
            }
        })
    }

    fun loadCats(user: FirebaseUser) {
        val viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getCats(user).observe(this, object : Observer<List<Categories>> {
            override fun onChanged(t: List<Categories>?) {
                val data = t
                if (data != null) {
                    var categorySearch = ""
                    if (data[0].archive == true) {
                        archive.isChecked = true
                        categorySearch += "&event_type%5B%5D=archive"
                    } else {
                        archive.isChecked = false
                    }
                    if (data[0].audioDescribed == true) {
                        audioDescribed.isChecked = true
                        categorySearch += "&event_type%5B%5D=audio-description"
                    } else {
                        audioDescribed.isChecked = false
                    }
                    if (data[0].boing == true) {
                        boing.isChecked = true
                        categorySearch += "&event_type%5B%5D=boing-festival"
                    } else {
                        boing.isChecked = false
                    }
                    if (data[0].cafe == true) {
                        cafe.isChecked = true
                        categorySearch += "&event_type%5B%5D=cafe"
                    } else {
                        cafe.isChecked = false
                    }
                    if (data[0].captionedSubtitles == true) {
                        captionedSubtitles.isChecked = true
                        categorySearch += "&event_type%5B%5D=captioned-subtitles"
                    } else {
                        captionedSubtitles.isChecked = false
                    }
                    if (data[0].comedy == true) {
                        comedy.isChecked = true
                        categorySearch += "event_type%5B%5D=comedy"
                    } else {
                        comedy.isChecked = false
                    }
                    if (data[0].family == true) {
                        family.isChecked = true
                        categorySearch += "&event_type%5B%5D=family"
                    } else {
                        family.isChecked = false
                    }
                    if (data[0].festival == true) {
                        festival.isChecked = true
                        categorySearch += "&event_type%5B%5D=festival"
                    } else {
                        festival.isChecked = false
                    }
                    if (data[0].foreign == true) {
                        foreign.isChecked = true
                        categorySearch += "&event_type%5B%5D=foreign-language-subtitles"
                    } else {
                        foreign.isChecked = false
                    }
                    if (data[0].music == true) {
                        music.isChecked = true
                        categorySearch += "&event_type%5B%5D=music"
                    } else {
                        music.isChecked = false
                    }
                    if (data[0].live == true) {
                        live.isChecked = true
                        categorySearch += "&event_type%5B%5D=recorded-live-screening"
                    } else {
                        live.isChecked = false
                    }
                    if (data[0].relaxed == true) {
                        relaxed.isChecked = true
                        categorySearch += "&event_type%5B%5D=relaxed"
                    } else {
                        relaxed.isChecked = false
                    }
                    if (data[0].talks == true) {
                        talks.isChecked = true
                        categorySearch += "&event_type%5B%5D=talks"
                    } else {
                        talks.isChecked = false
                    }
                    if (data[0].theatreDance == true) {
                        theatreDance.isChecked = true
                        categorySearch += "&event_type%5B%5D=theathre-dance"
                    } else {
                        theatreDance.isChecked = false
                    }
                    if (data[0].workshops == true) {
                        workshops.isChecked = true
                        categorySearch += "&event_type%5B%5D=workshops"
                    } else {
                        workshops.isChecked = false
                    }
                    setNotification(categorySearch)
                }
            }
        })
    }

    fun setNotification(category: String) {
        val viewModel = ViewModelProviders.of(this).get(WhatsOnViewModel::class.java)
        viewModel.getWhatsOn("", category, "").observe(this, object : Observer<List<WhatsOn>> {
            override fun onChanged(t: List<WhatsOn>?) {
                val data = t
                if (data != null) {
                    val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val idNum = System.currentTimeMillis().toInt()
                    val categoryIntent = PendingIntent.getBroadcast(
                            context,
                            0,
                            Intent(activity, AlarmBroadcastReceiver::class.java).apply {
                                putExtra("notificationId", idNum)
                                putExtra("categorySearch", category)
                                putExtra("type", "category")
                            },
                            PendingIntent.FLAG_CANCEL_CURRENT
                    )

                    val receiver = ComponentName(context!!, AlarmBroadcastReceiver::class.java)

                    activity!!.packageManager.setComponentEnabledSetting(
                            receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP
                    )

                    alarmManager.set(
                            AlarmManager.RTC_WAKEUP,
                            Calendar.getInstance().apply {
                                set(Calendar.HOUR_OF_DAY, 9)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.YEAR, 2019)
                                set(Calendar.MONTH, 7)
                                set(Calendar.DATE, 9)
                            }.timeInMillis,
                            categoryIntent
                    )
                }
            }
        })


    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        super.onPrepareOptionsMenu(menu)
    }


}