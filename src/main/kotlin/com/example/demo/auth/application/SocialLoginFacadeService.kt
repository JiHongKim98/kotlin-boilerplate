package com.example.demo.auth.application

import com.example.demo.auth.application.dto.TokenPairResponse
import org.springframework.stereotype.Service

@Service
class SocialLoginFacadeService(
    private val tokenManager: TokenManager,
    private val authUserService: AuthUserService,
    private val authTokenService: AuthTokenService,
) {

    fun loginOrCreateAuthUser(socialId: String, stringSocialType: String): TokenPairResponse {
        val authUser = authUserService.getOrRegisterAuthUser(socialId, stringSocialType)
        val authToken = authTokenService.createNewAuthToken(authUser.userId)
        return tokenManager.generateTokenPair(authToken)
    }
}
