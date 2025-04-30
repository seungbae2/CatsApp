package com.example.catsapp.core.domain.repository

import com.example.catsapp.core.common.Resource
import com.example.catsapp.core.domain.model.Cat
import kotlinx.coroutines.flow.Flow

interface CatRepository {
    fun getCats(limit: Int) : Flow<Resource<List<Cat>>>
}