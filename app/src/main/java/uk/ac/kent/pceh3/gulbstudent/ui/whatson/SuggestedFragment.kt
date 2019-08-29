package uk.ac.kent.pceh3.gulbstudent.ui.whatson

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.*
import android.view.View.*
import kotlinx.android.synthetic.main.fragment_whats_on.*
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.model.WhatsOn
import uk.ac.kent.pceh3.gulbstudent.ui.WhatsOnViewModel

// suggested fragment (category notification page)
class SuggestedFragment : Fragment() {

    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_whats_on, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = LinearLayoutManager(this.context)
        val bundle = this.arguments
        val categorySearch = bundle!!.getString("categorySearch")
        val startDate = bundle.getString("startDate")
        val endDate = bundle.getString("endDate")

        loadFeed("", categorySearch!!, startDate!!, endDate!!)
    }

    //load feed with the search queries
    private fun loadFeed(search: String, eventType: String, startDate: String, endDate: String) {
        progressBar.visibility = VISIBLE
        val viewModel = ViewModelProviders.of(this).get(WhatsOnViewModel::class.java)
        viewModel.getWhatsOn(search, eventType, startDate, endDate).observe(this, Observer<List<WhatsOn>> { t ->
            if (t != null) {
                //show data result in RV
                recyclerViewWO.layoutManager = layoutManager
                val rvWhatsOnAdapter = RvWhatsOnAdapter(t)
                recyclerViewWO.adapter = rvWhatsOnAdapter
                rvWhatsOnAdapter.updateData(t)
                progressBar.visibility = GONE
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        super.onPrepareOptionsMenu(menu)
    }
}