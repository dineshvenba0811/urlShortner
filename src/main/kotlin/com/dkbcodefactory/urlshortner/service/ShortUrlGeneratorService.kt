package com.dkbcodefactory.urlshortner.service

import com.dkbcodefactory.urlshortner.configuration.ShortUrlProperties
import com.dkbcodefactory.urlshortner.mapper.UrlShortenerMapper
import com.dkbcodefactory.urlshortner.dto.UrlShortenerRequestDto
import com.dkbcodefactory.urlshortner.dto.UrlShortenerResponseDto
import com.dkbcodefactory.urlshortner.entity.UrlLookUpEntity
import com.dkbcodefactory.urlshortner.exception.UrlGenerationException
import com.dkbcodefactory.urlshortner.mapper.UrlLookupMapper
import com.dkbcodefactory.urlshortner.repository.UrlLookUpRepository
import com.dkbcodefactory.urlshortner.utils.UrlShortenerUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.cassandra.core.CassandraOperations
import org.springframework.stereotype.Service

@Service
class ShortUrlGeneratorService(
    private val urlLookUpRepository: UrlLookUpRepository,
    private val cassandraTemplate: CassandraOperations,
    private val shortUrlProperties: ShortUrlProperties
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ShortUrlGeneratorService::class.java)
    }

    /**
     * Processes the URL shortening request.
     *
     * If the long URL has already been shortened, it returns the existing short URL.
     * Otherwise, it generates and stores a new short URL mapping.
     *
     * @param request The URL shortening request containing the long URL.
     * @return A DTO containing the short URL (with configured prefix), long url and long url hash.
     */

    fun getShortUrl(request: UrlShortenerRequestDto): UrlShortenerResponseDto {
        val originalLongUrl = request.longUrl
        val originalLongUrlHash = UrlShortenerUtil.generateMd5Hash(originalLongUrl)
        // Check for existing mapping using the MD5 hash as the key.
        return urlLookUpRepository.findById(originalLongUrlHash)
            .map { lookUpEntity -> buildExistingShortUrl(lookUpEntity, originalLongUrl) }
            .orElseGet { createAndPersistShortUrl(originalLongUrl, originalLongUrlHash) }
    }

    /**
     * Builds the response DTO for an already existing URL mapping.
     *
     * @param lookupEntity The lookup entity retrieved from the database.
     * @param originalLongUrl The original long URL.
     * @return The response DTO with the short URL including the configured prefix.
     */
    private fun buildExistingShortUrl(
        lookupEntity: UrlLookUpEntity,
        originalLongUrl: String
    ): UrlShortenerResponseDto {
        return UrlShortenerMapper.toDTO(
            lookupEntity.shortUrl,
            lookupEntity.longUrlHash,
            originalLongUrl,
            shortUrlProperties.prefix
        )
    }

    /**
     * Generates a new short URL mapping and saves it using a logged batch to ensure both the mapping
     * and lookup entities are inserted atomically.
     *
     * @param originalLongUrl The original long URL.
     * @param originalLongUrlHash The MD5 hash of the long URL.
     * @return The response DTO with the newly created short URL.
     * @throws UrlGenerationException If the database operations fail.
     */
    private fun createAndPersistShortUrl(
        originalLongUrl: String,
        originalLongUrlHash: String
    ): UrlShortenerResponseDto {

        val shortCode = UrlShortenerUtil.generateShortUrl(originalLongUrl)
        val entity = UrlShortenerMapper.toEntity(originalLongUrl, shortCode, originalLongUrlHash)
        val lookUpEntity = UrlLookupMapper.toEntity(shortCode, originalLongUrlHash)

        try {
            // Group both inserts into a logged batch for atomicity.
            val batchOps = cassandraTemplate.batchOps()
            batchOps.insert(entity)
            batchOps.insert(lookUpEntity)
            batchOps.execute()

            return UrlShortenerMapper.toDTOFromEntity(entity, shortUrlProperties.prefix)
        } catch (ex: Exception) {
            logger.error("Error inserting URL mapping for long URL: $originalLongUrl", ex)
            throw UrlGenerationException("Failed to create URL mapping for: $originalLongUrl")
        }
    }


}