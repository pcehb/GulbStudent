package uk.ac.kent.pceh3.gulbstudent.ui.WhatsOn

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.model.WhatsOn
import com.squareup.picasso.Picasso


/**
 * Created by pceh3 on 14/07/2019.
 */
class RvWhatsOnAdapter(var WhatsOnList: List<WhatsOn>?) : RecyclerView.Adapter<RvWhatsOnAdapter.ViewHolder>(), View.OnClickListener {

    fun updateData(newsFeed: List<WhatsOn>) {
        this.WhatsOnList = newsFeed
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.adapter_whats_on_item_layout, p0, false)
        v.setOnClickListener(this)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return WhatsOnList!!.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.article?.text = WhatsOnList!![p1].excerpt
        p0.date?.text = WhatsOnList!![p1].date
        p0.title?.text = WhatsOnList!![p1].title

        p0.title?.tag = WhatsOnList!![p1].index

        Picasso.get()
                .load(WhatsOnList!![p1].imageUrl)
                .placeholder(R.drawable.logo)
                .into(p0.image)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val article = itemView.findViewById<TextView>(R.id.article)
        val date = itemView.findViewById<TextView>(R.id.date)
        val title = itemView.findViewById<TextView>(R.id.title)
        val image = itemView.findViewById<ImageView>(R.id.myImageView)
    }

    override fun onClick(itemView: View) {

        val pos = itemView.findViewById<TextView>(R.id.title).tag.toString().toInt()


        val activity = itemView.getContext() as AppCompatActivity

        activity.viewPager.visibility = View.GONE
        activity.tab_layout.visibility = View.GONE
        activity.content.visibility = View.VISIBLE

        val newFragment = EventFragment()
        val args = Bundle()
        args.putCharSequence("title", WhatsOnList!![pos].title)
        args.putParcelable("event", WhatsOnList!![pos])

        newFragment.arguments = args

        activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.content, newFragment)
                .addToBackStack(null)
                .commit()
    }

}