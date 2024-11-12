package com.example.demo.user.domain.model

import com.example.demo.common.domain.BaseTimeEntity
import com.example.demo.common.domain.IdGenerator
import com.example.demo.user.domain.vo.Name
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Embedded
    val name: Name,

    @Column(unique = true, nullable = false)
    val authUserId: Long,

    @Id
    @Column(name = "user_id", nullable = false)
    val id: Long = IdGenerator.generate(),
) : BaseTimeEntity() {

    companion object {
        fun create(id: Long, authUserId: Long): User {
            return User(
                id = id,
                authUserId = authUserId,
                name = Name()
            )
        }
    }
}
