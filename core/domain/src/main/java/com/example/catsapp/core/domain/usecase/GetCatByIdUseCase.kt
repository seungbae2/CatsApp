package com.example.catsapp.core.domain.usecase

import com.example.catsapp.core.data_api.repository.CatRepository
import com.example.catsapp.core.model.Cat
import javax.inject.Inject

class GetCatByIdUseCase @Inject constructor(
    private val catsRepository: CatRepository,
) {
    suspend fun invoke(id: String): Cat = catsRepository.getCatById(id)
} 