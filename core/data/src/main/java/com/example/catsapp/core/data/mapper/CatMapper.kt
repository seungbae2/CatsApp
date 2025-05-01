package com.example.catsapp.core.data.mapper

import com.example.catsapp.core.database.entity.CatEntity
import com.example.catsapp.core.model.Cat
import com.example.catsapp.core.network.response.CatResponse

fun CatEntity.toDomain(): Cat =
    Cat(
        id = id,
        url = url,
        width = width,
        height = height,
        path = path,
    )

fun Cat.toEntity(): CatEntity =
    CatEntity(
        id = id,
        url = url,
        width = width,
        height = height,
        path = path,
    )

fun CatResponse.toDomain(): Cat =
    Cat(
        id = id,
        url = url,
        width = width,
        height = height,
        path = ""
    )