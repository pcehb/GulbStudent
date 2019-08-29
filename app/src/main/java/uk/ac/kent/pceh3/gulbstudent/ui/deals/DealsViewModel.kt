package uk.ac.kent.pceh3.gulbstudent.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import uk.ac.kent.pceh3.gulbstudent.model.Deal
import uk.ac.kent.pceh3.gulbstudent.network.FirebaseRepository

// view model for deals fragment
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
