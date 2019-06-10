package uk.ac.kent.pceh3.gulbstudent.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk.ac.kent.pceh3.gulbstudent.model.Deal


/**
 * Created by pceh3 on 07/06/2019.
 */
class DealsViewModel : ViewModel() {
  //  var deals: MutableLiveData<List<Deal>> = MutableLiveData()


    private var dealLiveData: LiveData<Deal>? = null

    private val repository = FirebaseRepository()

//    fun getDeals() : LiveData<List<Deal>> {
//        if (deals == null) {
//            deals = MutableLiveData<List<Deal>>()
//            loadDeals()
//        }
//        return deals;
//    }

    fun getDeals(): LiveData<Deal> {
        if (dealLiveData == null) {
            // Load from server
            dealLiveData = repository.getDeals()
        }
        return dealLiveData as LiveData<Deal>
    }


//    fun loadDeals() {
//
//        if (deals == null) {
//            // Load from server
//            deals = repository.getInstance().getDeals()
//        }
//        return deals
//
//    }

}
