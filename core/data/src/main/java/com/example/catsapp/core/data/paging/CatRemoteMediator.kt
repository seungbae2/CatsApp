package com.example.catsapp.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.catsapp.core.data.util.ImageCacheHelper
import com.example.catsapp.core.database.dao.CatDao
import com.example.catsapp.core.database.entity.CatEntity
import com.example.catsapp.core.network.retrofit.CatApi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CatRemoteMediator @Inject constructor(
    private val api: CatApi,
    private val dao: CatDao,
    private val cache: ImageCacheHelper,
) : RemoteMediator<Int, CatEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CatEntity>,
    ): MediatorResult {

        if (loadType == LoadType.PREPEND) {
            return MediatorResult.Success(endOfPaginationReached = true)
        }

        return try {
            val pageSize = state.config.pageSize
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

            MediatorResult.Success(endOfPaginationReached = remote.isEmpty())
        } catch (io: IOException) {
            MediatorResult.Error(io)
        } catch (http: HttpException) {
            MediatorResult.Error(http)
        }
    }
}