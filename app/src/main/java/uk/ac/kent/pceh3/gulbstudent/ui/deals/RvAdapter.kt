package uk.ac.kent.pceh3.gulbstudent.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.model.Deal

// recycler view adapter for deals fragment
class RvAdapter(val dealList: List<Deal>?) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.adapter_item_layout, p0, false)
        return ViewHolder(v)
    }
    override fun getItemCount(): Int {
        return dealList!!.size
    }

    // display deal data in cardview
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.code?.text = dealList!![p1].code
        p0.description?.text = dealList[p1].description
        Picasso.get()
                .load(dealList[p1].photoURL)
                .placeholder(R.drawable.logo)
                .into(p0.photo)

        // when copy clicked, copy to clipboard
        p0.copy.setOnClickListener { view ->

            // check boolean for CLIPBOARD
            val sharedPref: SharedPreferences = view.context.getSharedPreferences("CLIPBOARD", 0)

            // if CLIPBOARD is true show popup instructions
            if (sharedPref.getBoolean("CLIPBOARD", true)) {
                val layoutInflater = view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val customView = layoutInflater.inflate(R.layout.deal_popup, null)

                val stopPopupBtn = customView.findViewById(R.id.stop) as Button
                val okayPopupBtn = customView.findViewById(R.id.okay) as Button

                //instantiate popup window
                val popupWindow = PopupWindow(customView, LinearLayout.LayoutParams.WRAP_CONTENT,  LinearLayout.LayoutParams.WRAP_CONTENT)

                //display the popup window
                popupWindow.showAtLocation(view.rootView, Gravity.CENTER, 0, 0)
                popupWindow.isFocusable = true
                popupWindow.update()

                // set CLIPBOARD to false
                stopPopupBtn.setOnClickListener{
                    val editor = sharedPref.edit()
                    editor.putBoolean("CLIPBOARD", false)
                    editor.apply()
                    popupWindow.dismiss()
                }

                okayPopupBtn.setOnClickListener{
                    popupWindow.dismiss()
                }
            }

            // copy code to clipboard
            val clipboard = view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", p0.code?.text)
            clipboard.primaryClip = clip

            Snackbar.make(p0.copy, R.string.clipboard, Snackbar.LENGTH_SHORT).show()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val code = itemView.findViewById<TextView>(R.id.code)
        val description = itemView.findViewById<TextView>(R.id.description)
        val copy = itemView.findViewById<ImageButton>(R.id.imageButton)
        val photo = itemView.findViewById<ImageView>(R.id.articleImage)
    }
}