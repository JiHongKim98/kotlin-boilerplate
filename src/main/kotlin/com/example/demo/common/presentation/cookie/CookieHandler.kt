package com.example.demo.common.presentation.cookie

import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component

@Component
class CookieHandler(
    private val cookieProperties: CookieProperties,
) {

    fun addCookie(
        response: HttpServletResponse,
        name: String,
        value: String,
        maxAge: Long = cookieProperties.maxAge,
    ) {
        val cookie = makeCookie(name, value, maxAge)
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString())
    }

    fun deleteCookie(response: HttpServletResponse, name: String) {
        val cookie = makeCookie(name, "", 0)
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString())
    }

    private fun makeCookie(name: String, value: String, maxAge: Long): ResponseCookie {
        return ResponseCookie.from(name, value)
            .maxAge(maxAge)
            .path(cookieProperties.path)
            .domain(cookieProperties.domain)
            .sameSite(cookieProperties.sameSite)
            .secure(cookieProperties.secure)
            .httpOnly(cookieProperties.httpOnly)
            .build()
    }
}
