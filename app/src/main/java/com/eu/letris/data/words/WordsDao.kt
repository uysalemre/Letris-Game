package com.eu.letris.data.words

import androidx.room.*

@Dao
interface WordsDao {
    @Query("SELECT * FROM tb_words WHERE word LIKE :qWord LIMIT 1")
    suspend fun checkWord(qWord: String): WordEntity?
}