package uk.ac.kent.pceh3.gulbstudent.ui.login

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
import kotlinx.android.synthetic.main.fragment_login.*
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.WhatsOnFragment


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        setHasOptionsMenu(true)
        auth = FirebaseAuth.getInstance()

        activity!!.toolBar.setNavigationIcon(null)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginButton.setOnClickListener{
            var email = emailEditText.text.toString()
            var password = passwordEditText.text.toString()
            Log.d(TAG, "signIn:$email")

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this.requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
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


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(context,task.exception!!.message, Toast.LENGTH_SHORT).show()
                        }

                    }

        }

        createAccountButton.setOnClickListener{
            activity!!.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content, CreateAccountFragment())
                    .commit()
        }

        forgotPasswordButton.setOnClickListener{
            activity!!.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content, ResetPasswordFragment())
                    .commit()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        super.onPrepareOptionsMenu(menu)
    }

}