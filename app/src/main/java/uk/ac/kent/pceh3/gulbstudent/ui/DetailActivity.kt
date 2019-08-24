package uk.ac.kent.pceh3.gulbstudent.ui

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.animation.DecelerateInterpolator
import kotlinx.android.synthetic.main.activity_detail.*
import uk.ac.kent.pceh3.gulbstudent.R
import uk.ac.kent.pceh3.gulbstudent.ui.login.CreateAccountFragment
import uk.ac.kent.pceh3.gulbstudent.ui.login.ResetPasswordFragment
import uk.ac.kent.pceh3.gulbstudent.ui.whatson.EventFragment
import uk.ac.kent.pceh3.gulbstudent.ui.whatson.SuggestedFragment

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAnimation()
        setContentView(R.layout.activity_detail)


        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_name)
        toolbar.setTitleTextColor(getColor(R.color.colorAccent))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.navigationIcon?.setColorFilter(resources.getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP)


        val extras = intent.getStringExtra("openingFragment")
        if (extras != null && extras == "article") {
            println("BLOG ARTICLE")
            val newFragment = BlogArticleFragment()
        val args = Bundle()
        args.putCharSequence("title", intent.getStringExtra("title"))
        args.putCharSequence("date", intent.getStringExtra("date"))
        args.putCharSequence("article", intent.getStringExtra("article"))
        args.putCharSequence("photoURL", intent.getStringExtra("photoURL"))
        newFragment.arguments = args

            val fragmentManager = supportFragmentManager
            fragmentManager
                .beginTransaction()
                .replace(R.id.content, newFragment)
                .commit()

        }
        else if (extras != null && extras == "showEvent") {
            println("EVENT")
            val newFragment = EventFragment()
            val args = Bundle()

            args.putCharSequence("title", intent.getStringExtra("title"))
            args.putCharSequence("excerpt", intent.getStringExtra("excerpt"))
            args.putCharSequence("date", intent.getStringExtra("date"))
            args.putCharSequence("label", intent.getStringExtra("label"))
            args.putCharSequence("imageUrl", intent.getStringExtra("imageUrl"))
            args.putCharSequence("url", intent.getStringExtra("url"))
            args.putCharSequence("bookLink", intent.getStringExtra("bookLink"))
            args.putInt("index", intent.getIntExtra("index", 1))

            newFragment.arguments = args

            val fragmentManager = supportFragmentManager
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content, newFragment)
                    .commit()

        }
        else if (extras != null&&extras.equals("suggested")) {
            println("SUGGESTED")

            val fragment = SuggestedFragment()
            val bundle = Bundle()
            bundle.putString("categorySearch", intent.getStringExtra("categorySearch"))
            bundle.putString("startDate", intent.getStringExtra("startDate"))
            bundle.putString("endDate", intent.getStringExtra("endDate"))
            fragment.arguments = bundle

            val fragmentManager = supportFragmentManager
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content, fragment)
                    .commit()
        }
        else if (extras != null && extras == "create") {
            println("CREATE")
            val newFragment = CreateAccountFragment()
            val fragmentManager = supportFragmentManager
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content, newFragment)
                    .commit()

        }
        else if (extras != null && extras == "reset") {
            println("RESET")
            val newFragment = ResetPasswordFragment()

            val fragmentManager = supportFragmentManager
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content, newFragment)
                    .commit()

        }

    }

    private fun setAnimation() {
        val slide = Slide()
        slide.slideEdge = Gravity.END
        slide.duration = 300
        slide.interpolator = DecelerateInterpolator()
        window.exitTransition = slide
        window.enterTransition = slide
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
