package uk.ac.kent.pceh3.gulbstudent.ui.WhatsOn

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_event.*

import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.model.WhatsOn

class EventFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)

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

        book.setOnClickListener{
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(event.bookLink)
            startActivity(openURL)
        }
    }
}
