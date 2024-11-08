package com.example.demo.auth.application

import com.example.demo.auth.domain.model.AuthToken
import com.example.demo.auth.domain.repository.AuthTokenRepository
import com.example.demo.auth.exception.NotExistsTokenException
import com.example.demo.auth.exception.TokenUserIdMismatchException
import org.springframework.stereotype.Service

@Service
class AuthTokenService(
    private val authTokenRepository: AuthTokenRepository,
) {

    fun createNewAuthToken(userId: Long): AuthToken {
        val authToken = AuthToken.create(userId)
        return authTokenRepository.save(authToken)
    }

    fun regenerateAuthToken(tokenId: String): AuthToken {
        return authTokenRepository.findAndDeleteByTokenId(tokenId)
            ?.let {
                val newAuthToken = AuthToken.create(it.userId)
                authTokenRepository.save(newAuthToken)
            }
            ?: throw NotExistsTokenException()
    }

    fun removeTokenIfMatchUserId(tokenId: String, userId: Long) {
        authTokenRepository.findByTokenId(tokenId)
            ?.let {
                when (it.isMatchUserId(userId)) {
                    true -> authTokenRepository.deleteByTokenId(tokenId)
                    else -> throw TokenUserIdMismatchException()
                }
            }
            ?: throw NotExistsTokenException()
    }
}
