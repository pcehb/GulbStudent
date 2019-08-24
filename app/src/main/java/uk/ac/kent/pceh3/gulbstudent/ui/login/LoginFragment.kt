package uk.ac.kent.pceh3.gulbstudent.ui.login

import android.app.ActivityOptions
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.WhatsOnFragment
import uk.ac.kent.pceh3.gulbstudent.ui.DetailActivity


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        setHasOptionsMenu(true)
        auth = FirebaseAuth.getInstance()

        activity!!.toolBar.navigationIcon = null

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginButton.setOnClickListener{
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            Log.d(TAG, "signIn:$email")

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this.requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")

                            activity!!.viewPager.currentItem = 0
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

            val activity = context as AppCompatActivity

            activity.let{
                val intent = Intent (it, DetailActivity::class.java)
                intent.putExtra("openingFragment", "create")

                val options = ActivityOptions.makeSceneTransitionAnimation(activity)
                it.startActivity(intent, options.toBundle())
            }
        }

        forgotPasswordButton.setOnClickListener{
            val activity = context as AppCompatActivity

            activity.let{
                val intent = Intent (it, DetailActivity::class.java)
                intent.putExtra("openingFragment", "reset")

                val options = ActivityOptions.makeSceneTransitionAnimation(activity)
                it.startActivity(intent, options.toBundle())
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
        super.onPrepareOptionsMenu(menu)
    }

}