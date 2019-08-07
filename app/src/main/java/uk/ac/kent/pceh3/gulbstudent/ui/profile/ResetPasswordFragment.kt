package uk.ac.kent.pceh3.gulbstudent.ui.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uk.ac.kent.pceh3.gulbstudent.R

class ResetPasswordFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_reset_password, container, false)

        return view
    }
}