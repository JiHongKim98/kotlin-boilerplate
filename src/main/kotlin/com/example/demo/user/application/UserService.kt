package com.example.demo.user.application

import com.example.demo.user.application.dto.UserInfoResponse
import com.example.demo.user.domain.repository.UserRepository
import com.example.demo.user.exception.UserNotExistsException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    fun findUserInfoByUserId(userId: Long): UserInfoResponse {
        val findUser = userRepository.findByIdOrNull(userId)
            ?: throw UserNotExistsException()
        return UserInfoResponse.of(findUser)
    }
}
