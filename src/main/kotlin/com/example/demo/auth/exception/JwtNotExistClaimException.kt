package com.example.demo.auth.exception

import com.example.demo.common.exception.CustomException
import org.springframework.http.HttpStatus

class JwtNotExistClaimException : CustomException(
    code = "NOT_EXIST_CLAIM_KEY",
    status = HttpStatus.UNAUTHORIZED,
    message = "not exists key in claims",
)
