package com.example.demo.auth.application.dto

import com.fasterxml.jackson.annotation.JsonIgnore

data class TokenPairResponse(
    val accessToken: String,
    @JsonIgnore val refreshToken: String,
)
