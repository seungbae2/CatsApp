package com.example.catsapp.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
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
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (entities.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            // 네트워크 연결 문제나 타임아웃일 경우 → 로컬 DB fallback
            val offset = (page - 1) * loadSize
            val fallback = withContext(Dispatchers.IO) {
                catDao.getRandomCatsPaginated(limit = loadSize, offset = offset)
            }
            LoadResult.Page(
                data = fallback.map { it.toDomain() },
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (fallback.isEmpty()) null else page + 1
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
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (fallback.isEmpty()) null else page + 1
                    )
                }

                else -> {
                    // 클라이언트 오류나 기타 예외 → 실패로 처리
                    LoadResult.Error(e)
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}