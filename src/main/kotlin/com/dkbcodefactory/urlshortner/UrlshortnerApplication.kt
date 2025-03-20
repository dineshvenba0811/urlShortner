package com.dkbcodefactory.urlshortner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UrlshortnerApplication

fun main(args: Array<String>) {
	runApplication<UrlshortnerApplication>(*args)
}
