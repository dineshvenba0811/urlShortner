package com.dkbcodefactory.urlshortner.integration

import com.dkbcodefactory.urlshortner.entity.UrlShortenerEntity
import com.dkbcodefactory.urlshortner.repository.UrlShortenRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.CassandraContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension::class)
class CassandraIntegrationTest {

    companion object {
        // Define a Testcontainers Cassandra instance.
        @Container
        val cassandraContainer = CassandraContainer<Nothing>("cassandra:3.11.2").apply {
            withExposedPorts(9042)
        }

        @JvmStatic
        @DynamicPropertySource
        fun cassandraProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.cassandra.contact-points") { cassandraContainer.containerIpAddress }
            registry.add("spring.data.cassandra.port") { cassandraContainer.getMappedPort(9042) }
        }
    }

    @Autowired
    lateinit var urlShortenRepository: UrlShortenRepository

    @Test
    fun `should save and retrieve UrlShortenerEntity from Cassandra`() {
        // Arrange
        val entity = UrlShortenerEntity(
            shortUrl = "test123",
            longUrl = "https://www.example.com/some/very/long/url",
            longUrlHash = "dummyhash123"
        )

        // Act
        urlShortenRepository.save(entity)

        // Then
        val retrieved = urlShortenRepository.findById("test123")
        assertThat(retrieved).isPresent
        retrieved.ifPresent { result ->
            assertThat(result.shortUrl).isEqualTo("test123")
            assertThat(result.longUrl).isEqualTo("https://www.example.com/some/very/long/url")
            assertThat(result.longUrlHash).isEqualTo("dummyhash123")
        }
    }
}