package com.dkbcodefactory.urlshortner.controller

import com.dkbcodefactory.urlshortner.dto.UrlShortenerRequestDto
import com.dkbcodefactory.urlshortner.dto.UrlShortenerResponseDto
import com.dkbcodefactory.urlshortner.service.ShortUrlGeneratorService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class UrlController(private val service:ShortUrlGeneratorService) {

    @PostMapping("/shorten")
    fun shorten(@RequestBody urlShortenerRequestDto: UrlShortenerRequestDto): UrlShortenerResponseDto {
        return service.getShortUrl(urlShortenerRequestDto);
    }

}