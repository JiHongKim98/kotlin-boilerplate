package com.example.demo.auth.domain.event

import com.example.demo.auth.domain.model.AuthUser

data class AuthUserCreateEvent(
    val userId: Long,
    val authUserId: Long,
) {

    companion object {
        fun create(authUser: AuthUser): AuthUserCreateEvent {
            return AuthUserCreateEvent(
                userId = authUser.userId,
                authUserId = authUser.id,
            )
        }
    }
}
