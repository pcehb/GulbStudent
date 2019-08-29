package uk.ac.kent.pceh3.gulbstudent

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_blog.*
import uk.ac.kent.pceh3.gulbstudent.model.Blog
import uk.ac.kent.pceh3.gulbstudent.ui.BlogViewModel
import uk.ac.kent.pceh3.gulbstudent.ui.RvBlogAdapter

// blog fragment
class BlogFragment : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_blog, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager = LinearLayoutManager(this.context)

        recyclerView.layoutManager = linearLayoutManager

        // observe blog data
        val viewModel = ViewModelProviders.of(this).get(BlogViewModel::class.java)
        viewModel.getBlog().observe(this, Observer<List<Blog>> { t ->
            // parse data to adapter
            val rvAdapter = RvBlogAdapter(t)
            recyclerView.adapter = rvAdapter
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        super.onPrepareOptionsMenu(menu)
    }

}