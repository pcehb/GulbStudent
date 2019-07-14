package uk.ac.kent.pceh3.gulbstudent.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.model.Blog

/**
 * Created by pceh3 on 14/07/2019.
 */
class RvBlogAdapter(val blogList: List<Blog>?) : RecyclerView.Adapter<RvBlogAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.adapter_blog_item_layout, p0, false)
        return ViewHolder(v)
    }
    override fun getItemCount(): Int {
        return blogList!!.size
    }
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        p0.article?.text = blogList!![p1].article
        p0.date?.text = blogList[p1].date
        p0.title?.text = blogList[p1].title

    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val article = itemView.findViewById<TextView>(R.id.article)
        val date = itemView.findViewById<TextView>(R.id.date)
        val title = itemView.findViewById<TextView>(R.id.title)

    }
}