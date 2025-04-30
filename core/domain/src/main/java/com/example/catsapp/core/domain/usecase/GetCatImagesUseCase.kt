package com.example.catsapp.core.domain.usecase

import com.example.catsapp.core.common.Resource
import com.example.catsapp.core.model.Cat
import com.example.catsapp.core.data_api.repository.CatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCatImagesUseCase @Inject constructor(
    private val repository: CatRepository
) {
    operator fun invoke(limit: Int = 10): Flow<Resource<List<Cat>>> {
        return repository.getCats(limit)
    }
}