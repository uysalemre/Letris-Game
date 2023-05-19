package com.eu.letris.di

import android.content.Context
import androidx.room.Room
import com.eu.letris.data.scores.ScoresDatabase
import com.eu.letris.data.words.WordsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesWordsDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, WordsDatabase::class.java, "turkish_words.db")
            .createFromAsset("turkish_words.db")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideScoresDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ScoresDatabase::class.java, "scores.db").build()
}