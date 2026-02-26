package com.justclient.speakingpractice.data.repos

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.justclient.speakingpractice.data.classes.Word
import com.justclient.speakingpractice.utils.GlobalConsts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore(name = "words_datastore")

class DataStoreRepo(private val context: Context) {

    // The key by which our list will be stored in the DataStore as a JSON string
    private val wordsKey = stringPreferencesKey("words_list")

    // 1. RECEIVING A LIST (with automatic updating via Flow)
    private val allWordsFlow: Flow<List<Word>> = context.dataStore.data.map { preferences ->
        val jsonString = preferences[wordsKey]
        if (jsonString != null) {
            Json.decodeFromString<List<Word>>(jsonString)
        } else {
            emptyList()
        }
    }

    /**
     * @param wordType The type of word to filter (for example, GlobalConsts.TP_WORD).
     *      * If passed null, returns all words.
     */
    fun getWordsByType(wordType: Int?): Flow<List<Word>> {
        return allWordsFlow.map { words ->
            if (wordType == null || wordType == GlobalConsts.TP_ALL) {
                words
            } else {
                words.filter { it.type == wordType }
            }
        }
    }

    // 2. ADDING A NEW WORD
    suspend fun addWord(newWord: Word) {
        context.dataStore.edit { preferences ->
            val currentListJson = preferences[wordsKey]
            val currentList = if (currentListJson != null) {
                Json.decodeFromString<MutableList<Word>>(currentListJson)
            } else {
                mutableListOf()
            }
            currentList.add(newWord)
            preferences[wordsKey] = Json.encodeToString(currentList)
        }
    }

    // 3. EDITING AN EXISTING WORD
    suspend fun editWord(updatedWord: Word) {
        context.dataStore.edit { preferences ->
            val currentListJson = preferences[wordsKey] ?: return@edit
            val currentList = Json.decodeFromString<MutableList<Word>>(currentListJson)
            val index = currentList.indexOfFirst { it.id == updatedWord.id }
            if (index != -1) {
                currentList[index] = updatedWord
                preferences[wordsKey] = Json.encodeToString(currentList)
            }
        }
    }

    // 4. DELETE A WORD BY ID
    suspend fun deleteWord(wordId: String) {
        context.dataStore.edit { preferences ->
            val currentListJson = preferences[wordsKey] ?: return@edit
            val currentList = Json.decodeFromString<MutableList<Word>>(currentListJson)
            // Удаляем все элементы с таким ID
            currentList.removeAll { it.id == wordId }
            preferences[wordsKey] = Json.encodeToString(currentList)
        }
    }
}
