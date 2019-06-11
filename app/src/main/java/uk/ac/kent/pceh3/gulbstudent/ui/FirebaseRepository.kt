package uk.ac.kent.pceh3.gulbstudent.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.LiveData
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk.ac.kent.pceh3.gulbstudent.model.Deal
import com.google.firebase.database.DatabaseReference;


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
        val myRef = FirebaseDatabase.getInstance().getReference("deals")
        networkStatus.setValue(NetworkStatus.LOADING)
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val map = dataSnapshot.value as Map<String, Any>
                //val value = dataSnapshot.getValue(String::class.java)
                Log.d(TAG, "Value is: $map")

                //dealList.value(dataSnapshot.child("-LW76biueowe").value)
                networkStatus.setValue(NetworkStatus.IDLE)


            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
                networkStatus.setValue(NetworkStatus.IDLE)
            }
        })
        return dealList


    }


}