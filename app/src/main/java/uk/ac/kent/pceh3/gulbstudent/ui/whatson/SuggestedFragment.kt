package uk.ac.kent.pceh3.gulbstudent.ui.whatson

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.view.View.*
import kotlinx.android.synthetic.main.fragment_whats_on.*
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.model.WhatsOn
import uk.ac.kent.pceh3.gulbstudent.ui.WhatsOnViewModel


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

        println("$categorySearch, $startDate, $endDate")

        loadFeed("", categorySearch!!, startDate!!, endDate!!)
    }

    fun loadFeed(search:String, eventType: String, startDate: String, endDate: String){
        progressBar.visibility = VISIBLE
        val viewModel = ViewModelProviders.of(this).get(WhatsOnViewModel::class.java)
        viewModel.getWhatsOn(search, eventType, startDate, endDate).observe(this, object : Observer<List<WhatsOn>> {
            override fun onChanged(t: List<WhatsOn>?) {
                val data = t
                if (data != null){
                    recyclerViewWO.layoutManager = layoutManager
                    val rvWhatsOnAdapter = RvWhatsOnAdapter(data)
                    recyclerViewWO.adapter = rvWhatsOnAdapter
                    rvWhatsOnAdapter.updateData(data)
                    progressBar.visibility = GONE
                }
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        super.onPrepareOptionsMenu(menu)
    }
}