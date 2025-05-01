package com.example.catsapp.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.catsapp.core.common.NetworkWatcher
import com.example.catsapp.core.data.util.ImageCacheHelper
import com.example.catsapp.core.database.dao.CatDao
import com.example.catsapp.core.database.entity.CatEntity
import com.example.catsapp.core.network.retrofit.CatApi
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CatRemoteMediator @Inject constructor(
    private val api: CatApi,
    private val dao: CatDao,
    private val cache: ImageCacheHelper,
    private val networkWatcher: NetworkWatcher,
    private val pageSize: Int = 10,
) : RemoteMediator<Int, CatEntity>() {

    private var currentPage: Int = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CatEntity>,
    ): MediatorResult {

        if (!networkWatcher.isOnline()) {
            return MediatorResult.Success(endOfPaginationReached = false)
        }

        val page = when (loadType) {
            LoadType.REFRESH -> 0.also { currentPage = 0 }
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = false)
            LoadType.APPEND -> currentPage + 1
        }

        return try {
            val remote = api.getCatImages(limit = pageSize)

            val entities = remote.map { dto ->
                val path = cache.cache(dto.id, dto.url).path
                CatEntity(
                    id = dto.id,
                    url = dto.url,
                    filePath = path,
                    savedAt = System.currentTimeMillis()
                )
            }
            dao.upsertAll(entities)

            currentPage = page
            MediatorResult.Success(endOfPaginationReached = remote.isEmpty())
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}