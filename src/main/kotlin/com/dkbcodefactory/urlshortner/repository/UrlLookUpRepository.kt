package com.dkbcodefactory.urlshortner.repository

import com.dkbcodefactory.urlshortner.entity.UrlLookUpEntity
import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository

@Repository
interface UrlLookUpRepository : CassandraRepository<UrlLookUpEntity, String> {
}