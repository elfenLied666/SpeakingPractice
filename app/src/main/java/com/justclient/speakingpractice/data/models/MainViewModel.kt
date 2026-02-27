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

    private val allWords: StateFlow<List<Word>> = dataStoreRepo.getWordsByType(null)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val wordsCount: StateFlow<Int> = allWords.map { list ->
        list.count { it.type == GlobalConsts.TP_WORD }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val sentencesCount: StateFlow<Int> = allWords.map { list ->
        // Считаем количество элементов с нужным типом
        list.count { it.type == GlobalConsts.TP_SENTENCE }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    /*init {
        checkAndSeedData()
    }*/

    fun checkAndSeedData() {
        viewModelScope.launch {
            val currentWords = dataStoreRepo.getWordsByType(null).first()
            if (currentWords.isEmpty()) {
                val testData = listOf(
                    Word(enWord = "Hi", spWord = "hola", type = GlobalConsts.TP_WORD),
                    Word(enWord = "Good bye", spWord = "Adiós", type = GlobalConsts.TP_WORD),
                    Word(enWord = "Good afternoon", spWord = "Buenas tardes.", type = GlobalConsts.TP_WORD),
                    Word(enWord = "Good morning", spWord = "Buenos días", type = GlobalConsts.TP_WORD),

                    Word(enWord = "I am fine, thank you", spWord = "Estoy bien, gracias", type = GlobalConsts.TP_SENTENCE),
                    Word(enWord = "What is your name?", spWord = "¿Cómo te llamas?", type = GlobalConsts.TP_SENTENCE),
                    Word(enWord = "Where are you from?", spWord = "¿De dónde eres?", type = GlobalConsts.TP_SENTENCE),
                    Word(enWord = "See you later", spWord = "Hasta luego", type = GlobalConsts.TP_SENTENCE)
                )

                testData.forEach { word ->
                    dataStoreRepo.addWord(word)
                }
            }
        }
    }

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
