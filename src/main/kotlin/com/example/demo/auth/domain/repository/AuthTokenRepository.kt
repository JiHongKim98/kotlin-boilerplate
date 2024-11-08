package com.example.demo.auth.domain.repository

import com.example.demo.auth.domain.model.AuthToken

interface AuthTokenRepository {

    fun save(authToken: AuthToken): AuthToken

    fun findByTokenId(tokenId: String): AuthToken?

    fun findAndDeleteByTokenId(tokenId: String): AuthToken?

    fun deleteByTokenId(tokenId: String)
}
