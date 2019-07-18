package uk.ac.kent.pceh3.gulbstudent.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat.getColor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import kotlinx.android.synthetic.main.fragment_blog_article.*
import uk.ac.kent.pceh3.gulbstudent.R




/**
 * Created by pceh3 on 14/07/2019.
 */

class BlogArticleFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_blog_article, container, false)


        return view
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        article?.text = this.arguments?.getString("article")
        date?.text = this.arguments?.getString("date")
        title?.text = this.arguments?.getString("title")


    }

}