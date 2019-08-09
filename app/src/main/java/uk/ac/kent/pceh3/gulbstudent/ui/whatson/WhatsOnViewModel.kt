package uk.ac.kent.pceh3.gulbstudent.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import uk.ac.kent.pceh3.gulbstudent.model.Bookmarks
import uk.ac.kent.pceh3.gulbstudent.model.WhatsOn
import uk.ac.kent.pceh3.gulbstudent.network.FirebaseRepository
import uk.ac.kent.pceh3.gulbstudent.network.WhatsOnAjax

/**
 * Created by pceh3 on 14/07/2019.
 */
class WhatsOnViewModel : ViewModel() {

    private var whatsOnLiveData: LiveData<List<WhatsOn>>? = null

    private lateinit var showBoolean: LiveData<Boolean>

    private val repository = WhatsOnAjax()

    fun getWhatsOn(search: String, eventType : String, startDate : String, endDate : String): LiveData<List<WhatsOn>> {
            // Load from server
            whatsOnLiveData = repository.getWhatsOn(search, eventType, startDate, endDate)
        return whatsOnLiveData as LiveData<List<WhatsOn>>
    }

    private val firebaseRepository = FirebaseRepository()

    fun getShowBookmarked(user: FirebaseUser, indexUrl: String): LiveData<Boolean> {
        showBoolean = firebaseRepository.getShowBookmarked(user, indexUrl)
        return showBoolean
    }
}