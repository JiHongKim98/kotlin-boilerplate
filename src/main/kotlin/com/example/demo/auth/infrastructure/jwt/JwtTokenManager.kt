package com.example.demo.auth.infrastructure.jwt

import com.example.demo.auth.application.TokenManager
import com.example.demo.auth.application.dto.TokenPairResponse
import com.example.demo.auth.domain.model.AuthToken
import com.example.demo.auth.exception.JwtNotExistClaimException
import com.example.demo.auth.exception.TokenExpiredException
import com.example.demo.auth.exception.TokenParseException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenManager(
    private val jwtProperties: JwtProperties,
) : TokenManager {

    private val parser: JwtParser = Jwts.parserBuilder().setSigningKey(jwtProperties.key).build()

    override fun generateTokenPair(authToken: AuthToken): TokenPairResponse {
        val accessToken = createToken(USER_ID, authToken.userId, jwtProperties.accessExp)
        val refreshToken = createToken(TOKEN_ID, authToken.tokenId, jwtProperties.refreshExp)
        return TokenPairResponse(accessToken, refreshToken)
    }

    override fun extractUserId(token: String): Long {
        val claims = parseClaimsBody(token)
        return claims[USER_ID] as? Long ?: throw JwtNotExistClaimException()
    }

    override fun extractTokenId(token: String): String {
        val claims = parseClaimsBody(token)
        return claims[TOKEN_ID] as? String ?: throw JwtNotExistClaimException()
    }

    private fun createToken(key: String, value: Any, expiredAt: Long): String {
        return Jwts.builder()
            .addClaims(mapOf(key to value))
            .setExpiration(Date(System.currentTimeMillis() + expiredAt))
            .signWith(jwtProperties.key)
            .compact()
    }

    private fun parseClaimsBody(token: String): Claims {
        return runCatching {
            parser.parseClaimsJws(token).body
        }.getOrElse { throwable ->
            when (throwable) {
                is ExpiredJwtException -> throw TokenExpiredException()
                else -> throw TokenParseException()
            }
        }
    }

    companion object {
        private const val USER_ID = "user_id"
        private const val TOKEN_ID = "token_id"
    }
}
