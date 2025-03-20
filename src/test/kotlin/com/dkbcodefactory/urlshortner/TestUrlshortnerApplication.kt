package com.dkbcodefactory.urlshortner

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<UrlshortnerApplication>().with(TestcontainersConfiguration::class).run(*args)
}
