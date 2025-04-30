package com.example.catsapp.core.database.di

import android.content.Context
import androidx.room.Room
import com.example.catsapp.core.database.CatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesCatDatabase(
        @ApplicationContext context: Context,
    ): CatDatabase = Room.databaseBuilder(
        context,
        CatDatabase::class.java,
        "cat-database"
    ).build()
}