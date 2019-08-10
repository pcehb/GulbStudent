package uk.ac.kent.pceh3.gulbstudent.ui.login

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_reset_password.*
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.WhatsOnFragment

class ResetPasswordFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_reset_password, container, false)
        setHasOptionsMenu(true)
        auth = FirebaseAuth.getInstance()
        return view
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        super.onPrepareOptionsMenu(menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resetButton.setOnClickListener{
            var email = emailEditText.text.toString()
            Log.d(TAG, "signIn:$email")

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Email set
                            Log.d(TAG, "Email sent.")
                            Toast.makeText(context,"Password reset email sent.", Toast.LENGTH_SHORT).show()


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(context,task.exception!!.message, Toast.LENGTH_SHORT).show()
                        }

                    }
        }

    }
}