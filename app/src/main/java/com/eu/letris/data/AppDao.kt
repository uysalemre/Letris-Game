package com.eu.letris.data

import androidx.room.*

@Dao
interface WordsDao {
    @Query("SELECT * FROM tb_words WHERE word LIKE :qWord LIMIT 1")
    suspend fun checkWord(qWord: String): WordEntity?
}

@Dao
interface ScoresDao {
    @Query("SELECT * FROM tb_scores ORDER BY score DESC LIMIT 100")
    suspend fun getScoreList(): List<ScoreEntity>

    @Insert(entity = ScoreEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addScore(score: ScoreEntity): Long
}