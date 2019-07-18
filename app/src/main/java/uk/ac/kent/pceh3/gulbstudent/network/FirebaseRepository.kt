package uk.ac.kent.pceh3.gulbstudent.network

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.LiveData
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk.ac.kent.pceh3.gulbstudent.model.Blog
import uk.ac.kent.pceh3.gulbstudent.model.Deal


/**
 * Created by pceh3 on 07/06/2019.
 */
class FirebaseRepository{

    enum class NetworkStatus {
        IDLE, LOADING
    }

    private val networkStatus = MutableLiveData<NetworkStatus>()

    fun getNetworkStatus(): LiveData<NetworkStatus> {
        return networkStatus
    }


    // Retrieve list of feed articles
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

                    fire.code = code1 //set
                    fire.description = description1 //set
                    fireList.add(fire)
                    dealList.value = fireList
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                networkStatus.setValue(NetworkStatus.IDLE)
            }
        })
        return dealList


    }

    // Retrieve list of feed articles
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

                    fire.article = article1 //set
                    fire.date = date1 //set
                    fire.title = title1 //set
                    fireList.add(fire)
                    blogList.value = fireList
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                networkStatus.setValue(NetworkStatus.IDLE)
            }
        })
        return blogList


    }


}