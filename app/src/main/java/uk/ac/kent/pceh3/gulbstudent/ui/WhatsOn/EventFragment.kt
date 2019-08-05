package uk.ac.kent.pceh3.gulbstudent.ui.WhatsOn

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_event.*
import uk.ac.kent.pceh3.gulbstudent.MainActivity

import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.model.WhatsOn

class EventFragment : Fragment() {
    private var isFABOpen: Boolean? = false
    private var notificationManager: NotificationManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)


        notificationManager =
                activity!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel(
                "uk.ac.kent.pceh3.gulbstudent",
                "GulbStudent",
                "Show notifications")

        return view
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

//            val openURL = Intent(Intent.ACTION_VIEW)
//            openURL.data = Uri.parse(event.bookLink)
//            startActivity(openURL)
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

//
//            val openURL = Intent(Intent.ACTION_VIEW)
//            openURL.data = Uri.parse(event.url)
//            startActivity(openURL)
        }

        bookmark.setOnClickListener{
            val notificationID = event.index.toString().toInt()
            val channelID = "uk.ac.kent.pceh3.gulbstudent"

            val resultIntent = Intent(activity, MainActivity::class.java)

            val pendingIntent = PendingIntent.getActivity(
                    this.context,
                    0,
                    resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notification = Notification.Builder(context,
                    channelID)
                    .setContentTitle("GulbStudent")
                    .setContentText("'"+ event.title + "' is happening tonight.")
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentIntent(pendingIntent)
                    .setChannelId(channelID)
                    .build()

            notificationManager?.notify(notificationID, notification)


            // OPEN SHOW EVENT PAGE ON CLICK
            // NOTIFY 9AM ON SHOW DAY

        }
    }

    private fun createNotificationChannel(id: String, name: String,
                                          description: String) {

        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(id, name, importance)

        channel.description = description
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        channel.vibrationPattern =
                longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        notificationManager?.createNotificationChannel(channel)
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