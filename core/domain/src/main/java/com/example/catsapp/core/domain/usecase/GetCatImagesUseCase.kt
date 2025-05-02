package com.example.catsapp.core.domain.usecase

import androidx.paging.PagingData
import com.example.catsapp.core.model.Cat
import com.example.catsapp.core.data_api.repository.CatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCatImagesUseCase @Inject constructor(
    private val repository: CatRepository
) {
    operator fun invoke(pageSize: Int = 10): Flow<PagingData<Cat>> {
        return repository.getCatsPaging(pageSize)
    }
}