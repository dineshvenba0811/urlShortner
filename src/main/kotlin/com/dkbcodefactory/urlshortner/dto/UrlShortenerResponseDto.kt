package com.dkbcodefactory.urlshortner.dto

import java.time.LocalDate

data class UrlShortenerResponseDto(
    val originUrl: String,
    var shortUrl: String,
    var createdAt: LocalDate
)
