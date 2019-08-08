package uk.ac.kent.pceh3.gulbstudent.ui.profile

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import uk.ac.kent.pceh3.gulbstudent.model.Bookmarks
import uk.ac.kent.pceh3.gulbstudent.network.FirebaseRepository

class ProfileViewModel : ViewModel(){

    private var bookmarkLiveData: LiveData<List<Bookmarks>>? = null

    private val repository = FirebaseRepository()

    fun getBookmarks(user: FirebaseUser): LiveData<List<Bookmarks>> {
        if (bookmarkLiveData == null) {
            // Load from server
            bookmarkLiveData = repository.getBookmarks(user)
        }
        return bookmarkLiveData as LiveData<List<Bookmarks>>
    }
}