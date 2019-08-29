package uk.ac.kent.pceh3.gulbstudent.ui.whatson

import android.app.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.ComponentName
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat.getColorStateList
import androidx.core.view.ViewCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_event.*
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.network.AlarmBroadcastReceiver
import uk.ac.kent.pceh3.gulbstudent.ui.WhatsOnViewModel
import java.lang.Double.parseDouble
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

//event fragment
@Suppress("DEPRECATION")
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

        excerpt?.text = this.arguments?.getString("excerpt")
        date?.text = this.arguments?.getString("date")
        title?.text = this.arguments?.getString("title")
        label?.text =  this.arguments?.getString("label")
        Picasso.get()
                .load( this.arguments?.getString("imageUrl"))
                .placeholder(R.drawable.logo)
                .into(image)


        val urlRe = Regex("[^A-Za-z0-9 ]")
        indexUrl = urlRe.replace(this.arguments?.getString("url").toString(), "")

        isBookmarked()

        //show fabs
        fabPlus.setOnClickListener {
            if (!isFABOpen!!) {
                showFABMenu()
            } else {
                closeFABMenu()
            }
        }

        //allows user to buy ticket on gulb website (in webview)
        fabBuy.setOnClickListener{

            val activity = context as AppCompatActivity

            val newFragment = WebviewFragment()
            val args = Bundle()
            args.putCharSequence("url",  this.arguments?.getString("bookLink"))

            newFragment.arguments = args

            activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content, newFragment)
                    .addToBackStack(null)
                    .commit()

        }
        //allows user to share link to show (website link)
        fabShare.setOnClickListener{
            val openURL = Intent(Intent.ACTION_SEND)
            openURL.putExtra(Intent.EXTRA_TEXT,  this.arguments?.getString("url"))
            openURL.type = "text/plain"
            startActivity(Intent.createChooser(openURL, "Sharing Option"))
        }

        //allows user to view show on gulb website (in webview)
        fabWeb.setOnClickListener{

            val activity = context as AppCompatActivity

            val newFragment = WebviewFragment()
            val args = Bundle()
            args.putCharSequence("url",  this.arguments?.getString("url"))

            newFragment.arguments = args

            activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content, newFragment)
                    .addToBackStack(null)
                    .commit()

        }

        //set bookmark for show
        bookmark.setOnClickListener {
            val alarmManager = activity!!.getSystemService(ALARM_SERVICE) as AlarmManager

            val user = auth.currentUser
            val id = System.currentTimeMillis().toInt() //unique id of current time

            val bookmarkIntent = PendingIntent.getBroadcast(
                    context,
                    id,
                    Intent(context, AlarmBroadcastReceiver::class.java).apply {
                        putExtra("notificationId", id)
                        putExtra("title",  this@EventFragment.arguments?.getString("title"))
                        putExtra("url", indexUrl)
                        putExtra("type", "bookmark")
                        putExtra("openingFragment", "showEvent")
                        putExtra("excerpt", this@EventFragment.arguments?.getString("excerpt"))
                        putExtra("date", this@EventFragment.arguments?.getString("date"))
                        putExtra("label", this@EventFragment.arguments?.getString("label"))
                        putExtra("imageUrl",this@EventFragment.arguments?.getString("imageUrl"))
                        putExtra("bookLink", this@EventFragment.arguments?.getString("bookLink"))
                        putExtra("index", this@EventFragment.arguments?.getInt("index", 1))
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

                val dateFormatted =  this.arguments?.getString("date")
                val re = Regex("[^A-Za-z0-9 ]")
                val splitDate = re.replace(dateFormatted.toString(), "")
                val splitDate2 = splitDate.split("\\s".toRegex())

                for (w in splitDate2) {
                    var numeric = true
                    try {
                        parseDouble(w)
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

                // set alarm to go off at 9am on day of show
                alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        Calendar.getInstance().apply {
                            set(HOUR_OF_DAY, 9)
                            set(MINUTE, 0)
                            set(SECOND, 0)
                            set(YEAR, year)
                            set(MONTH, month)
                            set(DATE, day)
                        }.timeInMillis,
                        bookmarkIntent
                )

                bookmark.backgroundTintList = getColorStateList(context!!, R.color.colorPrimaryDark)
                bookmark.text = "Event Bookmarked"
                bookmark.setTextColor(resources.getColor(R.color.colorAccent))


                // add bookmark to firebase under user
                database = FirebaseDatabase.getInstance().reference.child("users").child(user!!.uid).child("bookmarked").child(indexUrl)

                database.child("title").setValue(this.arguments?.getString("title"))
                database.child("year").setValue(year)
                database.child("month").setValue(month)
                database.child("date").setValue(day)
                database.child("index").setValue(indexUrl)
                database.child("id").setValue(id)
                database.child("photoURL").setValue(this.arguments?.getString("imageUrl"))
                database.child("description").setValue(this.arguments?.getString("excerpt"))

                bookmarked = true
            } else {
                val receiver = ComponentName(context!!, AlarmBroadcastReceiver::class.java)

                this.context!!.packageManager.setComponentEnabledSetting(
                        receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                )

                bookmark.backgroundTintList = getColorStateList(context!!, R.color.colorAccent)
                bookmark.text = "Bookmark"
                bookmark.setTextColor(resources.getColor(R.color.colorPrimaryDark))

                //remove bookmark from firebase
                FirebaseDatabase.getInstance().reference.child("users").child(user!!.uid).child("bookmarked").child(indexUrl).removeValue()

                //cancel bookmark notification
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

        val user = auth.currentUser

        val viewModel = ViewModelProviders.of(this).get(WhatsOnViewModel::class.java)
        viewModel.getShowBookmarked(user!!, indexUrl).observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean?) {
                if (t == true) {
                    bookmark.backgroundTintList = getColorStateList(context!!, R.color.colorPrimaryDark)
                    bookmark.text = "Event Bookmarked"
                    bookmark.setTextColor(resources.getColor(R.color.colorAccent))

                    bookmarked = true
                } else {

                    bookmark.backgroundTintList = getColorStateList(context!!, R.color.colorAccent)
                    bookmark.text = "Bookmark"
                    bookmark.setTextColor(resources.getColor(R.color.colorPrimaryDark))

                    bookmarked = false
                }

            }
        })
    }

}