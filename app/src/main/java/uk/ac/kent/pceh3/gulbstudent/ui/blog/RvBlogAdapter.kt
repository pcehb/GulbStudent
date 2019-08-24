package uk.ac.kent.pceh3.gulbstudent.ui

import android.app.ActivityOptions
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.model.Blog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.squareup.picasso.Picasso
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
        Picasso.get()
                .load(blogList[p1].photoURL)
                .placeholder(R.drawable.logo)
                .into(p0.photo)
        p0.photo.tag = blogList[p1].photoURL

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val article = itemView.findViewById<TextView>(R.id.description)
        val date = itemView.findViewById<TextView>(R.id.date)
        val title = itemView.findViewById<TextView>(R.id.title)
        val photo = itemView.findViewById<ImageView>(R.id.articleImage)
    }

    override fun onClick(itemView: View) {
        val article = itemView.findViewById<TextView>(R.id.description)
        val date = itemView.findViewById<TextView>(R.id.date)
        val title = itemView.findViewById<TextView>(R.id.title)
        val photo = itemView.findViewById<ImageView>(R.id.articleImage)

        val activity = itemView.getContext() as AppCompatActivity

        activity?.let{
            val intent = Intent (it, DetailActivity::class.java)
            intent.putExtra("openingFragment", "article")
            intent.putExtra("title", title.text)
            intent.putExtra("date", date.text)
            intent.putExtra("article", article.text)
            intent.putExtra("photoURL", photo.tag.toString())
            val options = ActivityOptions.makeSceneTransitionAnimation(activity)
            it.startActivity(intent, options.toBundle())
        }
    }
}