package com.eu.letris.data.scores

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ScoresRepository @Inject constructor(private val scoresDatabase: ScoresDatabase) {
    suspend fun addPoint(point: Int) = flow {
        emit(scoresDatabase.scoresDao().addScore(ScoreEntity(score = point)))
    }.flowOn(Dispatchers.IO)

    suspend fun getTopPointList() = flow {
        emit(scoresDatabase.scoresDao().getScoreList())
    }.flowOn(Dispatchers.IO)
}