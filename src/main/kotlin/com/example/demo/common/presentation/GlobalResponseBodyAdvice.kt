package com.example.demo.common.presentation

import com.example.demo.common.exception.dto.GlobalExceptionResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice
class GlobalResponseBodyAdvice : ResponseBodyAdvice<Any> {

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        val path = request.uri.path
        return when (body) {
            is GlobalExceptionResponse -> body
            is String -> {
                response.headers.contentType = MediaType.APPLICATION_JSON
                val apiResponse = GlobalApiResponse(path, body)
                objectMapper.writeValueAsString(apiResponse)
            }

            else -> GlobalApiResponse(path, body)
        }
    }

    companion object {
        private val objectMapper = jacksonObjectMapper()
    }
}
