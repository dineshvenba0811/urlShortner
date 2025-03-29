package com.dkbcodefactory.urlshortner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories

@SpringBootApplication (exclude = [DataSourceAutoConfiguration::class])
@EnableCassandraRepositories(basePackages = ["com.dkbcodefactory.urlshortner.repository"])
class UrlShorterApplication

fun main(args: Array<String>) {
	runApplication<UrlShorterApplication>(*args)
}
