package com.example.demo.common.presentation.cookie

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.cookie")
data class CookieProperties(
    val path: String,
    val maxAge: Long,
    val domain: String,
    val sameSite: String,
    val secure: Boolean,
    val httpOnly: Boolean,
)
