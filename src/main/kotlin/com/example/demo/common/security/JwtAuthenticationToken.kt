package com.example.demo.common.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(
    principal: Any,
    credentials: Any,
    authorities: Collection<GrantedAuthority>? = null,
) : UsernamePasswordAuthenticationToken(principal, credentials, authorities) {

    fun getAccessToken(): String {
        return this.principal as String
    }

    companion object {
        private const val EMPTY_CREDENTIALS = ""

        fun beforeOf(accessToken: String): JwtAuthenticationToken {
            return JwtAuthenticationToken(accessToken, EMPTY_CREDENTIALS)
        }

        fun afterOf(userId: Long): JwtAuthenticationToken {
            return JwtAuthenticationToken(userId, EMPTY_CREDENTIALS, null)
        }
    }
}
