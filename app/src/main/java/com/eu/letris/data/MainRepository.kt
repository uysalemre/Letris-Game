package com.eu.letris.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val wordsDatabase: WordsDatabase,
    private val scoresDatabase: ScoresDatabase
) {
    suspend fun checkWordIsExists(word: String) = flow {
        emit(wordsDatabase.wordsDao().checkWord(word))
    }.flowOn(Dispatchers.IO)

    suspend fun addPoint(point: Int) = flow {
        emit(scoresDatabase.scoresDao().addScore(ScoreEntity(score = point)))
    }.flowOn(Dispatchers.IO)

    suspend fun getTopPointList() = flow {
        emit(scoresDatabase.scoresDao().getScoreList())
    }.flowOn(Dispatchers.IO)
}