package uk.ac.kent.pceh3.gulbstudent.ui.comp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import uk.ac.kent.pceh3.gulbstudent.model.Comp
import uk.ac.kent.pceh3.gulbstudent.network.FirebaseRepository

class CompViewModel: ViewModel(){

    private var compLiveData: LiveData<Comp>? = null
    private lateinit var gulbCardLiveData: LiveData<Boolean>
    private var entriesLiveData: LiveData<List<String>>? = null

    private val repository = FirebaseRepository()

    fun getComp(): LiveData<Comp> {
        if (compLiveData == null) {
            // Load from server
            compLiveData = repository.getComp()
        }
        return compLiveData as LiveData<Comp>
    }

    fun getGulbCard(user: FirebaseUser): LiveData<Boolean> {
            // Load from server
            gulbCardLiveData = repository.getGulbCard(user)

        return gulbCardLiveData
    }

    fun getEntries(): LiveData<List<String>> {
        // Load from server
       entriesLiveData = repository.getCompEntries()

        return entriesLiveData as LiveData<List<String>>
    }
}