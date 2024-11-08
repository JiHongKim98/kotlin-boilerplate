package com.example.demo.auth.domain.model

import java.util.*

class AuthToken(
    val tokenId: String = UUID.randomUUID().toString(),
    val userId: Long,
) {

    fun isMatchUserId(matchId: Long): Boolean = (userId == matchId)

    companion object {
        fun create(userId: Long): AuthToken {
            return AuthToken(
                userId = userId,
            )
        }
    }
}
