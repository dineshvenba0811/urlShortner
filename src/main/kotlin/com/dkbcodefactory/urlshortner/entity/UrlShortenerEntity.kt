package com.dkbcodefactory.urlshortner.entity

import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table

@Table("url_mapping")
class UrlShortenerEntity(

    @PrimaryKey
    var shortUrl: String,

    var longUrl: String,

    var longUrlHash: String,
)