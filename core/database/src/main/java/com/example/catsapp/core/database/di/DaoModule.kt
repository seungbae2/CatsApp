package com.example.catsapp.core.database.di

import com.example.catsapp.core.database.CatDatabase
import com.example.catsapp.core.database.dao.CatDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    fun provideCatDao(database: CatDatabase): CatDao {
        return database.catDao()
    }
}