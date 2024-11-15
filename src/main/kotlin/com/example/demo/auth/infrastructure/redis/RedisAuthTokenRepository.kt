package com.example.demo.auth.infrastructure.redis

import com.example.demo.auth.domain.model.AuthToken
import com.example.demo.auth.domain.repository.AuthTokenRepository
import com.example.demo.common.utils.redis.getAndDeleteWithDeserialize
import com.example.demo.common.utils.redis.getWithDeserialize
import com.example.demo.common.utils.redis.setWithSerialize
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class RedisAuthTokenRepository(
    private val redisTemplate: RedisTemplate<String, String>,
    @Value("\${jwt.refresh-exp}") private val timeout: Long,
) : AuthTokenRepository {

    override fun save(authToken: AuthToken): AuthToken {
        redisTemplate.opsForValue().setWithSerialize(
            key = authToken.tokenId,
            value = authToken,
            timeout = timeout
        )
        return authToken
    }

    override fun findByTokenId(tokenId: String): AuthToken? {
        return redisTemplate.opsForValue().getWithDeserialize(tokenId, AuthToken::class)
    }

    override fun findAndDeleteByTokenId(tokenId: String): AuthToken? {
        return redisTemplate.opsForValue().getAndDeleteWithDeserialize(tokenId, AuthToken::class)
    }

    override fun deleteByTokenId(tokenId: String) {
        redisTemplate.delete(tokenId)
    }
}
