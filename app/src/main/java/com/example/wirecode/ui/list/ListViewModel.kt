package com.example.wirecode.ui.list

import android.app.Application
import androidx.lifecycle.*
import com.example.wirecode.database.Mapping
import com.example.wirecode.database.MappingDatabase
import com.example.wirecode.database.MappingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class ListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MappingRepository
    val allMappings: LiveData<List<Mapping>>

    init {
        val mappingDao = MappingDatabase.getDatabase(application, viewModelScope).mappingDao()
        repository = MappingRepository(mappingDao)
        allMappings = repository.getAll
    }

    fun insert(mapping: Mapping) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(mapping)
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}