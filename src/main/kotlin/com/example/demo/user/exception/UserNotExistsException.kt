package com.example.demo.user.exception

import com.example.demo.common.exception.CustomException
import org.springframework.http.HttpStatus

class UserNotExistsException : CustomException(
    code = "USER_NOT_EXISTS",
    status = HttpStatus.NOT_FOUND,
    message = "not exists user"
)
