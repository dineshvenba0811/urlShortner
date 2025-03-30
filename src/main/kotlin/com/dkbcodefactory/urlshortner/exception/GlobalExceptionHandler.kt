package com.dkbcodefactory.urlshortner.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UrlNotFoundException::class)
    fun handleUrlNotFoundException(ex: UrlNotFoundException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(mapOf("error" to (ex.message ?: "Short URL not found")))
    }

    @ExceptionHandler(UrlGenerationException::class)
    fun handleUrlGenerationException(ex: UrlGenerationException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("An error occurred while processing your request. Please try again later.")
    }

}