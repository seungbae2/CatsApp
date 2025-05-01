package com.example.catsapp.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cats")
data class CatEntity(
    @PrimaryKey val id: String,
    val url: String,
    val filePath: String?,
    val savedAt: Long,
)