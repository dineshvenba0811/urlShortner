package com.dkbcodefactory.urlshortner.dto

data class UrlShortenerResponseDto(
    val shortUrl: String,
    val longUrl: String,
    val longUrlHash: String,
)
