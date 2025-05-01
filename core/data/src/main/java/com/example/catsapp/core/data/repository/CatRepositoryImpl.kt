package com.example.catsapp.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.catsapp.core.common.NetworkWatcher
import com.example.catsapp.core.data.mapper.toDomain
import com.example.catsapp.core.data.paging.CatRemoteMediator
import com.example.catsapp.core.data.util.ImageCacheHelper
import com.example.catsapp.core.data_api.repository.CatRepository
import com.example.catsapp.core.database.dao.CatDao
import com.example.catsapp.core.model.Cat
import com.example.catsapp.core.network.retrofit.CatApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CatRepositoryImpl @Inject constructor(
    private val api: CatApi,
    private val dao: CatDao,
    private val imageCacheHelper: ImageCacheHelper,
    private val networkWatcher: NetworkWatcher,
) : CatRepository {

    override fun getCatsPaging(pageSize: Int): Flow<PagingData<Cat>> =
        Pager(
            config = PagingConfig(pageSize = pageSize),
            remoteMediator = CatRemoteMediator(
                api = api,
                dao = dao,
                cache = imageCacheHelper,
                networkWatcher = networkWatcher,
                pageSize = pageSize
            ),
            pagingSourceFactory = { dao.pagingSource() }
        )
            .flow
            .map { paging -> paging.map { it.toDomain() } }
}