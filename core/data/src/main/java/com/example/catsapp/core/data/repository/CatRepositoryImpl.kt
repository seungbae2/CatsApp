package com.example.catsapp.core.data.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.catsapp.core.common.Resource
import com.example.catsapp.core.domain.model.Cat
import com.example.catsapp.core.domain.repository.CatRepository
import com.example.catsapp.core.data.mapper.toDomain
import com.example.catsapp.core.network.retrofit.CatApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class CatRepositoryImpl @Inject constructor(
    private val api: CatApi
) : CatRepository {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun getCats(limit: Int): Flow<Resource<List<Cat>>> = flow {
        emit(Resource.Loading)
        try {
            val catsResponse = api.getCatImages(limit)
            when (val resource = api.getCatImages(limit)) {
                is Resource.Success -> {
                    val cats = resource.data.map { it.toDomain() }
                    emit(Resource.Success(cats))
                }
                is Resource.Error -> emit(resource)
                Resource.Loading -> {}
            }
        } catch (e: HttpException) {
            emit(Resource.Error(e))
        } catch (e: IOException) {
            emit(Resource.Error(e))
        }
    }
}