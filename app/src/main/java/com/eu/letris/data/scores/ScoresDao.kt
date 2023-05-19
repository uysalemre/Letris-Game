package com.eu.letris.data.scores

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScoresDao {
    @Query("SELECT score FROM tb_scores ORDER BY score DESC LIMIT 100")
    suspend fun getScoreList(): List<Int>

    @Insert(entity = ScoreEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addScore(score: ScoreEntity): Long
}