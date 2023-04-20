package com.eu.letris.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_words")
data class WordEntity(
    @PrimaryKey(autoGenerate = false) @ColumnInfo("word") val word: String
)

@Entity(tableName = "tb_scores")
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("id") val id: Int = 0,
    @ColumnInfo("score") val score: Int
)