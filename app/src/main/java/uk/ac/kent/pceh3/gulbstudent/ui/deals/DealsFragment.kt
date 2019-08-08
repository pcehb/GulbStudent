package uk.ac.kent.pceh3.gulbstudent

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.arch.lifecycle.Observer
import uk.ac.kent.pceh3.gulbstudent.model.Deal
import android.arch.lifecycle.ViewModelProviders
import android.content.ContentValues
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import kotlinx.android.synthetic.main.fragment_deals.*
import uk.ac.kent.pceh3.gulbstudent.ui.DealsViewModel
import uk.ac.kent.pceh3.gulbstudent.ui.RvAdapter


class DealsFragment : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_deals, container, false)
        setHasOptionsMenu(true)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager = LinearLayoutManager(this.context)

        recyclerView.layoutManager = linearLayoutManager


        val viewModel = ViewModelProviders.of(this).get(DealsViewModel::class.java)
        viewModel.getDeals().observe(this, object : Observer<List<Deal>> {
            override fun onChanged(t: List<Deal>?) {
                val data = t
                val rvAdapter = RvAdapter(data)
                recyclerView.adapter = rvAdapter
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        super.onPrepareOptionsMenu(menu)
    }
}