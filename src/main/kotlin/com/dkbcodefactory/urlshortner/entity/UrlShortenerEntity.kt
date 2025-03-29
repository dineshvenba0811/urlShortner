package com.dkbcodefactory.urlshortner.entity

import org.springframework.data.cassandra.core.mapping.PrimaryKey
import org.springframework.data.cassandra.core.mapping.Table
import java.time.LocalDate

@Table("url_mapping")
class UrlShortenerEntity (

    @PrimaryKey
    var shortUrl: String,

    var originUrl: String,

    var createdAt: LocalDate

)