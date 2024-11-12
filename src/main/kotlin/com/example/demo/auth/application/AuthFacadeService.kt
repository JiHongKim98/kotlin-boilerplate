package com.example.demo.auth.application

import com.example.demo.auth.application.dto.TokenPairResponse
import org.springframework.stereotype.Service

@Service
class AuthFacadeService(
    private val tokenManager: TokenManager,
    private val authTokenService: AuthTokenService,
) {

    fun reissueToken(refreshToken: String): TokenPairResponse {
        val tokenId = tokenManager.extractTokenId(refreshToken)
        val authToken = authTokenService.regenerateAuthToken(tokenId)
        return tokenManager.generateTokenPair(authToken)
    }

    fun deleteToken(userId: Long, refreshToken: String) {
        val tokenId = tokenManager.extractTokenId(refreshToken)
        authTokenService.removeTokenIfMatchUserId(tokenId, userId)
    }
}
