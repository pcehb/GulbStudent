package uk.ac.kent.pceh3.gulbstudent.ui.login

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_account.*
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.WhatsOnFragment


class CreateAccountFragment: Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_create_account, container, false)
        setHasOptionsMenu(true)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        return view
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        super.onPrepareOptionsMenu(menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createAccountButton.setOnClickListener{
            var email = emailEditText.text.toString()
            var password = passwordEditText.text.toString()
            Log.d(ContentValues.TAG, "signIn:$email")

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this.requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser

                            activity!!.viewPager.setCurrentItem(0)
                            activity!!.content.visibility = View.GONE
                            activity!!.viewPager.visibility = View.VISIBLE
                            activity!!.tab_layout.visibility = View.VISIBLE

                            activity!!.toolBar.setNavigationIcon(R.drawable.icons8_menu_24)


                            activity!!.supportFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.content, WhatsOnFragment())
                                    .commit()

                            database = database.child("users").child(user!!.uid)
                            database.child("categories").child("archive").setValue(false)
                            database.child("categories").child("audioDescribed").setValue(false)
                            database.child("categories").child("boing").setValue(false)
                            database.child("categories").child("cafe").setValue(false)
                            database.child("categories").child("captionedSubtitles").setValue(false)
                            database.child("categories").child("comedy").setValue(false)
                            database.child("categories").child("family").setValue(false)
                            database.child("categories").child("festival").setValue(false)
                            database.child("categories").child("foreign").setValue(false)
                            database.child("categories").child("music").setValue(false)
                            database.child("categories").child("live").setValue(false)
                            database.child("categories").child("relaxed").setValue(false)
                            database.child("categories").child("talks").setValue(false)
                            database.child("categories").child("theatreDance").setValue(false)
                            database.child("categories").child("workshops").setValue(false)
                            database.child("gulbCard").setValue(false)


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(context, task.exception!!.message, Toast.LENGTH_SHORT).show()
                        }

                    }
        }

    }
}