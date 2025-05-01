package com.example.catsapp.core.model

import java.io.File

data class Cat(
    val id: String,
    val remoteUrl: String,
    val localPath: String? = null,
) {
    val imageModel: Any
        get() = localPath?.let(::File) ?: remoteUrl     // File or String
}