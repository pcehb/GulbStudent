package uk.ac.kent.pceh3.gulbstudent.ui.whatson

import android.app.*
import android.content.ComponentName
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getColorStateList
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_event.*

import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.model.WhatsOn
import uk.ac.kent.pceh3.gulbstudent.network.AlarmBroadcastReceiver
import java.lang.Double.parseDouble
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

class EventFragment : Fragment() {
    private var isFABOpen: Boolean? = false
    private var day = 1
    private var month = 1
    private var year = 2019

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)
        setHasOptionsMenu(true)
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

        if (event.bookmarked == true) {
            bookmark.backgroundTintList = getColorStateList(context!!, R.color.colorPrimaryDark)
            bookmark.text = "Event Bookmarked"
            bookmark.setTextColor(resources.getColor(R.color.colorAccent))
        }


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

            val bookmarkIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    Intent(activity, AlarmBroadcastReceiver::class.java).apply {
                        putExtra("notificationId", event.index)
                        putExtra("title", event.title)
                    },
                    PendingIntent.FLAG_CANCEL_CURRENT
            )
            if (event.bookmarked == false) {

                val receiver = ComponentName(context, AlarmBroadcastReceiver::class.java)

                this.context!!.packageManager.setComponentEnabledSetting(
                        receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP
                )

                val dateFormatted = event.date
                val re = Regex("[^A-Za-z0-9 ]")
                var splitDate = re.replace(dateFormatted.toString(), "")
                var splitDate2 = splitDate.split("\\s".toRegex())

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
                            set(HOUR_OF_DAY, 17)
                            set(MINUTE, 12)
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

                event.bookmarked = true
            }
            else{
                val receiver = ComponentName(context, AlarmBroadcastReceiver::class.java)

                this.context!!.packageManager.setComponentEnabledSetting(
                        receiver,
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP
                )

                bookmark.backgroundTintList = getColorStateList(context!!, R.color.colorAccent)
                bookmark.text = "Bookmark"
                bookmark.setTextColor(resources.getColor(R.color.colorPrimaryDark))

                alarmManager.cancel(bookmarkIntent)
                event.bookmarked = false
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


}