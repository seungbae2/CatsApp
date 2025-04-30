package com.example.catsapp.core.data.di

import com.example.catsapp.core.data.repository.CatRepositoryImpl
import com.example.catsapp.core.domain.repository.CatRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindCatRepository(
        catRepositoryImpl: CatRepositoryImpl,
    ): CatRepository

} 