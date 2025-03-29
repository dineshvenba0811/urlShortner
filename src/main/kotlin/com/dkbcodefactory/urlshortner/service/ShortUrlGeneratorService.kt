package com.dkbcodefactory.urlshortner.service

import com.dkbcodefactory.urlshortner.mapper.UrlShortenerMapper
import com.dkbcodefactory.urlshortner.dto.UrlShortenerRequestDto
import com.dkbcodefactory.urlshortner.dto.UrlShortenerResponseDto
import com.dkbcodefactory.urlshortner.repository.UrlShortenRepository
import com.dkbcodefactory.urlshortner.utils.UrlShortenerUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ShortUrlGeneratorService (private val urlShortenRepository: UrlShortenRepository){

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ShortUrlGeneratorService::class.java)
    }

    fun getShortUrl(urlShortenerRequestDto: UrlShortenerRequestDto): UrlShortenerResponseDto {
        // Generate a short URL from the original URL
        val shortcode=UrlShortenerUtil.generateShortUrl(urlShortenerRequestDto.originUrl)
        // Map request DTO to entity
        val entity= UrlShortenerMapper.toEntity(urlShortenerRequestDto, shortcode)
        // Save the entity to the database
        val savedEntity=urlShortenRepository.save(entity)
        // Convert the saved entity back to response DTO
        return UrlShortenerMapper.toDTO(savedEntity)
    }


}