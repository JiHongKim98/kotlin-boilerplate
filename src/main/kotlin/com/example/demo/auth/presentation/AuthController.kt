package com.example.demo.auth.presentation

import com.example.demo.auth.application.AuthFacadeService
import com.example.demo.auth.application.dto.TokenPairResponse
import com.example.demo.common.presentation.cookie.CookieHandler
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val cookieHandler: CookieHandler,
    private val authFacadeService: AuthFacadeService,
) {

    @PostMapping("/reissue")
    fun reissue(
        @CookieValue(name = REFRESH_TOKEN_COOKIE_NAME) refreshToken: String,
        response: HttpServletResponse,
    ): ResponseEntity<TokenPairResponse> {
        val tokenPairResponse = authFacadeService.reissueToken(refreshToken)
        cookieHandler.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, tokenPairResponse.refreshToken)
        return ResponseEntity.ok(tokenPairResponse)
    }

    @PostMapping("/logout")
    fun logout(
        @AuthenticationPrincipal userId: Long,
        @CookieValue(name = REFRESH_TOKEN_COOKIE_NAME) refreshToken: String,
        response: HttpServletResponse,
    ): ResponseEntity<Void> {
        authFacadeService.deleteToken(userId, refreshToken)
        cookieHandler.deleteCookie(response, REFRESH_TOKEN_COOKIE_NAME)
        return ResponseEntity.ok().build()
    }

    companion object {
        private const val REFRESH_TOKEN_COOKIE_NAME = "refresh_token"
    }
}
