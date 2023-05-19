package com.eu.letris.data.scores

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ScoreEntity::class], version = 1, exportSchema = false)
abstract class ScoresDatabase : RoomDatabase() {
    abstract fun scoresDao(): ScoresDao
}