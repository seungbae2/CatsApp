package com.example.catsapp.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catsapp.core.database.entity.CatEntity

@Dao
interface CatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(cat: CatEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(cats: List<CatEntity>)

    @Query(
        """
        SELECT * FROM cats
        ORDER BY CASE WHEN :random = 1 THEN RANDOM() ELSE id END
        """
    )
    fun pagingSource(random: Boolean = true): PagingSource<Int, CatEntity>

    @Query("SELECT COUNT(*) FROM cats")
    suspend fun count(): Int

    @Query("DELETE FROM cats")
    suspend fun clearAll()
} 