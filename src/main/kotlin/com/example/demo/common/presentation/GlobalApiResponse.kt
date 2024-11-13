package com.example.demo.common.presentation

import java.time.LocalDateTime

data class GlobalApiResponse(
    val path: String,
    val body: Any?,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)
