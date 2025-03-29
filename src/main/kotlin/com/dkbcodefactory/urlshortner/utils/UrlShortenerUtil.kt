package com.dkbcodefactory.urlshortner.utils

import io.seruco.encoding.base62.Base62
import java.security.MessageDigest

object UrlShortenerUtil {
    private val base62 = Base62.createInstance()

    fun generateShortUrl(originalUrl: String, length: Int = 7): String {
        val md5Value= MessageDigest.getInstance("MD5")
        val digest=md5Value.digest(originalUrl.toByteArray())
        val encodedBase62 = base62.encode(digest)
        val base62String = String(encodedBase62)
        return base62String.take(length)
    }
}