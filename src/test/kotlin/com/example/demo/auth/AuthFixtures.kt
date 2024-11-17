package com.example.demo.auth

import com.example.demo.auth.domain.model.AuthToken
import com.example.demo.auth.domain.model.AuthUser
import com.example.demo.auth.domain.vo.SocialType

fun createAuthUser(
    userId: Long = 123456789L,
    socialId: String = "1234567890",
    socialType: SocialType = SocialType.KAKAO,
    authUserId: Long = 987654321L,
): AuthUser {
    return AuthUser(
        userId = userId,
        socialId = socialId,
        socialType = socialType,
        id = authUserId
    )
}

fun createAuthToken(
    userId: Long = 123456789L,
    tokenId: String = "123456789",
): AuthToken {
    return AuthToken(
        userId = userId,
        tokenId = tokenId
    )
}
