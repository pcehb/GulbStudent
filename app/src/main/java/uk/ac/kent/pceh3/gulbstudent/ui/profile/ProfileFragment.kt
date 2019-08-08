package uk.ac.kent.pceh3.gulbstudent

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_whats_on.*
import uk.ac.kent.pceh3.gulbstudent.model.Bookmarks
import uk.ac.kent.pceh3.gulbstudent.model.WhatsOn
import uk.ac.kent.pceh3.gulbstudent.ui.WhatsOnViewModel
import uk.ac.kent.pceh3.gulbstudent.ui.profile.ProfileAdapter
import uk.ac.kent.pceh3.gulbstudent.ui.profile.ProfileViewModel
import uk.ac.kent.pceh3.gulbstudent.ui.whatson.RvWhatsOnAdapter


class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var database: DatabaseReference
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        user = auth.currentUser!!

        database = FirebaseDatabase.getInstance().reference.child("users").child(user!!.uid).child("categories")

        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveBtn.setOnClickListener {
            if (archive.isChecked) {
                database.child("archive").setValue("true")
            } else{
                database.child("archive").setValue("false")
            }
            if (audioDescribed.isChecked) {
                database.child("audioDescribed").setValue("true")
            } else{
                database.child("audioDescribed").setValue("false")
            }
            if (boing.isChecked) {
                database.child("boing").setValue("true")
            } else{
                database.child("boing").setValue("false")
            }
            if (cafe.isChecked) {
                database.child("cafe").setValue("true")
            } else{
                database.child("cafe").setValue("false")
            }
            if (captionedSubtitles.isChecked) {
                database.child("captionedSubtitles").setValue("true")
            } else{
                database.child("captionedSubtitles").setValue("false")
            }
            if (comedy.isChecked) {
                database.child("comedy").setValue("true")
            } else{
                database.child("comedy").setValue("false")
            }
            if (family.isChecked) {
                database.child("family").setValue("true")
            } else{
                database.child("family") .setValue("false")
            }
            if (festival.isChecked) {
                database.child("festival").setValue("true")
            } else{
                database.child("festival").setValue("false")
            }
            if (foreign.isChecked) {
                database.child("foreign").setValue("true")
            } else{
                database.child("foreign").setValue("false")
            }
            if (music.isChecked) {
                database.child("music").setValue("true")
            } else{
                database.child("music").setValue("false")
            }
            if (live.isChecked) {
                database.child("live").setValue("true")
            } else{
                database.child("live").setValue("false")
            }
            if (relaxed.isChecked) {
                database.child("relaxed").setValue("true")
            } else{
                database.child("relaxed").setValue("false")
            }
            if (talks.isChecked) {
                database.child("talks").setValue("true")
            } else{
                database.child("talks").setValue("false")
            }
            if (theatreDance.isChecked) {
                database.child("theatreDance").setValue("true")
            } else{
                database.child("theatreDance").setValue("false")
            }
            if (workshops.isChecked) {
                database.child("workshops").setValue("true")
            } else{
                database.child("workshops").setValue("false")
            }
        }


        loadBookmarks(user)
    }

    fun loadBookmarks(user: FirebaseUser){
        val viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getBookmarks(user).observe(this, object : Observer<List<Bookmarks>> {
            override fun onChanged(t: List<Bookmarks>?) {
                val data = t
                if (data != null){
                    linearLayoutManager = LinearLayoutManager(context)
                    showsRV.layoutManager = linearLayoutManager
                    val ProfileAdapter = ProfileAdapter(data)
                    showsRV.adapter = ProfileAdapter
                    ProfileAdapter.updateData(data)
                }
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        super.onPrepareOptionsMenu(menu)
    }


}