package uk.ac.kent.pceh3.gulbstudent.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import uk.ac.kent.pceh3.gulbstudent.model.Deal
import uk.ac.kent.pceh3.gulbstudent.network.FirebaseRepository


/**
 * Created by pceh3 on 07/06/2019.
 */
class DealsViewModel : ViewModel() {

    private var dealLiveData: LiveData<List<Deal>>? = null

    private val repository = FirebaseRepository()

    fun getDeals(): LiveData<List<Deal>> {
        if (dealLiveData == null) {
            // Load from server
            dealLiveData = repository.getDeals()
        }
        return dealLiveData as LiveData<List<Deal>>
    }


}
