package uk.ac.kent.pceh3.gulbstudent.ui.comp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_competition.*
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.model.Comp
import java.time.LocalDate
import java.time.format.DateTimeFormatter


// competition fragment
class CompetitionFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var gulbCard : Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_competition, container, false)

        auth = FirebaseAuth.getInstance()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // observe the current competition from view model
        val viewModel = ViewModelProviders.of(this).get(CompViewModel::class.java)
        viewModel.getComp().observe(this, Observer<Comp> { t ->
            description?.text = t.description
            date?.text = getString(R.string.closedate) + " " + t.closeDate
            title?.text = t.title
            Picasso.get()
                    .load(t.photoURL)
                    .placeholder(R.drawable.logo)
                    .into(image)

            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val date = LocalDate.parse(t.closeDate, formatter)

            // if closing date is after current date
            if (LocalDate.now().isAfter(date)){
                enter.text = getString(R.string.compclosed)
                enter.isEnabled = false
                enter.backgroundTintList = getColorStateList(context!!, R.color.colorPrimaryDark)
                enter.setTextColor(resources.getColor(R.color.colorAccent))
            }
        })

        // check if user has already entered
        viewModel.getEntries().observe(this, Observer<List<String>> { t ->

            for (entry in t){
                if (entry == auth.currentUser!!.email) {
                    enter.text = getString(R.string.entered)
                    enter.isEnabled = false
                    enter.backgroundTintList = getColorStateList(context!!, R.color.colorPrimaryDark)
                    enter.setTextColor(resources.getColor(R.color.colorAccent))
                }
            }
        })


        enter.setOnClickListener {
            val database = FirebaseDatabase.getInstance().reference.child("competition").child("entries")

            database.push().setValue(auth.currentUser!!.email)

            viewModel.getGulbCard(auth.currentUser!!).observe(this, Observer<Boolean> { t ->
                gulbCard = t == true
                if (gulbCard){
                    //enter twice if gulbcard member
                    database.push().setValue(auth.currentUser!!.email)
                }
            })


        }
    }
}
