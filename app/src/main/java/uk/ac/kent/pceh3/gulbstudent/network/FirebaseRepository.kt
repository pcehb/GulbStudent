package uk.ac.kent.pceh3.gulbstudent.network

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk.ac.kent.pceh3.gulbstudent.model.*


/**
 * Created by pceh3 on 07/06/2019.
 */
class FirebaseRepository {

    enum class NetworkStatus {
        IDLE, LOADING
    }

    private val networkStatus = MutableLiveData<NetworkStatus>()

    fun getNetworkStatus(): LiveData<NetworkStatus> {
        return networkStatus
    }


    // Retrieve list of deals
    fun getDeals(): LiveData<List<Deal>> {

        val dealList = MutableLiveData<List<Deal>>()
        val fireList = ArrayList<Deal>()

        val myRef = FirebaseDatabase.getInstance().getReference("deals")
        networkStatus.setValue(NetworkStatus.LOADING)
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                networkStatus.setValue(NetworkStatus.IDLE)

                fireList.clear()
                for (dataSnapshot1 in dataSnapshot.children) {
                    val firevalue = dataSnapshot1.getValue<Deal>(Deal::class.java)
                    val fire = Deal()

                    val code1 = firevalue!!.code
                    val description1 = firevalue.description
                    val photoURL1 = firevalue.photoURL

                    fire.code = code1 //set
                    fire.description = description1 //set
                    fire.photoURL = photoURL1 //set
                    fireList.add(fire)
                }
                fireList.reverse()
                dealList.value = fireList

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                networkStatus.setValue(NetworkStatus.IDLE)
            }
        })
        return dealList
    }


    // Retrieve size of deals
    fun getDealSize(): LiveData<Int> {

        var dealSize = MutableLiveData<Int>()

        val myRef = FirebaseDatabase.getInstance().getReference("deals")
        networkStatus.setValue(NetworkStatus.LOADING)
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                networkStatus.setValue(NetworkStatus.IDLE)

                dealSize.value = dataSnapshot.childrenCount.toInt()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                networkStatus.setValue(NetworkStatus.IDLE)
            }
        })
        return dealSize
    }

    // Retrieve comps
    fun getComp(): LiveData<Comp> {

        val comp = MutableLiveData<Comp>()

        val myRef = FirebaseDatabase.getInstance().getReference("competition")
        networkStatus.setValue(NetworkStatus.LOADING)
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                networkStatus.setValue(NetworkStatus.IDLE)

                val firevalue = dataSnapshot.getValue<Comp>(Comp::class.java)
                val fire = Comp()


                val closeDate1 = firevalue!!.closeDate
                val description1 = firevalue.description
                val photoURL1 = firevalue.photoURL
                val title1 = firevalue.title

                fire.closeDate = closeDate1 //set
                fire.description = description1 //set
                fire.photoURL = photoURL1 //set
                fire.title = title1 //set

                comp.value = fire
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                networkStatus.setValue(NetworkStatus.IDLE)
            }
        })
        return comp
    }

    // Retrieve list of entries
    fun getCompEntries(): LiveData<List<String>> {

        val entries = MutableLiveData<List<String>>()
        val fireList = ArrayList<String>()

        val myRef = FirebaseDatabase.getInstance().getReference("competition").child("entries")
        networkStatus.value = NetworkStatus.LOADING
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                networkStatus.value = NetworkStatus.IDLE

                fireList.clear()
                for (dataSnapshot1 in dataSnapshot.children) {
                    val firevalue = dataSnapshot1.value as String

                    fireList.add(firevalue)
                }

                entries.value = fireList

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                networkStatus.value = NetworkStatus.IDLE
            }
        })
        return entries
    }

    //get gulbcard boolean for user
    fun getGulbCard(user: FirebaseUser): LiveData<Boolean> {

        var gulbCard = MutableLiveData<Boolean>()

        val ref = FirebaseDatabase.getInstance().reference.child("users").child(user.uid).child("gulbCard")

        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                gulbCard.value = dataSnapshot.value as Boolean

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                networkStatus.value = NetworkStatus.IDLE
            }
        })
        return gulbCard
    }


    // Retrieve list of blogs
    fun getBlog(): LiveData<List<Blog>> {

        val blogList = MutableLiveData<List<Blog>>()
        val fireList = ArrayList<Blog>()

        val myRef = FirebaseDatabase.getInstance().getReference("blog")
        networkStatus.setValue(NetworkStatus.LOADING)
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                networkStatus.setValue(NetworkStatus.IDLE)

                fireList.clear()
                for (dataSnapshot1 in dataSnapshot.children) {
                    val firevalue = dataSnapshot1.getValue<Blog>(Blog::class.java)
                    val fire = Blog()

                    val article1 = firevalue!!.article
                    val date1 = firevalue.date
                    val title1 = firevalue.title
                    val photoURL1 = firevalue.photoURL

                    fire.article = article1 //set
                    fire.date = date1 //set
                    fire.title = title1 //set
                    fire.photoURL = photoURL1 //set
                    fireList.add(fire)
                    blogList.value = fireList
                }
                fireList.reverse()
                blogList.value = fireList

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                networkStatus.setValue(NetworkStatus.IDLE)
            }
        })
        return blogList
    }

    // Retrieve list of deals
    fun getBlogSize(): LiveData<Int> {

        var blogSize = MutableLiveData<Int>()

        val myRef = FirebaseDatabase.getInstance().getReference("blog")
        networkStatus.setValue(NetworkStatus.LOADING)
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                networkStatus.setValue(NetworkStatus.IDLE)

                blogSize.value = dataSnapshot.childrenCount.toInt()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                networkStatus.setValue(NetworkStatus.IDLE)
            }
        })
        return blogSize
    }

    // Retrieve list of bookmarks
    fun getBookmarks(user: FirebaseUser): LiveData<List<Bookmarks>> {

        val bookmarksList = MutableLiveData<List<Bookmarks>>()
        val fireList = ArrayList<Bookmarks>()

        val myRef = FirebaseDatabase.getInstance().reference.child("users").child(user.uid).child("bookmarked")
        networkStatus.setValue(NetworkStatus.LOADING)
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                networkStatus.setValue(NetworkStatus.IDLE)

                fireList.clear()
                for (dataSnapshot1 in dataSnapshot.children) {
                    val firevalue = dataSnapshot1.getValue<Bookmarks>(Bookmarks::class.java)
                    val fire = Bookmarks()

                    val title1 = firevalue!!.title
                    val date1 = firevalue.date
                    val month1 = firevalue.month
                    val year1 = firevalue.year
                    val index1 = firevalue.index
                    val id1 = firevalue.id
                    val photoURL = firevalue.photoURL
                    val description = firevalue.description


                    fire.title = title1 //set
                    fire.date = date1 //set
                    fire.month = month1 //set
                    fire.year = year1 //set
                    fire.index = index1 //set
                    fire.id = id1 //set
                    fire.photoURL = photoURL
                    fire.description = description
                    fireList.add(fire)
                    bookmarksList.value = fireList
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                networkStatus.setValue(NetworkStatus.IDLE)
            }
        })
        return bookmarksList
    }

    // Check if particular show is bookmarked
    fun getShowBookmarked(user: FirebaseUser, indexUrl: String): LiveData<Boolean> {

        var bookmarkBoolean = MutableLiveData<Boolean>()
        val fireList = ArrayList<Bookmarks>()

        val myRef = FirebaseDatabase.getInstance().reference.child("users").child(user.uid).child("bookmarked")
        networkStatus.setValue(NetworkStatus.LOADING)
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                networkStatus.setValue(NetworkStatus.IDLE)

                fireList.clear()
                for (dataSnapshot1 in dataSnapshot.children) {
                    val firevalue = dataSnapshot1.getValue<Bookmarks>(Bookmarks::class.java)

                    val index1 = firevalue!!.index

                    if (index1 == indexUrl) {
                        bookmarkBoolean.value = true
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                networkStatus.setValue(NetworkStatus.IDLE)
            }
        })
        return bookmarkBoolean

    }

    // Check what categories are selected
    fun getCats(user: FirebaseUser): LiveData<List<Categories>> {

        val categoriesList = MutableLiveData<List<Categories>>()
        val fireList = ArrayList<Categories>()

        val myRef = FirebaseDatabase.getInstance().reference.child("users").child(user.uid).child("categories")
        networkStatus.setValue(NetworkStatus.LOADING)
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                networkStatus.setValue(NetworkStatus.IDLE)

                fireList.clear()
                val firevalue = dataSnapshot.getValue(Categories::class.java)
                val fire = Categories()

                val archive = firevalue!!.archive
                val audioDescribed = firevalue.audioDescribed
                val boing = firevalue.boing
                val cafe = firevalue.cafe
                val captionedSubtitles = firevalue.captionedSubtitles
                val comedy = firevalue.comedy
                val family = firevalue.family
                val festival = firevalue.festival
                val foreign = firevalue.foreign
                val music = firevalue.music
                val live = firevalue.live
                val relaxed = firevalue.relaxed
                val talks = firevalue.talks
                val theatreDance = firevalue.theatreDance
                val workshops = firevalue.workshops

                fire.archive = archive //set
                fire.audioDescribed = audioDescribed //set
                fire.boing = boing //set
                fire.cafe = cafe //set
                fire.captionedSubtitles = captionedSubtitles //set
                fire.comedy = comedy //set
                fire.family = family
                fire.festival = festival
                fire.foreign = foreign
                fire.music = music
                fire.live = live
                fire.relaxed = relaxed
                fire.talks = talks
                fire.theatreDance = theatreDance
                fire.workshops = workshops
                fireList.add(fire)
                categoriesList.value = fireList

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                networkStatus.setValue(NetworkStatus.IDLE)
            }
        })
        return categoriesList

    }
}


