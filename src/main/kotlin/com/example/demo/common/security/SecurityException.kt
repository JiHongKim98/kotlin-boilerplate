package com.example.demo.common.security

import org.springframework.security.core.AuthenticationException

class SecurityException(
    override val message: String,
    override val cause: Throwable? = null,
) : AuthenticationException(message, cause)
