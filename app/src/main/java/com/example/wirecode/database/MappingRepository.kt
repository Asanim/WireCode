package com.example.wirecode.database

import androidx.lifecycle.LiveData


//todo: idea: load data from IOT (digital factory) network. Staff may add via computer
class MappingRepository (private val mappingDao: MappingDao) {
    val getAll: LiveData<List<Mapping>> = mappingDao.getAlphabetizedMappings()

    //suspend: needs to be called from a coroutine
    suspend fun insert(mapping: Mapping) {
        mappingDao.insert(mapping)
    }
}