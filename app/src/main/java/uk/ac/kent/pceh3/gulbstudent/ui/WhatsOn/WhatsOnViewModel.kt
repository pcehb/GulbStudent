package uk.ac.kent.pceh3.gulbstudent.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import uk.ac.kent.pceh3.gulbstudent.model.WhatsOn
import uk.ac.kent.pceh3.gulbstudent.network.WhatsOnAjax

/**
 * Created by pceh3 on 14/07/2019.
 */
class WhatsOnViewModel : ViewModel() {

    private var whatsOnLiveData: LiveData<List<WhatsOn>>? = null

    private val repository = WhatsOnAjax()

    fun getWhatsOn(search: String, eventType : String, date : String): LiveData<List<WhatsOn>> {
            // Load from server
            whatsOnLiveData = repository.getWhatsOn(search, eventType, date)
        return whatsOnLiveData as LiveData<List<WhatsOn>>
    }
}