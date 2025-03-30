package com.dkbcodefactory.urlshortner.entity

import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table

@Table("url_lookup")
class UrlLookUpEntity(
    @PrimaryKey
    val longUrlHash: String,
    val shortUrl: String
)