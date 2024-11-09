package com.example.demo.auth.exception

import com.example.demo.common.exception.CustomException
import org.springframework.http.HttpStatus

class TokenParseException : CustomException(
    code = "INVALID_TOKEN",
    status = HttpStatus.UNAUTHORIZED,
    message = "failed to parse JWT token",
)
