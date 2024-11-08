package com.example.demo.auth.exception

import com.example.demo.common.exception.CustomException
import org.springframework.http.HttpStatus

class TokenUserIdMismatchException : CustomException(
    code = "UNMATCHED_TOKEN_ID",
    status = HttpStatus.FORBIDDEN,
    message = "the provided tokenId does not match the given userId",
)
