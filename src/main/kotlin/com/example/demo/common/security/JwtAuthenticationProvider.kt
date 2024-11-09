package com.example.demo.common.security

import com.example.demo.auth.application.TokenManager
import com.example.demo.common.exception.CustomException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider(
    private val tokenManager: TokenManager,
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val jwtAuthenticationToken = authentication as JwtAuthenticationToken
        val accessToken = jwtAuthenticationToken.getAccessToken()
        val userId = extractUserIdByAccessToken(accessToken)
        return JwtAuthenticationToken.afterOf(userId)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return JwtAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    private fun extractUserIdByAccessToken(accessToken: String): Long {
        return try {
            tokenManager.extractUserId(accessToken)
        } catch (e: CustomException) {
            throw SecurityException(e.message, cause = e)
        }
    }
}
