package uk.ac.kent.pceh3.gulbstudent.ui.whatson

import android.app.*
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ComponentName
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getColorStateList
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_profile.*
import uk.ac.kent.pceh3.gulbstudent.MainActivity
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.model.Bookmarks
import uk.ac.kent.pceh3.gulbstudent.model.WhatsOn
import uk.ac.kent.pceh3.gulbstudent.network.AlarmBroadcastReceiver
import uk.ac.kent.pceh3.gulbstudent.ui.WhatsOnViewModel
import uk.ac.kent.pceh3.gulbstudent.ui.profile.ProfileAdapter
import uk.ac.kent.pceh3.gulbstudent.ui.profile.ProfileViewModel
import java.lang.Double.parseDouble
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

class EventFragment : Fragment() {
    private var isFABOpen: Boolean? = false
    private var day = 1
    private var month = 1
    private var year = 2019
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var indexUrl : String
    private var bookmarked = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        setHasOptionsMenu(true)

        auth = FirebaseAuth.getInstance()
        return view
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        super.onPrepareOptionsMenu(menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val event = this.arguments?.getParcelable("event") as WhatsOn

        excerpt?.text = event.excerpt
        date?.text = event.date
        title?.text = event.title
        label?.text = event.label
        Picasso.get()
                .load(event.imageUrl)
                .placeholder(R.drawable.logo)
                .into(image)


        val urlRe = Regex("[^A-Za-z0-9 ]")
        indexUrl = urlRe.replace(event.url.toString(), "")

        isBookmarked()




        fabPlus.setOnClickListener {
            if (!isFABOpen!!) {
                showFABMenu()
            } else {
                closeFABMenu()
            }
        }

        fabBuy.setOnClickListener{

            val activity = context as AppCompatActivity

            val newFragment = WebviewFragment()
            val args = Bundle()
            args.putCharSequence("url", event.bookLink)

            newFragment.arguments = args

            activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content, newFragment)
                    .addToBackStack(null)
                    .commit()

        }

        fabShare.setOnClickListener{
            val openURL = Intent(Intent.ACTION_SEND)
            openURL.putExtra(Intent.EXTRA_TEXT, event.url)
            openURL.type = "text/plain"
            startActivity(Intent.createChooser(openURL, "Sharing Option"))
        }

        fabWeb.setOnClickListener{

            val activity = context as AppCompatActivity

            val newFragment = WebviewFragment()
            val args = Bundle()
            args.putCharSequence("url", event.url)

            newFragment.arguments = args

            activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content, newFragment)
                    .addToBackStack(null)
                    .commit()

        }

        bookmark.setOnClickListener {
            val alarmManager = activity!!.getSystemService(ALARM_SERVICE) as AlarmManager

            var user = auth.currentUser
            val id = System.currentTimeMillis().toInt()

            val bookmarkIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    Intent(activity, AlarmBroadcastReceiver::class.java).apply {
                        putExtra("notificationId", id)
                        putExtra("title", event.title)
                        putExtra("url", indexUrl)
                        putExtra("type", "bookmark")
                    },
                    PendingIntent.FLAG_CANCEL_CURRENT
            )
            if (!bookmarked) {

                val receiver = ComponentName(context!!, AlarmBroadcastReceiver::class.java)

                this.context!!.packageManager.setComponentEnabledSetting(
                        receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                )

                val dateFormatted = event.date
                val re = Regex("[^A-Za-z0-9 ]")
                val splitDate = re.replace(dateFormatted.toString(), "")
                val splitDate2 = splitDate.split("\\s".toRegex())

                for (w in splitDate2) {
                    var numeric = true
                    try {
                        val num = parseDouble(w)
                    } catch (e: NumberFormatException) {
                        numeric = false
                    }
                    if (numeric) {
                        if (w.toInt() < 32) {
                            day = w.toInt()
                        }

                        if (w.length == 4) {
                            year = w.toInt()
                        }
                    } else {
                        if (w == "January" || w == "February" || w == "March" || w == "April" || w == "May" || w == "June" || w == "July" || w == "August"
                                || w == "September" || w == "October" || w == "November" || w == "December" || w == "Jan" || w == "Feb" || w == "Mar"
                                || w == "Apr" || w == "May" || w == "Jun" || w == "Jul" || w == "Aug" || w == "Sep" || w == "Oct" || w == "Nov" || w == "Dec") {

                            val newDate = SimpleDateFormat("MMM", Locale.ENGLISH).parse(w)
                            val cal = getInstance()
                            cal.time = newDate
                            val newMonth = cal.get(MONTH)

                            month = newMonth
                        }
                    }
                }

                alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        Calendar.getInstance().apply {
                            set(HOUR_OF_DAY, 9)
                            set(MINUTE, 0)
                            set(SECOND, 0)
                            set(YEAR, year)
                            set(MONTH, month)
                            set(DATE, 9)
                        }.timeInMillis,
                        bookmarkIntent
                )

                bookmark.backgroundTintList = getColorStateList(context!!, R.color.colorPrimaryDark)
                bookmark.text = "Event Bookmarked"
                bookmark.setTextColor(resources.getColor(R.color.colorAccent))

                database = FirebaseDatabase.getInstance().reference.child("users").child(user!!.uid).child("bookmarked").child(indexUrl)

                database.child("title").setValue(event.title)
                database.child("year").setValue(year)
                database.child("month").setValue(month)
                database.child("date").setValue(day)
                database.child("index").setValue(indexUrl)
                database.child("id").setValue(id)

                bookmarked = true
            }
            else{
                val receiver = ComponentName(context!!, AlarmBroadcastReceiver::class.java)

                this.context!!.packageManager.setComponentEnabledSetting(
                        receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                )

                bookmark.backgroundTintList = getColorStateList(context!!, R.color.colorAccent)
                bookmark.text = "Bookmark"
                bookmark.setTextColor(resources.getColor(R.color.colorPrimaryDark))

                FirebaseDatabase.getInstance().reference.child("users").child(user!!.uid).child("bookmarked").child(indexUrl).removeValue()

                alarmManager.cancel(bookmarkIntent)

                bookmarked = false
            }
        }
    }


    private fun showFABMenu() {
        isFABOpen = true
        fabBuy.animate().translationY(-150f)
        fabShare.animate().translationY(-275f)
        fabWeb.animate().translationY(-400f)
        ViewCompat.animate(fabPlus)
                .rotation(45.0f)
                .withLayer()
                .setDuration(300L)
                .start()
    }

    private fun closeFABMenu() {
        isFABOpen = false
        fabBuy.animate().translationY(0f)
        fabShare.animate().translationY(0f)
        fabWeb.animate().translationY(0f)
        ViewCompat.animate(fabPlus)
                .rotation(0.0f)
                .withLayer()
                .setDuration(300L)
                .start()

    }

    private fun isBookmarked(){

        var user = auth.currentUser

            val viewModel = ViewModelProviders.of(this).get(WhatsOnViewModel::class.java)
            viewModel.getShowBookmarked(user!!, indexUrl).observe(this, object : Observer<Boolean> {
                override fun onChanged(t: Boolean?) {
                   if (t == true){
                       bookmark.backgroundTintList = getColorStateList(context!!, R.color.colorPrimaryDark)
                       bookmark.text = "Event Bookmarked"
                       bookmark.setTextColor(resources.getColor(R.color.colorAccent))

                       bookmarked = true
                   }
                    else {

                       bookmark.backgroundTintList = getColorStateList(context!!, R.color.colorAccent)
                       bookmark.text = "Bookmark"
                       bookmark.setTextColor(resources.getColor(R.color.colorPrimaryDark))

                       bookmarked = false
                   }

                }
            })
    }

}