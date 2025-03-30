package com.dkbcodefactory.urlshortner.mapper

import com.dkbcodefactory.urlshortner.entity.UrlLookUpEntity

object UrlLookupMapper {
    // DTO to Entity
    fun toEntity(shortUrl: String, hashLongUrl: String): UrlLookUpEntity {
        return UrlLookUpEntity(
            shortUrl = shortUrl,
            longUrlHash = hashLongUrl,
        )
    }
}