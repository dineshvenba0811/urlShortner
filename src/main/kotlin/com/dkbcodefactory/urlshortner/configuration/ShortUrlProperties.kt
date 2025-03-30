package com.dkbcodefactory.urlshortner.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "short.url")
class ShortUrlProperties {
    lateinit var prefix: String
}