package com.example.demo.auth.exception

import com.example.demo.common.exception.CustomException
import org.springframework.http.HttpStatus

class NotExistsTokenException : CustomException(
    code = "NOT_EXIST_TOKEN",
    status = HttpStatus.NOT_FOUND,
    message = "not exists token",
)
