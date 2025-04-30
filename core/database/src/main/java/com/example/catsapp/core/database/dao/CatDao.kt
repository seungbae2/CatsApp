package com.example.catsapp.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.catsapp.core.database.entity.CatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatDao {
    @Query("SELECT * FROM cats ORDER BY CASE WHEN :isOffline = 1 THEN RANDOM() ELSE id END")
    fun getCatsFlow(isOffline: Boolean = false): Flow<List<CatEntity>>
} 