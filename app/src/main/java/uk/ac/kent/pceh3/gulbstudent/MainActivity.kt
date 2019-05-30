package uk.ac.kent.pceh3.gulbstudent

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    internal lateinit var viewpageradapter: ViewPagerAdapter //Declare PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolBar)

        toolBar.setTitle(R.string.app_name)
        toolBar.setTitleTextColor(getColor(R.color.colorAccent))




        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        viewpageradapter= ViewPagerAdapter(supportFragmentManager)

        this.viewPager.adapter=viewpageradapter  //Binding PagerAdapter with ViewPager
        this.tab_layout.setupWithViewPager(this.viewPager) //Binding ViewPager with TabLayout

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_whatson -> {
                this.viewPager.setCurrentItem(0)
                this.content.visibility = View.GONE
                this.viewPager.visibility = View.VISIBLE
                this.tab_layout.visibility = View.VISIBLE
            }
            R.id.nav_deals -> {
                this.viewPager.setCurrentItem(1)
                this.content.visibility = View.GONE
                this.viewPager.visibility = View.VISIBLE
                this.tab_layout.visibility = View.VISIBLE
            }
            R.id.nav_blog -> {
                this.viewPager.setCurrentItem(2)
                this.content.visibility = View.GONE
                this.viewPager.visibility = View.VISIBLE
                this.tab_layout.visibility = View.VISIBLE
            }
            else -> {
                this.viewPager.visibility = View.GONE
                this.tab_layout.visibility = View.GONE
                this.content.visibility = View.VISIBLE
            }
        }

        var fragment: Fragment = when (item.itemId) {
            R.id.nav_whatson -> {
                WhatsOnFragment()
            }
            R.id.nav_deals -> {
                DealsFragment()
            }
            R.id.nav_blog -> {
                BlogFragment()
            }
            R.id.nav_settings -> {
                ProfileFragment()
            }
            R.id.nav_gulb -> {
                AboutFragment()
            }
            R.id.nav_gulbcard -> {
                AboutFragment()
            }
            R.id.nav_gulbuncovered -> {
                AboutFragment()
            }
            R.id.nav_gulbstudent -> {
                AboutFragment()
            }
            else -> {
                ProfileFragment()
            }
        }

        var fragmentManager = supportFragmentManager
        fragmentManager
                .beginTransaction()
                .replace(R.id.content, fragment)
                .commit()
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}