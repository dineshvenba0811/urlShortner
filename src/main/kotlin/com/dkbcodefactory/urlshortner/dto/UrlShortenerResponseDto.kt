package com.dkbcodefactory.urlshortner.dto

import java.time.LocalDate

data class UrlShortenerResponseDto(
    val originUrl: String,
    val shortUrl: String,
    val createdAt: LocalDate
)
