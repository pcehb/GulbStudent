package uk.ac.kent.pceh3.gulbstudent

import android.Manifest
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.*
import androidx.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import com.google.android.gms.location.GeofencingClient
import android.os.Bundle
import android.provider.Settings
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import uk.ac.kent.pceh3.gulbstudent.ui.MainActivityViewModel
import uk.ac.kent.pceh3.gulbstudent.ui.login.LoginFragment
import android.view.View
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.material.tabs.TabLayout
import uk.ac.kent.pceh3.gulbstudent.model.Bookmarks
import uk.ac.kent.pceh3.gulbstudent.network.*
import uk.ac.kent.pceh3.gulbstudent.ui.comp.CompetitionFragment
import uk.ac.kent.pceh3.gulbstudent.ui.profile.ProfileViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var geofencingClient: GeofencingClient
    lateinit var geofence : Geofence
    private lateinit var auth: FirebaseAuth
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private val TAG = MainActivity::class.java.simpleName


    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolBar)

        if (!checkPermissions()) {
            requestPermissions()
        }


        geofencingClient = LocationServices.getGeofencingClient(applicationContext)


        toolBar.setTitle(R.string.app_name)
        toolBar.setTitleTextColor(getColor(R.color.colorAccent))

        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true


        if (!isConnected){
            val builder = AlertDialog.Builder(this)
        builder.setMessage("No internet connection. Please connect to the internet to use GulbStudent.")
                .setCancelable(false)
                .setPositiveButton("Okay") { _, _ ->
                    finish()
                }

        val alert: AlertDialog = builder.create()
        alert.show()
        }



        auth = FirebaseAuth.getInstance()


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(WhatsOnFragment(), "SHOWS")
        adapter.addFragment(DealsFragment(), "DEALS")
        adapter.addFragment(CompetitionFragment(), "WIN")
        adapter.addFragment(BlogFragment(), "BLOG")
        viewPager.adapter = adapter

        tab_layout.setupWithViewPager(viewPager)
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                //
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                //
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                if (viewPager.currentItem == 1){
                    tab_layout.getTabAt(1)!!.orCreateBadge.isVisible = false

                }else if (viewPager.currentItem == 3){
                    tab_layout.getTabAt(3)!!.orCreateBadge.isVisible = false

                }
            }
        })

        val sharedPref: SharedPreferences = getSharedPreferences("DEAL_SIZE", 0)
        val sharedPrefBlog: SharedPreferences = getSharedPreferences("BLOG_SIZE", 0)

        val viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        viewModel.getDealSize().observe(this, Observer<Int> { t ->
            when {
                sharedPref.getInt("DEAL_SIZE", 0) == 0 -> {
                    println("INITIAL DEAL SIZE = $t")
                    val editor = sharedPref.edit()
                    editor.putInt("DEAL_SIZE", t!!)
                    editor.apply()
                }
                sharedPref.getInt("DEAL_SIZE", 0) < t!! -> {
                    println("BIGGER DEAL SIZE = $t")
                    tab_layout.getTabAt(1)!!.orCreateBadge.backgroundColor = getColor(R.color.colorAccent)
                    tab_layout.getTabAt(1)!!.orCreateBadge.isVisible = true
                    tab_layout.getTabAt(1)!!.badge!!.number = (t - sharedPref.getInt("DEAL_SIZE", 0))
                    val editor = sharedPref.edit()
                    editor.putInt("DEAL_SIZE", t)
                    editor.apply()
                }
                else -> {
                    println("SMALLER/SAME DEAL SIZE = $t")
                    val editor = sharedPref.edit()
                    editor.putInt("DEAL_SIZE", t)
                    editor.apply()
                }
            }
        })

        viewModel.getBlogSize().observe(this, Observer<Int> { t ->
            when {
                sharedPrefBlog.getInt("BLOG_SIZE", 0) == 0 -> {
                    println("INITIAL BLOG SIZE = $t")
                    val editor = sharedPrefBlog.edit()
                    editor.putInt("BLOG_SIZE", t!!)
                    editor.apply()
                }
                sharedPrefBlog.getInt("BLOG_SIZE", 0) < t!! -> {
                    println("BIGGER BLOG SIZE = $t")
                    tab_layout.getTabAt(3)!!.orCreateBadge.backgroundColor = getColor(R.color.colorAccent)
                    tab_layout.getTabAt(3)!!.orCreateBadge.isVisible = true
                    tab_layout.getTabAt(3)!!.badge!!.number = (t - sharedPrefBlog.getInt("BLOG_SIZE", 0))
                    val editor = sharedPrefBlog.edit()
                    editor.putInt("BLOG_SIZE", t)
                    editor.apply()
                }
                else -> {
                    println("SMALLER/SAME BLOG SIZE = $t")
                    val editor = sharedPrefBlog.edit()
                    editor.putInt("BLOG_SIZE", t)
                    editor.apply()
                }
            }
        })



    }

    private fun add(success: () -> Unit,
                    failure: (error: String) -> Unit) {
        // 1

        geofence = Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId("gulbenkian")
                // Set the circular region of this geofence.
                .setCircularRegion(
                        51.298564,
                        1.069307,
                        400.toFloat()
                )
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build()

        if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 2
            geofencingClient
                    .addGeofences(buildGeofencingRequest(geofence), geofencePendingIntent)
                    .addOnSuccessListener {
                        success()
                    }
                    .addOnFailureListener {
                        // 4
                        failure(GeofenceErrorMessages.getErrorString(applicationContext, it))
                    }


        }
    }


    private fun buildGeofencingRequest(geofence: Geofence): GeofencingRequest {
        println("GEOFENCE BUILD REQUEST")
        return GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofences(listOf(geofence))
                .build()
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser == null){
            this.viewPager.visibility = View.GONE
            this.tab_layout.visibility = View.GONE
            this.content.visibility = View.VISIBLE

            var fragmentManager = supportFragmentManager
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content, LoginFragment())
                    .commit()
        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            this.content.visibility = View.GONE
            this.viewPager.visibility = View.VISIBLE
            this.tab_layout.visibility = View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        toolBar.menu.clear()
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_whatson -> {
                this.viewPager.currentItem = 0
                this.content.visibility = View.GONE
                this.viewPager.visibility = View.VISIBLE
                this.tab_layout.visibility = View.VISIBLE
            }
            R.id.nav_deals -> {
                this.viewPager.currentItem = 1
                this.content.visibility = View.GONE
                this.viewPager.visibility = View.VISIBLE
                this.tab_layout.visibility = View.VISIBLE
                this.tab_layout?.getTabAt(1)?.orCreateBadge?.isVisible = false
            }
            R.id.nav_comp -> {
                this.viewPager.currentItem = 2
                this.content.visibility = View.GONE
                this.viewPager.visibility = View.VISIBLE
                this.tab_layout.visibility = View.VISIBLE
            }
            R.id.nav_blog -> {
                this.viewPager.currentItem = 3
                this.content.visibility = View.GONE
                this.viewPager.visibility = View.VISIBLE
                this.tab_layout.visibility = View.VISIBLE
            }
            R.id.nav_signout ->{
                Log.w(ContentValues.TAG, "Signed Out")

                this.viewPager.visibility = View.GONE
                this.tab_layout.visibility = View.GONE
                this.content.visibility = View.VISIBLE
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
                viewModel.getBookmarks(auth.currentUser!!).observe(this, Observer<List<Bookmarks>> { t ->
                            for (bookmark in t){
                                val bookmarkIntent = PendingIntent.getBroadcast(
                                        applicationContext,
                                        bookmark.id!!,
                                        Intent(applicationContext, AlarmBroadcastReceiver::class.java).apply {
                                        },
                                        PendingIntent.FLAG_CANCEL_CURRENT
                                )
                                alarmManager.cancel(bookmarkIntent)
                            }
                        })



                val categoryIntent = PendingIntent.getBroadcast(
                        applicationContext,
                        0,
                        Intent(applicationContext, AlarmBroadcastReceiver::class.java).apply {},
                        PendingIntent.FLAG_UPDATE_CURRENT
                )

                alarmManager.cancel(categoryIntent)


                FirebaseAuth.getInstance().signOut()
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
            R.id.nav_comp -> {
                CompetitionFragment()
            }
            R.id.nav_blog -> {
                BlogFragment()
            }
            R.id.nav_profile -> {
                ProfileFragment()
            }
            R.id.nav_gulb -> {
                AboutFragment()
            }
            R.id.nav_gulbcard -> {
                GulbCardFragment()
            }
            R.id.nav_gulbuncovered -> {
                UncoveredFragment()
            }
            R.id.nav_gulbstudent -> {
                GulbStuFragment()
            }
            R.id.nav_signout -> {
                LoginFragment()
            }
            else -> {
                LoginFragment()
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

    private fun checkPermissions(): Boolean {
        val permissionState: Int = ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        Log.i(TAG, "Requesting permission")

        ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, @NonNull permissions: Array<String>,
            @NonNull grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isEmpty()) {

                Log.i(TAG, "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                val sharedPrefGeo: SharedPreferences = getSharedPreferences("GEOFENCE", 0)

                if (!sharedPrefGeo.getBoolean("GEOFENCE", false)) {
                    add(success = {
                        println("GEOFENCE SUCCESS")
                        val editor = sharedPrefGeo.edit()
                        editor.putBoolean("GEOFENCE", true)
                        editor.apply()
                    },
                            failure = {
                                println("GEOFENCE FAILURE")
                                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                            })
                }
            } else {
                // Permission denied.
                requestPermissions()
            }
        }
    }

}