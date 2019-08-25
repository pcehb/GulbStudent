package uk.ac.kent.pceh3.gulbstudent

import android.Manifest
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.*
import androidx.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.android.gms.location.GeofencingClient
import android.os.Bundle
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
import androidx.lifecycle.Observer
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.material.tabs.TabLayout
import uk.ac.kent.pceh3.gulbstudent.model.Bookmarks
import uk.ac.kent.pceh3.gulbstudent.network.*
import uk.ac.kent.pceh3.gulbstudent.ui.comp.CompetitionFragment
import uk.ac.kent.pceh3.gulbstudent.ui.profile.ProfileViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener

//        , GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener, SharedPreferences.OnSharedPreferenceChangeListener

{
    lateinit var geofencingClient: GeofencingClient
    lateinit var geofence : Geofence
    private lateinit var auth: FirebaseAuth

//    private var mLocationRequest: LocationRequest? = null
//    private var mGoogleApiClient: GoogleApiClient? = null
//    private var locationProviderClient: FusedLocationProviderClient? = null
//
//
//    companion object {
//        private val TAG = MainActivity::class.java.simpleName
//        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
//        private val UPDATE_INTERVAL = (10 * 1000).toLong()
//        private val FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2
//        private val MAX_WAIT_TIME = UPDATE_INTERVAL * 3
//    }



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
        adapter.addFragment(WhatsOnFragment(), "WHAT'S ON")
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

                }else if (viewPager.currentItem == 2){
                    tab_layout.getTabAt(2)!!.orCreateBadge.isVisible = false

                }
            }
        })


//        if (!checkPermissions()) {
//            requestPermissions()
//        }


        //buildGoogleApiClient()

       // locationProviderClient = LocationServices.getFusedLocationProviderClient(this)

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

        if (geofence != null
                && ContextCompat.checkSelfPermission(
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

        //PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)

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
//
//    private fun buildAlertMessageNoGps() {
//
//        val builder = AlertDialog.Builder(this)
//        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
//                .setCancelable(false)
//                .setPositiveButton("Yes") { _, _ ->
//                    startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                            , 11)
//                }
//                .setNegativeButton("No") { dialog, _ ->
//                    dialog.cancel()
//                    finish()
//                }
//        val alert: AlertDialog = builder.create()
//        alert.show()
//
//
//    }
//
//    private fun createLocationRequest() {
//        mLocationRequest = LocationRequest()
//
//        mLocationRequest!!.interval = UPDATE_INTERVAL
//
//        // Sets the fastest rate for active location updates. This interval is exact, and your
//        // application will never receive updates faster than this value.
//        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL
//        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//
//        // Sets the maximum time when batched location updates are delivered. Updates may be
//        // delivered sooner than this interval.
//        mLocationRequest!!.maxWaitTime = MAX_WAIT_TIME
//    }
//
//    fun buildGoogleApiClient() {
//        if (mGoogleApiClient != null) {
//            createLocationRequest()
//            return
//        }
//        mGoogleApiClient = GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .enableAutoManage(this, this)
//                .addApi(LocationServices.API)
//                .build()
//        createLocationRequest()
//    }
//
//    fun getPendingIntent(): PendingIntent {
//        val intent = Intent(this, LocationUpdatesBroadcastReceiver::class.java)
//        intent.action = LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES
//        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//    }
//
//    fun checkPermissions(): Boolean {
//        val permissionState: Int = ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//        )
//        return permissionState == PackageManager.PERMISSION_GRANTED
//    }
//
//
//    private fun requestPermissions() {
//        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//        )
//
//        // Provide an additional rationale to the user. This would happen if the user denied the
//        // request previously, but didn't check the "Don't ask again" checkbox.
//        if (shouldProvideRationale) {
//            Log.i(TAG, "Displaying permission rationale to provide additional context.")
//            Snackbar.make(
//                    findViewById(R.id.container),
//                    R.string.permission_rationale,
//                    Snackbar.LENGTH_INDEFINITE
//            )
//                    .setAction("OK", View.OnClickListener {
//                        // Request permission
//                        ActivityCompat.requestPermissions(
//                                this@MainActivity,
//                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                                REQUEST_PERMISSIONS_REQUEST_CODE
//                        )
//                    })
//                    .show()
//        } else {
//            Log.i(TAG, "Requesting permission")
//            // Request permission. It's possible this can be auto answered if device policy
//            // sets the permission in a given state or the user denied the permission
//            // previously and checked "Never ask again".
//            ActivityCompat.requestPermissions(
//                    this@MainActivity,
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    REQUEST_PERMISSIONS_REQUEST_CODE
//            )
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//            requestCode: Int, @NonNull permissions: Array<String>,
//            @NonNull grantResults: IntArray
//    ) {
//        Log.i(TAG, "onRequestPermissionResult")
//        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
//            if (grantResults.isEmpty()) {
//                // If user interaction was interrupted, the permission request is cancelled and you
//                // receive empty arrays.
//                Log.i(TAG, "User interaction was cancelled.")
//            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission was granted. Kick off the process of building and connecting
//                // GoogleApiClient.
//                buildGoogleApiClient()
//            } else {
//                // Permission denied.
//
//                // Notify the user via a SnackBar that they have rejected a core permission for the
//                // app, which makes the Activity useless. In a real app, core permissions would
//                // typically be best requested during a welcome-screen flow.
//
//                // Additionally, it is important to remember that a permission might have been
//                // rejected without asking the user for permission (device policy or "Never ask
//                // again" prompts). Therefore, a user interface affordance is typically implemented
//                // when permissions are denied. Otherwise, your app could appear unresponsive to
//                // touches or interactions which have required permissions.
//                Snackbar.make(
//                        findViewById(R.id.container),
//                        R.string.permission_denied_explanation,
//                        Snackbar.LENGTH_INDEFINITE
//                )
//                        .setAction(R.string.settings) {
//                            // Build intent that displays the App settings screen.
//                            val intent = Intent()
//                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                            val uri = Uri.fromParts(
//                                    "package",
//                                    BuildConfig.APPLICATION_ID, null
//                            )
//                            intent.data = uri
//                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                            startActivity(intent)
//                        }
//                        .show()
//            }
//        }
//    }
//
//
//    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String) {
//        if (s == LocationResultHelper.KEY_LOCATION_UPDATES_RESULT) {
//            println(LocationResultHelper.getSavedLocationResult(this))
//        }
//    }
//
//    override fun onConnectionSuspended(p0: Int) {
//        val text = "Connection suspended"
//        Log.w("LOCATION", "$text: Error code: $p0")
//    }
//
//    override fun onConnectionFailed(p0: ConnectionResult) {
//        val text = "Exception while connecting to Google Play services"
//        Log.w("LOCATION", text + ": " + p0.errorMessage)
//    }
//
//    override fun onConnected(@Nullable bundle: Bundle?) {
//        Log.i(TAG, "GoogleApiClient connected")
//
//        try {
//            Log.i(TAG, "Starting location updates")
//
//            var updatesIntent = Intent(this, LocationUpdatesBroadcastReceiver::class.java)
//            updatesIntent.action = "uk.ac.kent.pceh3.gulbstudent.network.action.PROCESS_UPDATES"
//            var pendingUpdatesIntent = PendingIntent.getBroadcast(this, 0, updatesIntent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//            LocationServices.FusedLocationApi.requestLocationUpdates(
//                    mGoogleApiClient, mLocationRequest, pendingUpdatesIntent)
//
//
//           var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//            locationProviderClient?.requestLocationUpdates(mLocationRequest, getPendingIntent())
//
//            if (LocationResultHelper.getSavedLocationResult(this) == null) {
//                println("Saved location result NULL")
//            } else {
//
//            }
//
//        } catch (e: SecurityException) {
//            e.printStackTrace()
//        }
//    }

    // Custom method to determine whether a service is running
    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        // Loop through the running services
        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                // If the service is running then return true
                return true
            }
        }
        return false
    }


// Extension function to show toast message
    fun Context.toast(message:String){
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
    }
}