package com.eu.letris.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WordEntity::class], version = 1, exportSchema = false)
abstract class WordsDatabase : RoomDatabase() {
    abstract fun wordsDao(): WordsDao
}

@Database(entities = [ScoreEntity::class], version = 1, exportSchema = false)
abstract class ScoresDatabase : RoomDatabase() {
    abstract fun scoresDao(): ScoresDao
}