package com.example.catsapp.core.data.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.example.catsapp.core.common.Resource
import com.example.catsapp.core.data.mapper.toDomain
import com.example.catsapp.core.data_api.repository.CatRepository
import com.example.catsapp.core.model.Cat
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
            val response = api.getCatImages(limit)
            val cats = response.map { it.toDomain() }
            emit(Resource.Success(cats))
        } catch (e: HttpException) {
            emit(Resource.Error(e))
        } catch (e: IOException) {
            emit(Resource.Error(e))
        }
    }
}