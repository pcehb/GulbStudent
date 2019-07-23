package uk.ac.kent.pceh3.gulbstudent.ui.WhatsOn

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_event.*

import uk.ac.kent.pceh3.gulbstudent.R

class EventFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        excerpt?.text = this.arguments?.getString("excerpt")
        date?.text = this.arguments?.getString("date")
        title?.text = this.arguments?.getString("title")
        label?.text = this.arguments?.getString("label")
        book?.text = this.arguments?.getString("bookLink")
        Picasso.get()
                .load(this.arguments?.getString("image"))
                .placeholder(R.drawable.logo)
                .into(image)



    }
}
