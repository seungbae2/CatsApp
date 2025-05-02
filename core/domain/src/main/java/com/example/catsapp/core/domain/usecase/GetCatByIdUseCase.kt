package com.example.catsapp.core.domain.usecase

import com.example.catsapp.core.data_api.repository.CatRepository
import com.example.catsapp.core.model.Cat
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCatByIdUseCase @Inject constructor(
    private val catsRepository: CatRepository,
) {
    fun invoke(id: String): Flow<Cat> = catsRepository.getCatById(id)
} 