package uk.ac.kent.pceh3.gulbstudent

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import uk.ac.kent.pceh3.gulbstudent.model.Deal
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_deals.*
import uk.ac.kent.pceh3.gulbstudent.ui.DealsViewModel
import uk.ac.kent.pceh3.gulbstudent.ui.RvAdapter

// deals fragment
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
        // observe deals data
        val viewModel = ViewModelProviders.of(this).get(DealsViewModel::class.java)
        viewModel.getDeals().observe(this, Observer<List<Deal>> { t ->
            // parse data to adapter
            val rvAdapter = RvAdapter(t)
            recyclerView.adapter = rvAdapter
        })

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        super.onPrepareOptionsMenu(menu)
    }
}