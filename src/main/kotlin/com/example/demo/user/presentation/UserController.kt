package com.example.demo.user.presentation

import com.example.demo.user.application.UserService
import com.example.demo.user.application.dto.UserInfoResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/me")
    fun getCurrentUserInfo(
        @AuthenticationPrincipal userId: Long,
    ): ResponseEntity<UserInfoResponse> {
        return ResponseEntity.ok(userService.findUserInfoByUserId(userId))
    }

    @GetMapping("/{userId}")
    fun getUserInfo(
        @PathVariable userId: Long,
    ): ResponseEntity<UserInfoResponse> {
        return ResponseEntity.ok(userService.findUserInfoByUserId(userId))
    }
}
