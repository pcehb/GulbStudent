package uk.ac.kent.pceh3.gulbstudent.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import uk.ac.kent.pceh3.gulbstudent.model.Bookmarks
import uk.ac.kent.pceh3.gulbstudent.model.Categories
import uk.ac.kent.pceh3.gulbstudent.network.FirebaseRepository

class ProfileViewModel : ViewModel(){

    private var bookmarkLiveData: LiveData<List<Bookmarks>>? = null
    private var categoryLiveData: LiveData<List<Categories>>? = null

    private val repository = FirebaseRepository()

    fun getBookmarks(user: FirebaseUser): LiveData<List<Bookmarks>> {
        if (bookmarkLiveData == null) {
            // Load from server
            bookmarkLiveData = repository.getBookmarks(user)
        }
        return bookmarkLiveData as LiveData<List<Bookmarks>>
    }

    fun getCats(user: FirebaseUser): LiveData<List<Categories>> {
        if (categoryLiveData == null) {
            // Load from server
            categoryLiveData = repository.getCats(user)
        }
        return categoryLiveData as LiveData<List<Categories>>
    }
}