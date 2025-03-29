package com.dkbcodefactory.urlshortner.mapper

import com.dkbcodefactory.urlshortner.dto.UrlShortenerRequestDto
import com.dkbcodefactory.urlshortner.dto.UrlShortenerResponseDto
import com.dkbcodefactory.urlshortner.entity.UrlShortenerEntity
import java.time.LocalDate

object UrlShortenerMapper {

    // DTO to Entity
    fun toEntity(dto: UrlShortenerRequestDto, shortUrl: String): UrlShortenerEntity {
        return UrlShortenerEntity(
            shortUrl = shortUrl,
            originUrl = dto.originUrl,
            createdAt = LocalDate.now(),
        )
    }

    fun toDTO(entity: UrlShortenerEntity): UrlShortenerResponseDto {
        return UrlShortenerResponseDto(
            shortUrl = entity.shortUrl,
            originUrl = entity.originUrl,
            createdAt = entity.createdAt,
        )
    }

}