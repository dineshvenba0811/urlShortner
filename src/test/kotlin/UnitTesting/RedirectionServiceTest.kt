package UnitTesting

import com.dkbcodefactory.urlshortner.configuration.ShortUrlProperties
import com.dkbcodefactory.urlshortner.dto.UrlShortenerResponseDto
import com.dkbcodefactory.urlshortner.entity.UrlShortenerEntity
import com.dkbcodefactory.urlshortner.exception.UrlNotFoundException
import com.dkbcodefactory.urlshortner.repository.UrlShortenRepository
import com.dkbcodefactory.urlshortner.service.RedirectionService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import java.util.*

class RedirectionServiceTest {

    private lateinit var urlShortenRepository: UrlShortenRepository
    private lateinit var shortUrlProperties: ShortUrlProperties
    private lateinit var redirectionService: RedirectionService

    @BeforeEach
    fun setUp() {
        urlShortenRepository = mock(UrlShortenRepository::class.java)
        shortUrlProperties = ShortUrlProperties().apply {
            prefix = "https://short.url.com/"
        }
        redirectionService = RedirectionService(urlShortenRepository, shortUrlProperties)
    }
    @Test
    fun `getOriginalUrl should throw UrlNotFoundException when entity is not found`() {
        // Given
        val shortUrlKey = "nonexistent"
        `when`(urlShortenRepository.findById(shortUrlKey))
            .thenReturn(Optional.empty())

        // When & Then
        val exception = assertThrows(UrlNotFoundException::class.java) {
            redirectionService.getOriginalUrl(shortUrlKey)
        }
        assertEquals(" Short Url $shortUrlKey not found", exception.message)
    }

    @Test
    fun `getOriginalUrl should return DTO when entity is found`() {
        // Given
        val shortUrlKey = "abc123"
        val entity = UrlShortenerEntity(
            shortUrl = shortUrlKey,
            longUrl = "https://www.example.com/some/very/long/url",
            longUrlHash = "dummyhash"
        )

        `when`(urlShortenRepository.findById(shortUrlKey))
            .thenReturn(Optional.of(entity))

        // When
        val result: UrlShortenerResponseDto = redirectionService.getOriginalUrl(shortUrlKey)

        // Then: Expect the short URL to include the prefix.
        val expectedShortUrl = shortUrlProperties.prefix + entity.shortUrl
        assertEquals(expectedShortUrl, result.shortUrl)
        assertEquals(entity.longUrl, result.longUrl)
        assertEquals(entity.longUrlHash, result.longUrlHash)
    }
}