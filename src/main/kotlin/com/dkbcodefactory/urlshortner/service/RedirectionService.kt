package com.dkbcodefactory.urlshortner.service

import com.dkbcodefactory.urlshortner.configuration.ShortUrlProperties
import com.dkbcodefactory.urlshortner.dto.UrlShortenerResponseDto
import com.dkbcodefactory.urlshortner.exception.UrlNotFoundException
import com.dkbcodefactory.urlshortner.mapper.UrlShortenerMapper
import com.dkbcodefactory.urlshortner.repository.UrlShortenRepository
import org.springframework.stereotype.Service
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Service
class RedirectionService(
    private val urlShortenRepository: UrlShortenRepository,
    private val shortUrlProperties: ShortUrlProperties
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(RedirectionService::class.java)
    }

    /**
     * Retrieves the original long URL details corresponding to the provided short URL key.
     *
     * This method looks up the short URL in the repository. If the mapping exists,
     * it converts the entity into a DTO with the appropriate prefix; otherwise, it throws a [UrlNotFoundException].
     *
     * @param shortUrl the unique short URL key (without prefix).
     * @return a [UrlShortenerResponseDto] containing the original long URL and related data.
     * @throws UrlNotFoundException if no mapping is found for the provided short URL.
     */
    fun getOriginalUrl(shortUrl: String): UrlShortenerResponseDto {
        val longUrlEntity = urlShortenRepository.findById(shortUrl)
            .orElseThrow {
                logger.warn("Short URL '{}' not found", shortUrl)
                UrlNotFoundException(" Short Url $shortUrl not found")
            }
        return UrlShortenerMapper.toDTOFromEntity(longUrlEntity, shortUrlProperties.prefix)
    }

}