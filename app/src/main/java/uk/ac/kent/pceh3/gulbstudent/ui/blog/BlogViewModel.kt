package uk.ac.kent.pceh3.gulbstudent.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import uk.ac.kent.pceh3.gulbstudent.model.Blog
import uk.ac.kent.pceh3.gulbstudent.network.FirebaseRepository

/**
 * Created by pceh3 on 14/07/2019.
 */
class BlogViewModel : ViewModel() {


    private var blogLiveData: LiveData<List<Blog>>? = null

    private val repository = FirebaseRepository()

    fun getBlog(): LiveData<List<Blog>> {
        if (blogLiveData == null) {
            // Load from server
            blogLiveData = repository.getBlog()
        }
        return blogLiveData as LiveData<List<Blog>>
    }


}
