package UnitTesting

import com.dkbcodefactory.urlshortner.configuration.ShortUrlProperties
import com.dkbcodefactory.urlshortner.dto.UrlShortenerRequestDto
import com.dkbcodefactory.urlshortner.dto.UrlShortenerResponseDto
import com.dkbcodefactory.urlshortner.entity.UrlLookUpEntity
import com.dkbcodefactory.urlshortner.entity.UrlShortenerEntity
import com.dkbcodefactory.urlshortner.exception.UrlGenerationException
import com.dkbcodefactory.urlshortner.repository.UrlLookUpRepository
import com.dkbcodefactory.urlshortner.service.ShortUrlGeneratorService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.data.cassandra.core.CassandraBatchOperations
import org.springframework.data.cassandra.core.CassandraOperations
import java.util.*

class ShortUrlGeneratorServiceTest {
    private lateinit var urlLookUpRepository: UrlLookUpRepository
    private lateinit var cassandraTemplate: CassandraOperations
    private lateinit var shortUrlProperties: ShortUrlProperties
    private lateinit var shortUrlGeneratorService: ShortUrlGeneratorService
    private lateinit var batchOps: CassandraBatchOperations

    @BeforeEach
    fun setUp() {
        urlLookUpRepository = mock(UrlLookUpRepository::class.java)
        cassandraTemplate = mock(CassandraOperations::class.java)
        shortUrlProperties = ShortUrlProperties().apply { prefix = "https://short.url.com/" }
        batchOps = mock(CassandraBatchOperations::class.java)
        `when`(cassandraTemplate.batchOps()).thenReturn(batchOps)
        shortUrlGeneratorService = ShortUrlGeneratorService(urlLookUpRepository, cassandraTemplate, shortUrlProperties)
    }

    /**
     * Test that when an existing lookup is found, the service returns the existing DTO.
     */
    @Test
    fun `getShortUrl returns existing DTO when mapping exists`() {
        // Given
        val originalLongUrl = "https://www.example.com/very/long/url"
        val originalLongUrlHash = "1519a5f05f1bba57ab05256ebafb264a"
        val shortCode = "doYj7oQ"
        val lookupEntity = UrlLookUpEntity(shortUrl = shortCode, longUrlHash = originalLongUrlHash)
        val request = UrlShortenerRequestDto(longUrl = originalLongUrl)

        `when`(urlLookUpRepository.findById(originalLongUrlHash)).thenReturn(Optional.of(lookupEntity))

        // When
        val response: UrlShortenerResponseDto = shortUrlGeneratorService.getShortUrl(request)

        // Then
        assertEquals(shortUrlProperties.prefix + shortCode, response.shortUrl)
        assertEquals(originalLongUrl, response.longUrl)
        assertEquals(originalLongUrlHash, response.longUrlHash)
    }

    /**
     * Test that when no existing mapping is found, a new mapping is created successfully.
     */
    @Test
    fun `getShortUrl creates new mapping when none exists`() {
        // Given
        val originalLongUrl = "https://www.example.com/another/long/url"
        val originalLongUrlHash = "89417810432fc7009714c829170fc658"
        val shortCode = "4AzrpPS"
        val request = UrlShortenerRequestDto(longUrl = originalLongUrl)
        `when`(urlLookUpRepository.findById(originalLongUrlHash)).thenReturn(Optional.empty())

        `when`(batchOps.insert(any(UrlShortenerEntity::class.java))).thenReturn(batchOps)
        `when`(batchOps.insert(any(UrlLookUpEntity::class.java))).thenReturn(batchOps)

        // When
        val response: UrlShortenerResponseDto = shortUrlGeneratorService.getShortUrl(request)

        // Then:
        assertEquals(shortUrlProperties.prefix + shortCode, response.shortUrl)
        assertEquals(originalLongUrl, response.longUrl)
        assertEquals(originalLongUrlHash, response.longUrlHash)

        verify(batchOps, times(1)).insert(any(UrlShortenerEntity::class.java))
        verify(batchOps, times(1)).insert(any(UrlLookUpEntity::class.java))
    }

    @Test
    fun `getShortUrl throws UrlGenerationException when batch execution fails`() {
        // Given
        val originalLongUrl = "https://www.example.com/error/url"
        val originalLongUrlHash = "hashError"
        val request = UrlShortenerRequestDto(longUrl = originalLongUrl)

        `when`(urlLookUpRepository.findById(originalLongUrlHash)).thenReturn(Optional.empty())
        doThrow(RuntimeException("Database error")).`when`(batchOps).execute()

        val exception = assertThrows(UrlGenerationException::class.java) {
            shortUrlGeneratorService.getShortUrl(request)
        }
        assertTrue(exception.message!!.contains("Failed to create URL mapping for: $originalLongUrl"))
    }

}