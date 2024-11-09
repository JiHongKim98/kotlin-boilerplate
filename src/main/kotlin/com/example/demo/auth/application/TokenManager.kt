package com.example.demo.auth.application

import com.example.demo.auth.application.dto.TokenPairResponse
import com.example.demo.auth.domain.model.AuthToken

interface TokenManager {

    fun generateTokenPair(authToken: AuthToken): TokenPairResponse

    fun extractUserId(token: String): Long

    fun extractTokenId(token: String): String
}
