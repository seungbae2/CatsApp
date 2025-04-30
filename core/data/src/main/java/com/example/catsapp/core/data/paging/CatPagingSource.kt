package com.example.catsapp.core.data.paging

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.catsapp.core.data.mapper.toDomain
import com.example.catsapp.core.network.util.retryIO
import com.example.catsapp.core.model.Cat
import com.example.catsapp.core.network.retrofit.CatApi
import java.io.IOException
import javax.inject.Inject

class CatPagingSource @Inject constructor(
    private val catApi: CatApi
) : PagingSource<Int, Cat>() {

    override fun getRefreshKey(state: PagingState<Int, Cat>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cat> {
        val page = params.key ?: 1
        val loadSize = params.loadSize

        return try {
            val cats = retryIO {
                catApi.getCatImages(loadSize).map { it.toDomain() }
            }
            
            LoadResult.Page(
                data = cats,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (cats.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}