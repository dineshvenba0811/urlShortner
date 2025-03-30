package com.dkbcodefactory.urlshortner.mapper

import com.dkbcodefactory.urlshortner.dto.UrlShortenerRequestDto
import com.dkbcodefactory.urlshortner.dto.UrlShortenerResponseDto
import com.dkbcodefactory.urlshortner.entity.UrlShortenerEntity
import org.springframework.beans.factory.annotation.Value
import java.time.LocalDate

object UrlShortenerMapper {

    // DTO to Entity
    fun toEntity(originalLongUrl: String, shortUrl: String, hashLongUrl: String): UrlShortenerEntity {
        return UrlShortenerEntity(
            shortUrl = shortUrl,
            longUrl = originalLongUrl,
            longUrlHash = hashLongUrl,
        )
    }

    fun toDTOFromEntity(savedEntity: UrlShortenerEntity, prefix: String): UrlShortenerResponseDto {
        return UrlShortenerResponseDto(
            shortUrl = prefix + savedEntity.shortUrl,
            longUrl = savedEntity.longUrl,
            longUrlHash = savedEntity.longUrlHash,
        )
    }

    fun toDTO(shortUrl: String, longUrlHash: String, originalLongUrl: String, prefix: String): UrlShortenerResponseDto {
        return UrlShortenerResponseDto(
            shortUrl = prefix + shortUrl,
            longUrl = originalLongUrl,
            longUrlHash = longUrlHash,
        )
    }

}