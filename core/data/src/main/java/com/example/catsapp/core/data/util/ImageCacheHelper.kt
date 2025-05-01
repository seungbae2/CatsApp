package com.example.catsapp.core.data.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageCacheHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient,
) {

    private val cacheDir: File by lazy {
        File(context.filesDir, "image_cache").apply { mkdirs() }
    }

    suspend fun cache(id: String, url: String): File = withContext(Dispatchers.IO) {
        val dstFile = File(cacheDir, "$id.jpg")
        val tmpFile = File(cacheDir, "$id.tmp")

        if (dstFile.exists()) return@withContext dstFile
        if (tmpFile.exists()) tmpFile.delete()

        try {
            val req = Request.Builder().url(url).build()
            okHttpClient.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) throw IOException("HTTP error ${resp.code} downloading $url")

                resp.body?.byteStream()?.use { input ->
                    tmpFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                } ?: throw IOException("Empty response body downloading $url")
            }

            if (!tmpFile.renameTo(dstFile)) {
                tmpFile.copyTo(dstFile, overwrite = true)
                tmpFile.delete()
            }

            return@withContext dstFile
        } catch (e: Exception) {
            if (tmpFile.exists()) tmpFile.delete()
            throw IOException("Failed to download or cache image $id from $url", e)
        }
    }

    fun getCachedFile(id: String): File? {
        val f = File(cacheDir, "$id.jpg")
        return if (f.exists()) f else null
    }

    suspend fun trimCache(maxAgeDays: Int = 30) = withContext(Dispatchers.IO) {
        try {
            val expireTimeMillis =
                System.currentTimeMillis() - TimeUnit.DAYS.toMillis(maxAgeDays.toLong())
            cacheDir.listFiles()?.forEach { file ->
                if (file.isFile && file.lastModified() < expireTimeMillis) {
                    file.delete()
                }
            }
        } catch (e: Exception) {
            Timber.e("Error trimming cache: ${e.message}")
        }
    }
}