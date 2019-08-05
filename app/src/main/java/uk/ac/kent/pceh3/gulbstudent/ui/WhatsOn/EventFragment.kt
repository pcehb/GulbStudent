package uk.ac.kent.pceh3.gulbstudent.ui.WhatsOn

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_event.*

import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.model.WhatsOn

class EventFragment : Fragment() {
    private var isFABOpen: Boolean? = false

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

        fabPlus.setOnClickListener {
            if (!isFABOpen!!) {
                showFABMenu()
            } else {
                closeFABMenu()
            }
        }

        fabBuy.setOnClickListener{
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(event.bookLink)
            startActivity(openURL)
        }

        fabShare.setOnClickListener{
            val openURL = Intent(Intent.ACTION_SEND)
            openURL.putExtra(Intent.EXTRA_TEXT, event.url)
            openURL.type = "text/plain"
            startActivity(Intent.createChooser(openURL, "Sharing Option"))
        }

        fabWeb.setOnClickListener{
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(event.url)
            startActivity(openURL)
        }
    }

    private fun showFABMenu() {
        isFABOpen = true
        fabBuy.animate().translationY(-150f)
        fabShare.animate().translationY(-275f)
        fabWeb.animate().translationY(-400f)
        ViewCompat.animate(fabPlus)
                .rotation(45.0f)
                .withLayer()
                .setDuration(300L)
                .start()
    }

    private fun closeFABMenu() {
        isFABOpen = false
        fabBuy.animate().translationY(0f)
        fabShare.animate().translationY(0f)
        fabWeb.animate().translationY(0f)
        ViewCompat.animate(fabPlus)
                .rotation(0.0f)
                .withLayer()
                .setDuration(300L)
                .start()

    }
}