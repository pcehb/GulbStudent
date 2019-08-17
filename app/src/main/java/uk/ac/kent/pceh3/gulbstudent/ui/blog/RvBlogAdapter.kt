package uk.ac.kent.pceh3.gulbstudent.ui

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_blog_article.view.*
import uk.ac.kent.pceh3.gulbstudent.MainActivity
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.ViewPagerAdapter
import uk.ac.kent.pceh3.gulbstudent.model.Blog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


/**
 * Created by pceh3 on 14/07/2019.
 */
class RvBlogAdapter(val blogList: List<Blog>?) : RecyclerView.Adapter<RvBlogAdapter.ViewHolder>(), View.OnClickListener {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.adapter_blog_item_layout, p0, false)

        v.setOnClickListener(this)
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

    override fun onClick(itemView: View) {
        val article = itemView.findViewById<TextView>(R.id.article)
        val date = itemView.findViewById<TextView>(R.id.date)
        val title = itemView.findViewById<TextView>(R.id.title)


        val activity = itemView.getContext() as AppCompatActivity

        activity.viewPager.visibility = View.GONE
        activity.tab_layout.visibility = View.GONE
        activity.content.visibility = View.VISIBLE

        val newFragment = BlogArticleFragment()
        val args = Bundle()
        args.putCharSequence("title", title.text)
        args.putCharSequence("date", date.text)
        args.putCharSequence("article", article.text)
        newFragment.arguments = args

        activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.content, newFragment)
                .addToBackStack(null)
                .commit()
    }
}