package com.example.demo.common.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher

class JwtAuthenticationFilter(
    requestMatcher: RequestMatcher
) : AbstractAuthenticationProcessingFilter(requestMatcher) {

    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Authentication {
        val accessToken = request.getHeader(HttpHeaders.AUTHORIZATION)
            ?.takeIf { it.startsWith(BEARER_PREFIX) }
            ?.removePrefix(BEARER_PREFIX)
            ?.trim()
            ?: throw SecurityException(MISSING_BEARER_TOKEN)

        val beforeToken = JwtAuthenticationToken.beforeOf(accessToken)
        return authenticationManager.authenticate(beforeToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
        authResult: Authentication
    ) {
        SecurityContextHolder.createEmptyContext().apply {
            authentication = authResult
            SecurityContextHolder.setContext(this)
        }
        filterChain.doFilter(request, response)
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        SecurityContextHolder.clearContext()
        failureHandler?.onAuthenticationFailure(request, response, failed)
    }

    companion object {
        private const val BEARER_PREFIX = "Bearer "
        private const val MISSING_BEARER_TOKEN = "bearer token was not provided"
    }
}
