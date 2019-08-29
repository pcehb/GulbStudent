package uk.ac.kent.pceh3.gulbstudent.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_blog_article.*
import uk.ac.kent.pceh3.gulbstudent.R

// blog article fragment
class BlogArticleFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_blog_article, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        description?.text = this.arguments?.getString("article")
        date?.text = this.arguments?.getString("date")
        title?.text = this.arguments?.getString("title")

        Picasso.get()
                .load(this.arguments?.getString("photoURL"))
                .placeholder(R.drawable.logo)
                .into(articleImage)
    }

}