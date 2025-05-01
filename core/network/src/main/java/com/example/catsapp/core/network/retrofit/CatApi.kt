package com.example.catsapp.core.network.retrofit

import com.example.catsapp.core.network.response.CatResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CatApi {
    @GET("v1/images/search")
    suspend fun getCatImages(
        @Query("limit") limit: Int = 10
    ): List<CatResponse>
}