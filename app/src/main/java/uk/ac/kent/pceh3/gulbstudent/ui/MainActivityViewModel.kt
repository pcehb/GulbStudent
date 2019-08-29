package uk.ac.kent.pceh3.gulbstudent.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import uk.ac.kent.pceh3.gulbstudent.network.FirebaseRepository

// main activity viewmodel
class MainActivityViewModel: ViewModel() {

    private lateinit var dealSize : LiveData<Int>
    private lateinit var blogSize : LiveData<Int>

    private val repository = FirebaseRepository()

    fun getDealSize(): LiveData<Int> {

        dealSize = repository.getDealSize()

        return dealSize
    }

    fun getBlogSize(): LiveData<Int> {

        blogSize = repository.getBlogSize()

        return blogSize
    }

}
