package com.example.demo.user.application.dto

import com.example.demo.user.domain.model.User

data class UserInfoResponse(
    val userId: Long,
    val name: String,
) {

    companion object {
        fun of(findUser: User): UserInfoResponse {
            return UserInfoResponse(
                userId = findUser.id,
                name = findUser.name.value,
            )
        }
    }
}
