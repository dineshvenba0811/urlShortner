package com.dkbcodefactory.urlshortner.controller

import com.dkbcodefactory.urlshortner.dto.UrlShortenerRequestDto
import com.dkbcodefactory.urlshortner.dto.UrlShortenerResponseDto
import com.dkbcodefactory.urlshortner.service.RedirectionService
import com.dkbcodefactory.urlshortner.service.ShortUrlGeneratorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api")
class UrlController(
    private val generationService: ShortUrlGeneratorService,
    private val redirectionService: RedirectionService
) {
    /**
     * Creates a shortened URL for the provided long URL.
     *
     * @param requestDto the URL shortening request payload containing the long URL.
     * @return a [ResponseEntity] with the shortened URL response.
     */
    @PostMapping("/shorten")
    fun shorten(@RequestBody requestDto: UrlShortenerRequestDto): ResponseEntity<UrlShortenerResponseDto> {
        val responseDto = generationService.getShortUrl(requestDto)
        return ResponseEntity.ok(responseDto)
    }

    /**
     * Redirects to the original long URL based on the provided short URL key.
     *
     * @param shortUrlKey the unique key of the short URL (without the domain prefix).
     * @return a [ResponseEntity] with a 301 status and Location header pointing to the original long URL.
     */
    @GetMapping("/{shortUrlKey}")
    fun redirect(@PathVariable shortUrlKey: String): ResponseEntity<Void> {
        val urlMapping = redirectionService.getOriginalUrl(shortUrlKey)
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
            .location(URI.create(urlMapping.longUrl))
            .build()
    }

}