package com.example.catsapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.catsapp.core.database.dao.CatDao
import com.example.catsapp.core.database.entity.CatEntity

@Database(
    entities = [
        CatEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class CatDatabase : RoomDatabase() {
    abstract fun catDao(): CatDao
}