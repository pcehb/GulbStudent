package uk.ac.kent.pceh3.gulbstudent.ui.profile

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_profile_layout.view.*
import uk.ac.kent.pceh3.gulbstudent.MainActivity
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.model.Bookmarks
import uk.ac.kent.pceh3.gulbstudent.network.AlarmBroadcastReceiver

// profile RV adapter
class ProfileAdapter (var BookmarkedList: List<Bookmarks>?) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>(), View.OnClickListener {
    private lateinit var auth: FirebaseAuth

    override fun onClick(v: View?) {
        val activity =  v!!.context as? MainActivity
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val title = v.title?.text.toString()
        val indexUrl = v.title?.tag.toString()
        val id = v.date?.tag.toString().toInt()

        val builder = AlertDialog.Builder(activity)

        builder.setTitle("Remove notification")

        // Display a message on alert dialog
        builder.setMessage("Are you want to remove $title from your bookmarks?")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("YES"){ _, _ ->
            val alarmManager = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val receiver = ComponentName(activity, AlarmBroadcastReceiver::class.java)

            activity.packageManager.setComponentEnabledSetting(
                    receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
            )

            val bookmarkIntent = PendingIntent.getBroadcast(
                    activity,
                    id,
                    Intent(activity, AlarmBroadcastReceiver::class.java).apply {
                        putExtra("notificationId", id)
                        putExtra("title", title)
                        putExtra("url", indexUrl)
                    },
                    PendingIntent.FLAG_CANCEL_CURRENT
            )
            alarmManager.cancel(bookmarkIntent)

            //delete bookmark from firebase
            FirebaseDatabase.getInstance().reference.child("users").child(user!!.uid).child("bookmarked").child(indexUrl).removeValue()
        }
        // Display a neutral button on alert dialog
        builder.setNeutralButton("Cancel"){_,_ ->
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun updateData(bookmarks: List<Bookmarks>) {
        this.BookmarkedList = bookmarks
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.adapter_profile_layout, p0, false)
        v.setOnClickListener(this)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return BookmarkedList!!.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val date = ""+ BookmarkedList!![p1].date + "-" +BookmarkedList!![p1].month + "-" + BookmarkedList!![p1].year

        p0.title?.text = BookmarkedList!![p1].title
        p0.title?.tag = BookmarkedList!![p1].index
        p0.date?.tag = BookmarkedList!![p1].id
        p0.date?.text = date
        p0.description?.text = BookmarkedList!![p1].description

        Picasso.get()
                .load(BookmarkedList!![p1].photoURL)
                .placeholder(R.drawable.logo)
                .into(p0.photo)


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val date = itemView.findViewById<TextView>(R.id.date)
        val description = itemView.findViewById<TextView>(R.id.description)
        val photo = itemView.findViewById<ImageView>(R.id.articleImage)
    }

}