package com.example.demo.auth.infrastructure.jwt

import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties
import java.security.Key

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val accessExp: Long,
    val refreshExp: Long,
    private val secretKey: String,
) {

    val key: Key = Keys.hmacShaKeyFor(secretKey.toByteArray())
}
