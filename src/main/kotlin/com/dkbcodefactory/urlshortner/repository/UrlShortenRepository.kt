package com.dkbcodefactory.urlshortner.repository

import com.dkbcodefactory.urlshortner.entity.UrlShortenerEntity
import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UrlShortenRepository:CassandraRepository<UrlShortenerEntity,UUID> {
}