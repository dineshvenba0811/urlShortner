package com.dkbcodefactory.urlshortner.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
class UrlShortnerEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var  id: Long?=null,

    @Column(nullable = false, unique = true)
    var shortUrl: String,

    @Column(nullable = false, unique = true,length = 2048)
    var originUrl: String,

    @Column(nullable = false)
    var createdAt: Instant = Instant.now()

)