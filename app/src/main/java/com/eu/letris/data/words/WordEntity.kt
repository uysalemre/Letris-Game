package com.eu.letris.data.words

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_words")
data class WordEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("word") val word: String
)
