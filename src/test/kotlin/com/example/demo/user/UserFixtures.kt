package com.example.demo.user

import com.example.demo.user.domain.model.User

fun createUser(
    userId: Long = 123456789L,
    authUserId: Long = 987654321L,
): User {
    return User.create(
        id = userId,
        authUserId = authUserId,
    )
}
