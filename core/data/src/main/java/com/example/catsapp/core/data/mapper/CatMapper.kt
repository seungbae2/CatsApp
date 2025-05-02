package com.example.catsapp.core.data.mapper

import com.example.catsapp.core.database.entity.CatEntity
import com.example.catsapp.core.model.Cat
import com.example.catsapp.core.network.response.CatResponse

fun CatEntity.toDomain(): Cat =
    Cat(
        id = id,
        remoteUrl = url,
        localPath = filePath
    )

fun CatResponse.toDomain(filePath: String?): Cat =
    Cat(
        id = id,
        remoteUrl = url,
        localPath = filePath
    )

fun CatResponse.toEntity(filePath: String?): CatEntity =
    CatEntity(
        id = id,
        url = url,
        filePath = filePath,
        savedAt = System.currentTimeMillis()
    )