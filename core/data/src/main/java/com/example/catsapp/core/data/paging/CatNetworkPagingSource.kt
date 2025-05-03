package com.example.catsapp.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.catsapp.core.common.NetworkWatcher
import com.example.catsapp.core.data.mapper.toDomain
import com.example.catsapp.core.data.util.ImageCacheHelper
import com.example.catsapp.core.database.dao.CatDao
import com.example.catsapp.core.database.entity.CatEntity
import com.example.catsapp.core.model.Cat
import com.example.catsapp.core.network.retrofit.CatApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CatNetworkPagingSource @Inject constructor(
    private val catApi: CatApi,
    private val catDao: CatDao,
    private val imageCache: ImageCacheHelper,
    private val networkWatcher: NetworkWatcher,
) : PagingSource<Int, Cat>() {

    override fun getRefreshKey(state: PagingState<Int, Cat>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Cat> {
        val page = params.key ?: 1
        val loadSize = params.loadSize

        val isOnline = networkWatcher.isOnline()

        if (!isOnline) {
            val totalCount = withContext(Dispatchers.IO) { catDao.count() }
            val totalPages = kotlin.math.ceil(totalCount / loadSize.toDouble()).toInt()

            // 페이지가 마지막을 넘으면, 오프라인 한정 에러를 던짐
            if (page > totalPages) {
                return LoadResult.Error(
                    IllegalStateException("오프라인 모드: 더 이상 불러올 캐시가 없습니다.")
                )
            }
        }

        return try {
            val catsResponse = catApi.getCatImages(loadSize)

            val entities = catsResponse.map { dto ->
                val path = imageCache.cache(dto.id, dto.url).path
                CatEntity(
                    id = dto.id,
                    url = dto.url,
                    filePath = path,
                    savedAt = System.currentTimeMillis()
                )
            }

            // 캐시 및 DB 저장
            catDao.upsertAll(entities)

            val domainModels = entities.map { it.toDomain() }

            LoadResult.Page(
                data = domainModels,
                prevKey = null,
                nextKey = (params.key ?: 0) + 1
            )
        } catch (e: IOException) {
            // 네트워크 연결 문제나 타임아웃일 경우 → 로컬 DB fallback
            val offset = (page - 1) * loadSize
            val fallback = withContext(Dispatchers.IO) {
                catDao.getRandomCatsPaginated(limit = loadSize, offset = offset)
            }
            LoadResult.Page(
                data = fallback.map { it.toDomain() },
                prevKey = null,
                nextKey = (params.key ?: 0) + 1
            )
        } catch (e: HttpException) {
            return when (e.code()) {
                in 500..599 -> {
                    // 서버 오류 → fallback
                    val offset = (page - 1) * loadSize
                    val fallback = withContext(Dispatchers.IO) {
                        catDao.getRandomCatsPaginated(limit = loadSize, offset = offset)
                    }
                    LoadResult.Page(
                        data = fallback.map { it.toDomain() },
                        prevKey = null,
                        nextKey = (params.key ?: 0) + 1
                    )
                }

                else -> {
                    LoadResult.Error(e)
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}