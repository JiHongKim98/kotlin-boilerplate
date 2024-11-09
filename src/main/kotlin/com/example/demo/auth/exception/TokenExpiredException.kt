package com.example.demo.auth.exception

import com.example.demo.common.exception.CustomException
import org.springframework.http.HttpStatus

class TokenExpiredException : CustomException(
    code = "ALREADY_EXPIRED_TOKEN",
    status = HttpStatus.UNAUTHORIZED,
    message = "token has expired",
)
