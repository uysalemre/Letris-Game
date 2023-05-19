package com.eu.letris.data.words

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WordsRepository @Inject constructor(private val wordsDatabase: WordsDatabase) {
    suspend fun checkWordIsExists(word: String) = flow {
        emit(wordsDatabase.wordsDao().checkWord(word))
    }.flowOn(Dispatchers.IO)
}