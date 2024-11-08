package com.example.demo.auth.domain.model

import com.example.demo.auth.domain.vo.SocialType
import com.example.demo.common.domain.IdGenerator
import jakarta.persistence.*

@Entity
@Table(name = "auth_users")
class AuthUser(
    @Column(name = "user_id", nullable = true)
    val userId: Long = IdGenerator.generate(),

    @Column(name = "social_type", nullable = false)
    @Enumerated(EnumType.STRING)
    val socialType: SocialType,

    @Column(name = "social_id", nullable = false)
    val socialId: String,

    @Id
    @Column(name = "auth_user_id")
    val id: Long = IdGenerator.generate(),
) {

    companion object {
        fun create(socialId: String, socialType: SocialType): AuthUser {
            return AuthUser(
                socialId = socialId,
                socialType = socialType,
            )
        }
    }
}
