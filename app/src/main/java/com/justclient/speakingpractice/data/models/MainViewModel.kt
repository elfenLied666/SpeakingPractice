package com.justclient.speakingpractice.data.models

import com.justclient.speakingpractice.data.repos.DataStoreRepo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.justclient.speakingpractice.data.classes.Word
import com.justclient.speakingpractice.utils.GlobalConsts
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStoreRepo = DataStoreRepo(application)

    private val _currentFilter = MutableStateFlow<Int?>(GlobalConsts.TP_ALL)
    val currentFilter: StateFlow<Int?> = _currentFilter.asStateFlow()

    val filteredWords: StateFlow<List<Word>> = _currentFilter.flatMapLatest { filterType ->
        dataStoreRepo.getWordsByType(filterType)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    fun setFilter(wordType: Int) {
        _currentFilter.value = wordType
    }

    fun addWord(enWord: String, spWord: String, type: Int) {
        viewModelScope.launch {
            val newWord = Word(enWord = enWord, spWord = spWord, type = type)
            dataStoreRepo.addWord(newWord)
        }
    }

    fun editWord(wordToEdit: Word) {
        viewModelScope.launch {
            dataStoreRepo.editWord(wordToEdit)
        }
    }

    fun deleteWord(wordId: String) {
        viewModelScope.launch {
            dataStoreRepo.deleteWord(wordId)
        }
    }
}
