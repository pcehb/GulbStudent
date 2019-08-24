package uk.ac.kent.pceh3.gulbstudent

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.Toolbar
import android.view.*
import android.view.View.*
import android.widget.*
import kotlinx.android.synthetic.main.fragment_whats_on.*
import uk.ac.kent.pceh3.gulbstudent.model.WhatsOn
import uk.ac.kent.pceh3.gulbstudent.ui.whatson.RvWhatsOnAdapter
import uk.ac.kent.pceh3.gulbstudent.ui.WhatsOnViewModel


class WhatsOnFragment : Fragment() {

    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_whats_on, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = LinearLayoutManager(this.context)

        loadFeed("", "&event_type=", "")
    }

    private fun loadFeed(search:String, eventType: String, date: String){
        progressBar.visibility = VISIBLE
        val viewModel = ViewModelProviders.of(this).get(WhatsOnViewModel::class.java)
        viewModel.getWhatsOn(search, eventType, date, "").observe(this, Observer<List<WhatsOn>> { data ->
                recyclerViewWO.layoutManager = layoutManager
                val rvWhatsOnAdapter = RvWhatsOnAdapter(data)
                recyclerViewWO.adapter = rvWhatsOnAdapter
                rvWhatsOnAdapter.updateData(data)
                progressBar.visibility = GONE
                noSearchResults.visibility = GONE
           if(data.isEmpty()){
                noSearchResults.visibility = VISIBLE}

        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            menu.clear()
            inflater.inflate(R.menu.menu, menu)

            // Get the SearchView and set the searchable configuration
            val showPopupBtn = menu.findItem(R.id.app_bar_search)

            showPopupBtn.setOnMenuItemClickListener {

                //instantiate the popup.xml layout file
                val layoutInflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val customView = layoutInflater.inflate(R.layout.search_popup, null)

                val closePopupBtn = customView.findViewById(R.id.closePopupBtn) as Button

                val archive = customView.findViewById(R.id.archive) as CheckBox
                val audioDescribed = customView.findViewById(R.id.audioDescribed) as CheckBox
                val boing = customView.findViewById(R.id.boing) as CheckBox
                val cafe = customView.findViewById(R.id.cafe) as CheckBox
                val captionedSubtitles = customView.findViewById(R.id.captionedSubtitles) as CheckBox
                val comedy = customView.findViewById(R.id.comedy) as CheckBox
                val family = customView.findViewById(R.id.family) as CheckBox
                val festival = customView.findViewById(R.id.festival) as CheckBox
                val foreign = customView.findViewById(R.id.foreign) as CheckBox
                val music = customView.findViewById(R.id.music) as CheckBox
                val live = customView.findViewById(R.id.live) as CheckBox
                val relaxed = customView.findViewById(R.id.relaxed) as CheckBox
                val talks = customView.findViewById(R.id.talks) as CheckBox
                val theatreDance = customView.findViewById(R.id.theatreDance) as CheckBox
                val workshops = customView.findViewById(R.id.workshops) as CheckBox

                //instantiate popup window
                val popupWindow = PopupWindow(customView, Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT)

                //display the popup window
                popupWindow.showAtLocation(relativeLayout, Gravity.CENTER, 0, 0)

                popupWindow.setFocusable(true)
                popupWindow.update()

                var categorySearch = ""
                var date = ""

                val dateSwitch = customView.findViewById(R.id.dateSwitch) as Switch
                val calendarView = customView.findViewById(R.id.dateCal) as CalendarView
                calendarView.visibility = GONE

                dateSwitch.setOnCheckedChangeListener{ _, isChecked ->
                    if (isChecked) {
                        calendarView.visibility = VISIBLE
                    }
                    else{
                        calendarView.visibility = GONE
                        date=""
                    }
                }

                calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

                    val monthReceived = (month + 1)
                    var monthFormatted = monthReceived.toString()

                    if (monthReceived < 10){
                        monthFormatted = "0$monthReceived"
                    }

                    var dateFormatted = dayOfMonth.toString()

                    if (dayOfMonth < 10){
                        dateFormatted = "0$dayOfMonth"
                    }

                    date = ""+year+""+monthFormatted+""+dateFormatted
                }

                //close the popup window on button click
                closePopupBtn.setOnClickListener(View.OnClickListener {
                    val searchText = customView.findViewById(R.id.searchText) as EditText
                    var search = ""
                    search = searchText.text.toString()


                    if (archive.isChecked) {
                        categorySearch += "&event_type%5B%5D=archive"
                    }
                    if (audioDescribed.isChecked) {
                        categorySearch += "&event_type%5B%5D=audio-description"
                    }
                    if (boing.isChecked) {
                        categorySearch += "&event_type%5B%5D=boing-festival"
                    }
                    if (cafe.isChecked) {
                        categorySearch += "&event_type%5B%5D=cafe"
                    }
                    if (captionedSubtitles.isChecked) {
                        categorySearch += "&event_type%5B%5D=captioned-subtitles"
                    }
                    if (comedy.isChecked) {
                        categorySearch += "event_type%5B%5D=comedy"
                    }
                    if (family.isChecked) {
                        categorySearch += "&event_type%5B%5D=family"
                    }
                    if (festival.isChecked) {
                        categorySearch += "&event_type%5B%5D=festival"
                    }
                    if (foreign.isChecked) {
                        categorySearch += "&event_type%5B%5D=foreign-language-subtitles"
                    }
                    if (music.isChecked) {
                        categorySearch += "&event_type%5B%5D=music"
                    }
                    if (live.isChecked) {
                        categorySearch += "&event_type%5B%5D=recorded-live-screening"
                    }
                    if (relaxed.isChecked) {
                        categorySearch += "&event_type%5B%5D=relaxed"
                    }
                    if (talks.isChecked) {
                        categorySearch += "&event_type%5B%5D=talks"
                    }
                    if (theatreDance.isChecked) {
                        categorySearch += "&event_type%5B%5D=theathre-dance"
                    }
                    if (workshops.isChecked) {
                        categorySearch += "&event_type%5B%5D=workshops"
                    }

                    println(search)
                    println(categorySearch)

                    loadFeed(search, categorySearch, date)
                    popupWindow.dismiss()
                })

                true
            }
    }
}