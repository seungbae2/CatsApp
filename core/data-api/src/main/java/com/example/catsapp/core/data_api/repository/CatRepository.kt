package com.example.catsapp.core.data_api.repository

import androidx.paging.PagingData
import com.example.catsapp.core.model.Cat
import kotlinx.coroutines.flow.Flow

interface CatRepository {
    fun getCatsPaging(pageSize: Int): Flow<PagingData<Cat>>

    suspend fun getCatById(id: String): Cat
}